package Client;

import Enums.Actions;
import Enums.Parameters;
import GreenfootNetworking.NetworkedActor;
import GreenfootNetworking.NetworkedWorld;
import org.json.simple.JSONObject;

public class MessageEncoder {

    private static MessageEncoder instance;
    private final JSONObject message = new JSONObject();

    protected static MessageEncoder getInstance(){
        if(instance == null){
            instance = new MessageEncoder();
        }
        return instance;
    }

    public void sendHandshakeUDP(Client self){
        message.clear();
        message.put(Parameters.Action.name(), Actions.HANDSHAKE.name());
        message.put(Parameters.ClientId.name(), self.getId());
        self.sendUDPMessage(message.toJSONString());
    }

    public void sendPositionUpdateUDP(Client self, NetworkedActor actor){
        message.clear();
        if(actor.getWorldId() > -1){
            message.put(Parameters.Action.name(), Actions.UPDATE_POSITION.name());
            message.put(Parameters.ClientId.name(), self.getId());
            message.put(Parameters.ActorId.name(), actor.getId());
            message.put(Parameters.NewXPosition.name(), actor.getX());
            message.put(Parameters.NewYPosition.name(), actor.getY());
            self.sendUDPMessage(message.toJSONString());
        }
    }
    public void sendImageUpdateTCP(Client self, int actorId, String newImagePath){
        message.clear();
        message.put(Parameters.Action.name(), Actions.UPDATE_IMAGE.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.ActorId.name(), actorId);
        message.put(Parameters.NewImageFilePath.name(), newImagePath);
        self.sendTCPMessage(message.toJSONString());
    }
    public void sendRotationUpdateUDP(Client self, int actorId){
        message.clear();
        message.put(Parameters.Action.name(), Actions.UPDATE_ROTATION.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.ActorId.name(), actorId);
        message.put(Parameters.NewRotation.name(), self.getActor(actorId).getRotation());
        self.sendUDPMessage(message.toJSONString());
    }

    public void sendCreateActorTCP(Client self, NetworkedActor actor){
        message.clear();
        message.put(Parameters.Action.name(), Actions.CREATE_ACTOR.name());
        message.put(Parameters.NewActorInformation.name(), actor.toJsonString());
        message.put(Parameters.ClientId.name(), self.getId());
        self.sendTCPMessage(message.toJSONString());
    }

    public void sendAddWorldTCP(Client self, NetworkedWorld newWorld){
        message.clear();
        message.put(Parameters.Action.name(), Actions.ADD_WORLD.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.NewWorldInformation.name(), newWorld.toJsonString());
        self.sendTCPMessage(message.toJSONString());
    }

    public void requestOtherActorsTCP(Client self) {
        message.clear();
        message.put(Parameters.Action.name(), Actions.REQUEST_OTHER_ACTORS.name());
        message.put(Parameters.ClientId.name(), self.getId());
        self.sendTCPMessage(message.toJSONString());
    }

    public void sendResetWorldTCP(Client self){
        message.clear();
        message.put(Parameters.Action.name(), Actions.RESET.name());
        message.put(Parameters.ClientId.name(), self.getId());
        self.sendTCPMessage(message.toJSONString());
    }

    public void sendAddActorToWorldTCP(Client self, NetworkedActor addable) {
        message.clear();
        message.put(Parameters.Action.name(), Actions.ADD_ACTOR_TO_WORLD.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.ActorId.name(), addable.getId());
        message.put(Parameters.WorldId.name(), addable.getWorldId());
        message.put(Parameters.NewXPosition.name(), addable.getStartX());
        message.put(Parameters.NewYPosition.name(), addable.getStartY());
        self.sendTCPMessage(message.toJSONString());
    }

    public void sendRemoveActorFromWorldTCP(Client self, NetworkedActor removable) {
        message.clear();
        message.put(Parameters.Action.name(), Actions.REMOVE_ACTOR_FROM_WORLD.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.ActorId.name(), removable.getId());
        message.put(Parameters.WorldId.name(), removable.getWorldId());
        self.sendTCPMessage(message.toJSONString());
    }
}
