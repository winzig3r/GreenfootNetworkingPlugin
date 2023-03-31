package Server;

import Enums.Actions;
import Enums.Parameters;
import org.json.simple.*;

import java.net.DatagramSocket;
import java.net.InetAddress;

class MessageDecoder {
    private static MessageDecoder instance;

    protected static MessageDecoder getInstance(){
        if(instance == null){
            instance = new MessageDecoder();
        }
        return instance;
    }

    protected void decodeUDPMessage(String message, InetAddress address, int port, DatagramSocket socket){
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(message);
        Actions action = Actions.valueOf((String)jsonMessage.get(Parameters.Action.name()));
        if(action.equals(Actions.HANDSHAKE)){
            int clientId = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
            Server.getClient(clientId).recogniseUDPConnection(new UDPServerClient(address, port, socket));
        }else{
            decodeMessage(message);
        }
    }

    protected void decodeMessage(String message){
        //System.out.println("Received message on the Serverside: " + message);
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(message);
        Actions action;
        try {
            action = Actions.valueOf((String)jsonMessage.get(Parameters.Action.name()));
        }catch (Exception e){
            action = Actions.UNKNOWN;
        }
        if (action.equals(Actions.UPDATE_POSITION)) {
            int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
            Server.informUDP(message, new int[]{fromClient});
        } else if (action.equals(Actions.UPDATE_ROTATION)) {
            int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
            Server.informUDP(message, new int[]{fromClient});
        } else if (action.equals(Actions.UPDATE_IMAGE)) {
            int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
            int actorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
            String newImagePath = (String) jsonMessage.get(Parameters.NewImageFilePath.name());
            Server.updateActorImage(actorId, newImagePath);
            Server.informUDP(message, new int[]{fromClient});
        }else if (action.equals(Actions.ADD_ACTOR)) {
            int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
            int worldId = ((Long)jsonMessage.get(Parameters.WorldId.name())).intValue();
            int xStart = ((Long)jsonMessage.get(Parameters.NewXPosition.name())).intValue();
            int yStart = ((Long)jsonMessage.get(Parameters.NewYPosition.name())).intValue();
            String imageFile = (String) jsonMessage.get(Parameters.NewImageFilePath.name());
            Server.addActorToWorld(actorId, worldId, xStart, yStart, imageFile);
            MessageEncoder.getInstance().broadcastAddActorTCP(actorId, worldId, xStart, yStart, imageFile);
        } else if (action.equals(Actions.CREATE_ACTOR)) {
            JSONObject newActorInformation = (JSONObject) JSONValue.parse((String) jsonMessage.get(Parameters.NewActorInformation.name()));
            int newActorId = Server.getNewActorId();
            int oldActorId = ((Long) newActorInformation.get(Parameters.ActorId.name())).intValue();
            int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
            newActorInformation.put(Parameters.ActorId.name(), newActorId);
            Server.createNewActor(newActorInformation.toJSONString());
            MessageEncoder.getInstance().informCreateActorTCP(newActorInformation.toJSONString(), fromClient);
            MessageEncoder.getInstance().sendUpdateActorIdTCP(fromClient, oldActorId, newActorId);
        } else if (action.equals(Actions.REMOVE_ACTOR)) {
            int actorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
            Server.removeActor(actorId);
            MessageEncoder.getInstance().broadcastRemoveClientTCP(actorId);
        } else if (action.equals(Actions.ADD_WORLD)) {
            Server.addNewWorld((String) jsonMessage.get(Parameters.NewWorldInformation.name()));
        } else{
            //System.out.println("Action not known on server: " + jsonMessage);
        }
    }
}

//TODO: for some reason the creation call of an actor is sent before the Handshake with the server could be made
