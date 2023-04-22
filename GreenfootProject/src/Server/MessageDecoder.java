package Server;

import Enums.Actions;
import Enums.Parameters;
import org.json.simple.*;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Objects;

class MessageDecoder {
    private static MessageDecoder instance;
    private final PostExecutor postExecutor;

    protected static MessageDecoder getInstance() {
        if (instance == null) {
            instance = new MessageDecoder();
        }
        return instance;
    }

    protected MessageDecoder() {
        this.postExecutor = new MessageDecoder.PostExecutor(this);
        postExecutor.start();
    }

    protected void decodeMessage(String message, InetAddress address, int port, DatagramSocket socket) {
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(message);
        Actions action = Actions.valueOf((String) jsonMessage.get(Parameters.Action.name()));
        if (action.equals(Actions.HANDSHAKE)) {
            System.out.println("Received message on server (UDP): " + message);
            int clientId = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
            Server.getClient(clientId).recogniseUDPConnection(new UDPServerClient(address, port, socket));
        } else {
            decodeMessage(message);
        }
    }

    protected boolean decodeMessage(String message) {
        System.out.println("Received message on server (TCP): " + message);
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(message);
        Actions action;
        try {
            action = Actions.valueOf((String) jsonMessage.get(Parameters.Action.name()));
            if (action.equals(Actions.UPDATE_POSITION)) {
                int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
                int actorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
                int newX = ((Long) jsonMessage.get(Parameters.NewXPosition.name())).intValue();
                int newY = ((Long) jsonMessage.get(Parameters.NewYPosition.name())).intValue();
                Server.informUDP(message, new int[]{fromClient});
                Server.updateActorPosition(actorId, newX, newY);
            } else if (action.equals(Actions.UPDATE_ROTATION)) {
                int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
                int actorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
                int newRotation = ((Long) jsonMessage.get(Parameters.NewRotation.name())).intValue();
                Server.informUDP(message, new int[]{fromClient});
                Server.updateActorRotation(actorId, newRotation);
            } else if (action.equals(Actions.UPDATE_IMAGE)) {
                int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
                int actorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
                String newImagePath = (String) jsonMessage.get(Parameters.NewImageFilePath.name());
                Server.updateActorImage(actorId, newImagePath);
                Server.informUDP(message, new int[]{fromClient});
            } else if (action.equals(Actions.CREATE_ACTOR)) {
                JSONObject newActorInformation = (JSONObject) JSONValue.parse((String) jsonMessage.get(Parameters.NewActorInformation.name()));
                int newActorId = Server.getNewActorId();
                int oldActorId = ((Long) newActorInformation.get(Parameters.ActorId.name())).intValue();
                int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
                newActorInformation.put(Parameters.ActorId.name(), newActorId);
                Server.createNewActor(newActorInformation);
                MessageEncoder.getInstance().informCreateActorTCP(newActorInformation.toJSONString(), fromClient);
                MessageEncoder.getInstance().sendCreatedActorTCP(fromClient, oldActorId, newActorId);
            } else if (action.equals(Actions.ADD_ACTOR_TO_WORLD)) {
                int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
                int actorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
                int worldId = ((Long) jsonMessage.get(Parameters.WorldId.name())).intValue();
                int startX = ((Long) jsonMessage.get(Parameters.NewXPosition.name())).intValue();
                int startY = ((Long) jsonMessage.get(Parameters.NewYPosition.name())).intValue();
                Server.addActorToWorld(actorId, worldId, startX, startY);
                Server.informTCP(message, new int[]{fromClient});
            } else if (action.equals(Actions.REMOVE_ACTOR_FROM_WORLD)) {
                int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
                int actorId = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
                int worldId = ((Long) jsonMessage.get(Parameters.WorldId.name())).intValue();
                Server.removeActorFromWorld(actorId, worldId);
                Server.informTCP(message, new int[]{fromClient});
            } else if (action.equals(Actions.ADD_WORLD)) {
                Server.addNewWorld((String) jsonMessage.get(Parameters.NewWorldInformation.name()));
            } else if (action.equals(Actions.REQUEST_OTHER_ACTORS)) {
                int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
                MessageEncoder.getInstance().sendOtherActorsTCP(fromClient);
            } else if (action.equals(Actions.RESET)) {
                int fromClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
                Server.removeClientActors(fromClient);
                Server.informTCP(message, new int[]{fromClient});
            }
            return true;
        } catch (NullPointerException e) {
            postExecutor.addNewMessage(message);
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    static class PostExecutor extends Thread {
        private final ArrayList<AbstractMap.SimpleEntry<Integer, String>> unprocessedMessages = new ArrayList<>();
        private final MessageDecoder decoder;

        private PostExecutor(MessageDecoder decoder) {
            this.decoder = decoder;
        }

        public void addNewMessage(String message) {
            if (checkForMessage(message)) return;
            synchronized (unprocessedMessages){
                this.unprocessedMessages.add(new AbstractMap.SimpleEntry<>(0, message));
            }
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                if (unprocessedMessages.size() > 0) {
                    AbstractMap.SimpleEntry<Integer, String> currentMessage = unprocessedMessages.get(0);
                    boolean completed = decoder.decodeMessage(currentMessage.getValue());
                    synchronized (unprocessedMessages) {
                        if (!completed && currentMessage.getKey() < 10) {
                            unprocessedMessages.add(new AbstractMap.SimpleEntry<>(currentMessage.getKey() + 1, currentMessage.getValue()));
                        }
                        unprocessedMessages.remove(0);
                    }
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public boolean checkForMessage(String message) {
            synchronized (unprocessedMessages) {
                for (AbstractMap.SimpleEntry<Integer, String> e : unprocessedMessages) {
                    if (Objects.equals(e.getValue(), message)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}