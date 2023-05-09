package de.webspired.Client;

import de.webspired.Enums.Actions;
import de.webspired.Enums.Parameters;
import de.webspired.GreenfootNetworking.GreenfootNetworkManager;
import de.webspired.GreenfootNetworking.NetworkedActor;
import org.json.simple.*;

import java.util.*;

/**
 * A Singleton Pattern for the receiver side of a client: Decodes the JSON messages sent to the client and executes the actions bound to those sent packets
 */
public class MessageDecoder {
    private static MessageDecoder instance;
    private final PostExecutor postExecutor;
    protected static MessageDecoder getInstance(){
        if(instance == null){
            instance = new MessageDecoder();
        }
        return instance;
    }

    public MessageDecoder(){
        this.postExecutor = new PostExecutor(this);
        postExecutor.start();
    }

    /**
     * @param message: The message received by self
     * @param self: The de.webspired.Client who received the message
     * @return Returns whether a message has been successfully processed or not
     */
    protected boolean decodeMessage(String message, Client self){
        System.out.println("Received message on client ("+self.getId()+"): " + message);
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(message);
        Actions action;
        try {
            action = Actions.valueOf((String)jsonMessage.get(Parameters.Action.name()));
            if(action.equals(Actions.HANDSHAKE)){
                JSONArray allActorData = (JSONArray) JSONValue.parse((String) jsonMessage.get(Parameters.AllCurrentActors.name()));
                for(Object actorData : allActorData.toArray()){
                    JSONObject currentActorData = (JSONObject) JSONValue.parse((String) actorData);
                    self.createGhostActor(new NetworkedActor(currentActorData));
                }
                self.setId(((Long) jsonMessage.get(Parameters.ClientId.name())).intValue());
                MessageEncoder.getInstance().sendHandshakeUDP(self);
            } else if (action.equals(Actions.UPDATE_POSITION)) {
                int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
                int newX = ((Long)jsonMessage.get(Parameters.NewXPosition.name())).intValue();
                int newY = ((Long)jsonMessage.get(Parameters.NewYPosition.name())).intValue();
                self.getActor(actorId).setLocation(newX, newY);
            } else if (action.equals(Actions.UPDATE_ROTATION)) {
                int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
                int newRotation = ((Long)jsonMessage.get(Parameters.NewRotation.name())).intValue();
                self.getActor(actorId).setRotation(newRotation);
            } else if (action.equals(Actions.UPDATE_IMAGE)) {
                int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
                String imageFilePath = (String) jsonMessage.get(Parameters.NewImageFilePath.name());
                self.getActor(actorId).setImage(imageFilePath);
                self.getNetworkedWorld(self.getActor(actorId).getWorldId()).repaint();
            }else if (action.equals(Actions.CREATE_ACTOR)) {
                JSONObject newActorInformation = (JSONObject) JSONValue.parse((String) jsonMessage.get(Parameters.NewActorInformation.name()));
                NetworkedActor networkedActor = new NetworkedActor(newActorInformation);
                self.createGhostActor(networkedActor);
            } else if (action.equals(Actions.CREATED_ACTOR)) {
                int oldActorId = ((Long) jsonMessage.get(Parameters.OldActorId.name())).intValue();
                int newActorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
                self.makeActorAddable(oldActorId, newActorId);
            } else if (action.equals(Actions.ADD_ACTOR_TO_WORLD)) {
                int actorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
                int worldId = ((Long) jsonMessage.get(Parameters.WorldId.name())).intValue();
                int startX = ((Long) jsonMessage.get(Parameters.NewXPosition.name())).intValue();
                int startY = ((Long) jsonMessage.get(Parameters.NewYPosition.name())).intValue();
                self.receiveToAdd(actorId, worldId, startX, startY);
            } else if (action.equals(Actions.REMOVE_ACTOR_FROM_WORLD)) {
                int actorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
                int worldId = ((Long) jsonMessage.get(Parameters.WorldId.name())).intValue();
                self.receiveToRemove(actorId, worldId);
            } else if (action.equals(Actions.REQUEST_OTHER_ACTORS)) {
                JSONArray allActorData = (JSONArray) JSONValue.parse((String) jsonMessage.get(Parameters.AllCurrentActors.name()));
                for(Object actorData : allActorData.toArray()){
                    JSONObject currentActorData = (JSONObject) JSONValue.parse((String) actorData);
                    self.createGhostActor(new NetworkedActor(currentActorData));
                }
                self.repaintWorlds();
            }else if (action.equals(Actions.RESET)) {
                int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
                self.removeClientActors(fromClient);
            }
            return true;
        }catch (NullPointerException e){
            postExecutor.addNewMessage(message);
            return false;
        }catch (IllegalArgumentException e){
            return false;
        }
    }

    /**
     * If a message executes weirdly and throws an error, this message is put into the Post Executor Thread
     * The Thread continuously runs every 100 milliseconds and re-executes wrongly run messages for up to 10 times until they are discarded
     */
    static class PostExecutor extends Thread{
        private final ArrayList<AbstractMap.SimpleEntry<Integer, String>> unprocessedMessages= new ArrayList<>();
        private final MessageDecoder decoder;
        private PostExecutor(MessageDecoder decoder){
            this.decoder = decoder;
        }

        public void addNewMessage(String message){
            if(checkForMessage(message)) return;
            this.unprocessedMessages.add(new AbstractMap.SimpleEntry<>(0, message));
        }
        @Override
        public void run() {
            super.run();
            while(!Thread.currentThread().isInterrupted()){
                if(unprocessedMessages.size() > 0){
                    AbstractMap.SimpleEntry<Integer, String> currentMessage = unprocessedMessages.get(0);
//                    System.out.println("Reexecuting message: " + currentMessage.getValue());
                    boolean completed = decoder.decodeMessage(currentMessage.getValue(), GreenfootNetworkManager.getInstance().getClient());
                    synchronized (unprocessedMessages){
                        if (!completed && currentMessage.getKey() < 10) {
                            unprocessedMessages.add(new AbstractMap.SimpleEntry<>(currentMessage.getKey() + 1, currentMessage.getValue()));
                        }
                        unprocessedMessages.remove(0);
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public boolean checkForMessage(String message){
            synchronized (unprocessedMessages){
                for(AbstractMap.SimpleEntry<Integer, String> e : unprocessedMessages){
                    if(Objects.equals(e.getValue(), message)){
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
