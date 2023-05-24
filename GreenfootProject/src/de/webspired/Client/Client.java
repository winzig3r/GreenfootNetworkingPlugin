package de.webspired.Client;


import de.webspired.GreenfootNetworking.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Client {

    public MessageEncoder messageEncoder;

    private final HashMap<Integer, NetworkedActor> networkedActors = new HashMap<>();
    private final HashMap<Integer, NetworkedActor> lateNetworkedActors = new HashMap<>();
    private final HashMap<Integer, NetworkedWorld> networkedWorlds = new HashMap<>();
    private final TCPClient tcpClient;
    private final UDPClient udpClient;
    private final ActorHandler actorHandler;
    private int id;
    private int createdActors = 0;
    private int loadedWorlds = 0;
    private boolean hasReceivedId = false;

    public Client(String ip, int tcpPort, int udpPort) {
        tcpClient = new TCPClient(ip, tcpPort, this);
        udpClient = new UDPClient(ip, udpPort, this);
        messageEncoder = MessageEncoder.getInstance();
        actorHandler = new ActorHandler(this);
    }

    protected void sendTCPMessage(String msg) {
        tcpClient.sendMessage(msg);
    }

    protected void sendUDPMessage(String msg) {
        udpClient.sendMessage(msg);
    }

    public boolean hasReceivedId() {
        return hasReceivedId;
    }

    /**
     * Sets the id to the newId and sets hasReceivedId to true @see
     * @param newId The new id of the client
     */
    protected void setId(int newId) {
        hasReceivedId = true;
        this.id = newId;
    }

    public int getId() {
        return id;
    }

    public void updateWorldAfterJoin(NetworkedWorld world){
        ArrayList<Integer> removableLateActors = new ArrayList<>();
        if(loadedWorlds > 0){
            MessageEncoder.getInstance().sendResetWorldTCP(this);
            removeClientActors(this.getId());
        }
        if(networkedWorlds.containsKey(world.getWorldId())){
            MessageEncoder.getInstance().requestOtherActorsTCP(this);
        }
        for(Map.Entry<Integer, NetworkedActor> lateActor : lateNetworkedActors.entrySet()){
            if(lateActor.getValue().getWorldId() == world.getWorldId()){
                world.addObject(lateActor.getValue(), lateActor.getValue().getStartX(), lateActor.getValue().getStartY());
                removableLateActors.add(lateActor.getValue().getId());
            }
        }
        for(int removableLateActor : removableLateActors){
            lateNetworkedActors.remove(removableLateActor);
        }
        loadedWorlds += 1;
    }

    public void addGhostWorld(NetworkedWorld world) {
        updateWorldAfterJoin(world);
        networkedWorlds.put(world.getWorldId(), world);
        messageEncoder.sendAddWorldTCP(this, world);
    }

    public void addRealWorld(NetworkedWorld world) {
        updateWorldAfterJoin(world);
        networkedWorlds.put(world.getWorldId(), world);
        messageEncoder.sendAddWorldTCP(this, world);
    }


    protected NetworkedActor getActor(int actorId) {
        return networkedActors.get(actorId);
    }

    public NetworkedWorld getNetworkedWorld(int worldId) {
        return networkedWorlds.get(worldId);
    }

    protected void createGhostActor(NetworkedActor actor) {
        networkedActors.put(actor.getId(), actor);
    }

    public void createRealActor(NetworkedActor actor) {
        createdActors++;
        if(networkedActors.containsKey(createdActors)) {
            createRealActor(actor);
            return;
        }
        actor.setId(createdActors);
        networkedActors.put(actor.getId(), actor);
        this.messageEncoder.sendCreateActorTCP(this, actor);
    }
    public void repaintWorlds() {
        for(Map.Entry<Integer, NetworkedWorld> nw : networkedWorlds.entrySet()){
            nw.getValue().repaint();
        }
    }

    public void changeConnection(String ip, int tcpPort, int udpPort) {
        tcpClient.changeConnection(ip, tcpPort);
        udpClient.changeConnection(ip, udpPort);
    }

    public void makeActorAddable(int oldActorId, int newActorId) {
        if (oldActorId != newActorId){
            if(newActorId > createdActors){
                createdActors++;
            }
            networkedActors.get(oldActorId).setId(newActorId);
            networkedActors.put(newActorId, networkedActors.get(oldActorId));
            networkedActors.remove(oldActorId);
        }
        networkedActors.get(newActorId).setAddableToWorld(true);
    }

    public void informToAdd(NetworkedActor addable) {
        MessageEncoder.getInstance().sendAddActorToWorldTCP(this, addable);
    }

    public void informToRemove(NetworkedActor removable){
        MessageEncoder.getInstance().sendRemoveActorFromWorldTCP(this, removable);
        networkedWorlds.get(removable.getWorldId()).removeObject(removable);
        removable.setWorldId(-1);
    }

    public void receiveToAdd(int actorId, int worldId, int startX, int startY){
        if(!this.networkedWorlds.containsKey(worldId)){
            networkedActors.get(actorId).setWorldId(worldId);
            networkedActors.get(actorId).setStartX(startX);
            networkedActors.get(actorId).setStartY(startY);
            lateNetworkedActors.put(actorId, networkedActors.get(actorId));
            return;
        }
        networkedActors.get(actorId).setWorldId(worldId);
        networkedActors.get(actorId).setStartX(startX);
        networkedActors.get(actorId).setStartY(startY);
        networkedWorlds.get(worldId).addObject(networkedActors.get(actorId), startX, startY);
    }

    public void receiveToRemove(int actorId, int worldId) {
        lateNetworkedActors.remove(actorId);
        if(networkedActors.get(actorId).getWorldId() == -1) return;
        networkedWorlds.get(worldId).removeObject(networkedActors.get(actorId));
        networkedActors.get(actorId).setWorldId(-1);
    }

    public ActorHandler getActorHandler(){
        return this.actorHandler;
    }

    public void removeClientActors(int fromClient) {
        ArrayList<Integer> actorsToRemove = new ArrayList<>();
        for (Map.Entry<Integer, NetworkedActor> networkedActorEntry : networkedActors.entrySet()) {
            if (networkedActorEntry.getValue().getCreatorClient() != fromClient) continue;
            receiveToRemove(networkedActorEntry.getValue().getId(), networkedActorEntry.getValue().getWorldId());
            actorsToRemove.add(networkedActorEntry.getKey());
        }
        for (int actorToRemove : actorsToRemove) {
            networkedActors.remove(actorToRemove);
        }
    }
}
