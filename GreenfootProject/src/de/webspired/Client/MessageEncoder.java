package de.webspired.Client;

import de.webspired.Enums.Actions;
import de.webspired.Enums.Parameters;
import de.webspired.GreenfootNetworking.NetworkedActor;
import de.webspired.GreenfootNetworking.NetworkedWorld;
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

    /**
     * Sends the Handshake message to make the de.webspired.Server recognise the UDP connection of this client (self) {@link Actions#HANDSHAKE}
     * @param self The client sending the message
     */
    public void sendHandshakeUDP(Client self){
        message.clear();
        message.put(Parameters.Action.name(), Actions.HANDSHAKE.name());
        message.put(Parameters.ClientId.name(), self.getId());
        self.sendUDPMessage(message.toJSONString());
    }

    /**
     * Every time an actor moves a movement packages is sent by this function {@link Actions#UPDATE_POSITION}
     * @param self The client sending the message
     * @param actor The actor of which the position updated
     */
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

    /**
     * Every time an actor changes its image this package is sent {@link Actions#UPDATE_IMAGE}
     * @param self The client that is sending the message (in case of multiple clients in one greenfoot instance)
     * @param actorId The id of the actor that is changing the image
     * @param newImagePath The new path to the image (has to be the same across all clients, other a FileNotFoundException will be thrown)
     */
    public void sendImageUpdateTCP(Client self, int actorId, String newImagePath){
        message.clear();
        message.put(Parameters.Action.name(), Actions.UPDATE_IMAGE.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.ActorId.name(), actorId);
        message.put(Parameters.NewImageFilePath.name(), newImagePath);
        self.sendTCPMessage(message.toJSONString());
    }

    /**
     *Every time an actor rotates in some way this package is sent by this function {@link Actions#UPDATE_ROTATION}
     * @param self The client that is sending the message (in case of multiple clients in one greenfoot instance)
     * @param actorId The actor id that rotated
     */
    public void sendRotationUpdateUDP(Client self, int actorId){
        message.clear();
        message.put(Parameters.Action.name(), Actions.UPDATE_ROTATION.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.ActorId.name(), actorId);
        message.put(Parameters.NewRotation.name(), self.getActor(actorId).getRotation());
        self.sendUDPMessage(message.toJSONString());
    }

    /**
     * Every time a client creates an actor this package is sent by this function {@link Actions#CREATE_ACTOR}
     * @param self The client that is sending the message (in case of multiple clients in one greenfoot instance)
     * @param actor The newly created actor
     */
    public void sendCreateActorTCP(Client self, NetworkedActor actor){
        message.clear();
        message.put(Parameters.Action.name(), Actions.CREATE_ACTOR.name());
        message.put(Parameters.NewActorInformation.name(), actor.toJsonString());
        message.put(Parameters.ClientId.name(), self.getId());
        self.sendTCPMessage(message.toJSONString());
    }

    /**
     * When the constructor of a class extending NetworkedWorld is called this package is sent to inform the server NOT other clients {@link Actions#ADD_WORLD}
     * @param self The client that is sending the message (in case of multiple clients in one greenfoot instance)
     * @param newWorld The newly created world
     */
    public void sendAddWorldTCP(Client self, NetworkedWorld newWorld){
        message.clear();
        message.put(Parameters.Action.name(), Actions.ADD_WORLD.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.NewWorldInformation.name(), newWorld.toJsonString());
        self.sendTCPMessage(message.toJSONString());
    }

    /**
     * Sometimes the client needs to request all other actors that are currently spawned in regardless of the world. This is what
     * this package does {@link Actions#REQUEST_OTHER_ACTORS}
     * @param self The client that is sending the message (in case of multiple clients in one greenfoot instance)
     */
    public void requestOtherActorsTCP(Client self) {
        message.clear();
        message.put(Parameters.Action.name(), Actions.REQUEST_OTHER_ACTORS.name());
        message.put(Parameters.ClientId.name(), self.getId());
        self.sendTCPMessage(message.toJSONString());
    }

    /**
     * If the client presses the reset button at the bottom of the screen its actors get deleted from other clients {@link Actions#RESET}
     * @param self The client that is sending the message (in case of multiple clients in one greenfoot instance)
     */
    public void sendResetWorldTCP(Client self){
        message.clear();
        message.put(Parameters.Action.name(), Actions.RESET.name());
        message.put(Parameters.ClientId.name(), self.getId());
        self.sendTCPMessage(message.toJSONString());
    }

    /**
     * If an actor gets added to any world this packet get sent {@link Actions#ADD_ACTOR_TO_WORLD}
     * @param self The client that is sending the message (in case of multiple clients in one greenfoot instance)
     * @param addable The actor that is newly added
     */
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

    /**
     * If an actor is removed from any world this packet get sent {@link Actions#REMOVE_ACTOR_FROM_WORLD}
     * @param self The client that is sending the message (in case of multiple clients in one greenfoot instance)
     * @param removable The actor that is removed
     */
    public void sendRemoveActorFromWorldTCP(Client self, NetworkedActor removable) {
        message.clear();
        message.put(Parameters.Action.name(), Actions.REMOVE_ACTOR_FROM_WORLD.name());
        message.put(Parameters.ClientId.name(), self.getId());
        message.put(Parameters.ActorId.name(), removable.getId());
        message.put(Parameters.WorldId.name(), removable.getWorldId());
        self.sendTCPMessage(message.toJSONString());
    }
}
