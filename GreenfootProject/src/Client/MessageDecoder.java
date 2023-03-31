package Client;

import Enums.Actions;
import Enums.Parameters;
import GreenfootNetworking.NetworkedActor;
import GreenfootNetworking.NetworkedWorld;
import org.json.simple.*;

import java.util.Map;

/**
 * A Singleton Pattern for the receiver side of a client: Decodes the JSON messages sent to the client and executes the actions bound to those sent packets
 */
public class MessageDecoder {
    private static MessageDecoder instance;

    protected static MessageDecoder getInstance(){
        if(instance == null){
            instance = new MessageDecoder();
        }
        return instance;
    }

    /**
     * @param message: The message received by self
     * @param self: The Client who received the message
     */
    protected void decodeMessage(String message, Client self){
        System.out.println("Received message on the clientside: " + message);
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(message);
        Actions action;
        try {
            action = Actions.valueOf((String)jsonMessage.get(Parameters.Action.name()));
        }catch (Exception e){
            action = Actions.UNKNOWN;
        }
        if(action.equals(Actions.HANDSHAKE)){
            self.setId(((Long) jsonMessage.get(Parameters.ClientId.name())).intValue());
            JSONArray allActorData = (JSONArray) JSONValue.parse((String) jsonMessage.get(Parameters.AllCurrentActors.name()));
            MessageEncoder.getInstance().sendHandshakeUDP(self);
            for(Object actorData : allActorData.toArray()){
                JSONObject currentActorData = (JSONObject) JSONValue.parse((String) actorData);
                self.createGhostActor(new NetworkedActor(currentActorData.toJSONString()));
            }
        } else if (action.equals(Actions.UPDATE_POSITION)) {
            int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
            int newX = ((Long)jsonMessage.get(Parameters.NewXPosition.name())).intValue();
            int newY = ((Long)jsonMessage.get(Parameters.NewYPosition.name())).intValue();
            //TODO: Check if the specified actor id even exists
            self.getActor(actorId).setLocation(newX, newY);
        } else if (action.equals(Actions.UPDATE_ROTATION)) {
            int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
            int newRotation = ((Long)jsonMessage.get(Parameters.NewRotation.name())).intValue();
            //TODO: Check if the specified actor id even exists
            self.getActor(actorId).setRotation(newRotation);
        } else if (action.equals(Actions.UPDATE_IMAGE)) {
            //TODO: Check if the specified actor id even exists
            int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
            String imageFilePath = (String) jsonMessage.get(Parameters.NewImageFilePath.name());
            self.getActor(actorId).setImage(imageFilePath);
        } else if (action.equals(Actions.UPDATE_ACTOR_ID)) {
            int oldActorId = ((Long) jsonMessage.get(Parameters.OldActorId.name())).intValue();
            int newActorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
            self.updateActorId(oldActorId, newActorId);
        } else if (action.equals(Actions.REMOVE_ACTOR)) {
            int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
            int worldId = ((Long)jsonMessage.get(Parameters.WorldId.name())).intValue();
            self.removeActor(actorId, worldId);
        } else if (action.equals(Actions.ADD_ACTOR)) {
            int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
            int worldId = ((Long)jsonMessage.get(Parameters.WorldId.name())).intValue();
            int startX = ((Long)jsonMessage.get(Parameters.NewXPosition.name())).intValue();
            int startY = ((Long)jsonMessage.get(Parameters.NewYPosition.name())).intValue();
            String imageFilePath = (String) jsonMessage.get(Parameters.NewImageFilePath.name());
            self.addActorToWorld(actorId, worldId, startX, startY, imageFilePath);
        } else if (action.equals(Actions.CREATE_ACTOR)) {
            String newActorInformation = (String)jsonMessage.get(Parameters.NewActorInformation.name());
            NetworkedActor networkedActor = new NetworkedActor(newActorInformation);
            self.createGhostActor(networkedActor);
        } else{
            System.out.println("Action not known on client: " + jsonMessage);
        }
    }
}
