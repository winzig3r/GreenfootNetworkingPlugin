package Client;

import Enums.Actions;
import Enums.Parameters;
import GreenfootNetworking.GreenfootNetworkManager;
import GreenfootNetworking.NetworkedActor;
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
     * @param self: The Client who received the message
     * @return Returns whether a message has been successfully processed or not
     */
    protected boolean decodeMessage(String message, Client self){
        //System.out.println("Received message on the clientside: " + message);
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(message);
        Actions action;
        try {
            action = Actions.valueOf((String)jsonMessage.get(Parameters.Action.name()));
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
                self.getActor(actorId).setLocation(newX, newY);
                self.getNetworkedWorld(self.getActor(actorId).getWorldId()).repaint();
            } else if (action.equals(Actions.UPDATE_ROTATION)) {
                int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
                int newRotation = ((Long)jsonMessage.get(Parameters.NewRotation.name())).intValue();
                self.getActor(actorId).setRotation(newRotation);
                self.getNetworkedWorld(self.getActor(actorId).getWorldId()).repaint();
            } else if (action.equals(Actions.UPDATE_IMAGE)) {
                int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
                String imageFilePath = (String) jsonMessage.get(Parameters.NewImageFilePath.name());
                self.getActor(actorId).setImage(imageFilePath);
                self.getNetworkedWorld(self.getActor(actorId).getWorldId()).repaint();
            } else if (action.equals(Actions.UPDATE_ACTOR_ID)) {
                int oldActorId = ((Long) jsonMessage.get(Parameters.OldActorId.name())).intValue();
                int newActorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
                self.updateActorId(oldActorId, newActorId);
            } else if (action.equals(Actions.REMOVE_ACTOR)) {
                int actorId = ((Long)jsonMessage.get(Parameters.ActorId.name())).intValue();
                int worldId = ((Long)jsonMessage.get(Parameters.WorldId.name())).intValue();
                self.removeActorFromWorld(actorId, worldId);
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
            } else if (action.equals(Actions.REQUEST_OTHER_ACTORS)) {
                JSONArray allActorData = (JSONArray) JSONValue.parse((String) jsonMessage.get(Parameters.AllCurrentActors.name()));
                for(Object actorData : allActorData.toArray()){
                    JSONObject currentActorData = (JSONObject) JSONValue.parse((String) actorData);
                    self.createGhostActor(new NetworkedActor(currentActorData.toJSONString()));
                }
            }
            return true;
        }catch (NullPointerException e){
            postExecutor.addNewMessage(message);
            return false;
        }catch (IllegalArgumentException e){
            return false;
        }
    }

    public PostExecutor getPostExecutor() {
        return postExecutor;
    }

    static class PostExecutor extends Thread{
        private final ArrayList<AbstractMap.SimpleEntry<Integer, String>> unprocessedMessages= new ArrayList<>();
        private final MessageDecoder decoder;
        private PostExecutor(MessageDecoder decoder){
            this.decoder = decoder;
        }

        public void addNewMessage(String message){
            this.unprocessedMessages.add(new AbstractMap.SimpleEntry<>(0, message));
        }
        @Override
        public void run() {
            super.run();
            while(true){
                if(unprocessedMessages.size() > 0){
                    AbstractMap.SimpleEntry<Integer, String> currentMessage = unprocessedMessages.get(0);
                    boolean completed = decoder.decodeMessage(currentMessage.getValue(), GreenfootNetworkManager.getInstance().getClient());
                    if (!completed && currentMessage.getKey() < 10) {
                        unprocessedMessages.add(new AbstractMap.SimpleEntry<>(currentMessage.getKey() + 1, currentMessage.getValue()));
                    } else {
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
    }
}
