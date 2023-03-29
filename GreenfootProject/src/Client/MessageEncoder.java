package Client;

import Enums.Actions;
import Enums.Parameters;
import GreenfootNetworking.NetworkedWorld;
import org.json.simple.JSONObject;

public class MessageEncoder {

    private static MessageEncoder instance;

    protected static MessageEncoder getInstance(){
        if(instance == null){
            instance = new MessageEncoder();
        }
        return instance;
    }

    public void sendHandshakeUDP(Client self){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.HANDSHAKE.name());
        message.put(Parameters.ClientId.name(), self.getId());
        self.sendUDPMessage(message.toJSONString());
    }

    public void sendPositionUpdateUDP(Client self, int actorId){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.UPDATE_POSITION.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.ActorId.name(), actorId);
        message.put(Parameters.NewXPosition.name(), self.getActor(actorId).getX());
        message.put(Parameters.NewYPosition.name(), self.getActor(actorId).getY());
        self.sendUDPMessage(message.toJSONString());
    }
    public void sendImageUpdateTCP(Client self, int actorId, String newImagePath){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.UPDATE_IMAGE.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.ActorId.name(), actorId);
        message.put(Parameters.NewImageFilePath.name(), newImagePath);
        self.sendTCPMessage(message.toJSONString());
    }
    public void sendRotationUpdateUDP(Client self, int actorId){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.UPDATE_ROTATION.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.ActorId.name(), actorId);
        message.put(Parameters.NewRotation.name(), self.getActor(actorId).getRotation());
        self.sendUDPMessage(message.toJSONString());
    }

    public void sendAddActorTCP(Client self, int actorId, int worldId, int x, int y){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.ADD_ACTOR.name());
        message.put(Parameters.ActorId.name(), actorId);
        message.put(Parameters.WorldId.name(), worldId);
        message.put(Parameters.NewXPosition.name(), x);
        message.put(Parameters.NewYPosition.name(), y);
        self.sendTCPMessage(message.toJSONString());
    }

    public void sendCreateActorTCP(Client self){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.CREATE_ACTOR.name());
        self.sendTCPMessage(message.toJSONString());
    }

    public void sendRemoveActorTCP(Client self, int actorId){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.REMOVE_ACTOR.name());
        message.put(Parameters.ActorId.name(), actorId);
        self.sendTCPMessage(message.toJSONString());
    }

    public void sendAddWorldTCP(Client self, NetworkedWorld newWorld){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.ADD_WORLD.name());
        message.put(Parameters.NewWorldInformation.name(), newWorld.toJsonString());
        self.sendTCPMessage(message.toJSONString());
    }
}
