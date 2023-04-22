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

    protected void sendCreatedActorTCP(int clientId, int oldActorId, int newActorId){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.CREATED_ACTOR.name());
        message.put(Parameters.ActorId.name(), newActorId);
        message.put(Parameters.OldActorId.name(), oldActorId);
        Server.sendToClientTCP(message.toJSONString(), clientId);
    }

    protected void sendOtherActorsTCP(int fromClient) {
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.REQUEST_OTHER_ACTORS.name());
        message.put(Parameters.AllCurrentActors.name(), Server.getAllCurrentActors());
        Server.sendToClientTCP(message.toJSONString(), fromClient);
    }

    protected void informCreateActorTCP(String newActorInformation, int fromClient){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.CREATE_ACTOR.name());
        message.put(Parameters.NewActorInformation.name(), newActorInformation);
        Server.informTCP(message.toJSONString(), new int[]{fromClient});
    }
}
