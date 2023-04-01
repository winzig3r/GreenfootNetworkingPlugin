package Server;

import Enums.Actions;
import Enums.Parameters;
import org.json.simple.JSONObject;

class MessageEncoder {

    private static MessageEncoder instance;

    protected static MessageEncoder getInstance(){
        if(instance == null){
            instance = new MessageEncoder();
        }
        return instance;
    }

    protected void sendHandshakeTCP(int clientId){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.HANDSHAKE.name());
        message.put(Parameters.ClientId.name(), clientId);
        message.put(Parameters.AllCurrentActors.name(), Server.getAllCurrentActors());
        Server.sendToClientTCP(message.toJSONString(), clientId);
    }

    protected void sendUpdateActorIdTCP(int clientId, int oldActorId, int newActorId){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.UPDATE_ACTOR_ID.name());
        message.put(Parameters.ActorId.name(), newActorId);
        message.put(Parameters.OldActorId.name(), oldActorId);
        Server.sendToClientTCP(message.toJSONString(), clientId);
    }

    protected void broadcastAddActorTCP(int actorId, int worldId, int xStart, int yStart, String imageFilePath){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.ADD_ACTOR.name());
        message.put(Parameters.ActorId.name(), actorId);
        message.put(Parameters.WorldId.name(), worldId);
        message.put(Parameters.NewXPosition.name(), xStart);
        message.put(Parameters.NewYPosition.name(), yStart);
        message.put(Parameters.NewImageFilePath.name(), imageFilePath);
        Server.broadcastTCP(message.toJSONString());
    }

    protected void informCreateActorTCP(String newActorInformation, int fromClient){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.CREATE_ACTOR.name());
        message.put(Parameters.NewActorInformation.name(), newActorInformation);
        Server.informTCP(message.toJSONString(), new int[]{fromClient});
    }

    protected void broadcastRemoveClientTCP(int actorId, int worldId){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.REMOVE_ACTOR.name());
        message.put(Parameters.ActorId.name(), actorId);
        message.put(Parameters.WorldId.name(), worldId);
        Server.broadcastTCP(message.toJSONString());
    }
}
