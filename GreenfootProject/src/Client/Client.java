package Client;


import GreenfootNetworking.*;

import java.util.Collections;
import java.util.HashMap;

public class Client {

    public MessageEncoder messageEncoder;

    private final HashMap<Integer, NetworkedActor> networkedActors = new HashMap<>();
    private final HashMap<Integer, NetworkedWorld> networkedWorlds = new HashMap<>();
    private final TCPClient tcpClient;
    private final UDPClient udpClient;
    private int id;
    private boolean hasReceivedId = false;

    public Client(String ip, int tcpPort, int udpPort) {
        tcpClient = new TCPClient(ip, tcpPort, this);
        udpClient = new UDPClient(ip, udpPort, this);
        messageEncoder = MessageEncoder.getInstance();
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

    protected void setId(int newId) {
        //System.out.println("Applied new clientid: " + newId);
        hasReceivedId = true;
        this.id = newId;
    }

    protected int getId() {
        return id;
    }

    protected NetworkedWorld getWorld(int worldId) {
        return networkedWorlds.get(worldId);
    }

    public void addGhostWorld(NetworkedWorld world) {
        networkedWorlds.put(world.getWorldId(), world);
    }

    public void addRealWorld(NetworkedWorld world) {
        networkedWorlds.put(world.getWorldId(), world);
        messageEncoder.sendAddWorldTCP(this, world);
    }

    protected NetworkedActor getActor(int actorId) {
        return networkedActors.get(actorId);
    }

    public NetworkedWorld getNetworkedWorld(int worldId) {
        return networkedWorlds.get(worldId);
    }

    protected void addActorToWorld(int actorId, int worldId, int startX, int startY, String imageFilePath) {
        //System.out.println("In Client.class Adding actor (" + actorId + " " + networkedActors.get(actorId) + ") to world (" + worldId + " " + networkedWorlds.get(worldId) + ")");
        networkedActors.get(actorId).setWorldId(worldId);
        networkedActors.get(actorId).setImage(imageFilePath);
        networkedWorlds.get(worldId).addObject(networkedActors.get(actorId), startX, startY);
        networkedWorlds.get(worldId).repaint();
    }

    protected void createGhostActor(NetworkedActor actor) {
        networkedActors.put(actor.getId(), actor);
    }

    public void createRealActor(NetworkedActor actor) {
        int newActorId = (networkedActors.size() == 0) ? 1 : Collections.max(networkedActors.keySet()) + 1;
        actor.setId(newActorId);
        networkedActors.put(actor.getId(), actor);
        this.messageEncoder.sendCreateActorTCP(this, actor);
    }

    protected void removeActor(int actorId, int worldId) {
        networkedActors.remove(actorId);
        networkedWorlds.get(worldId).removeObject(networkedActors.get(actorId));
    }

    public void updateActorId(int oldActorId, int newActorId) {
        if(oldActorId == newActorId) return;
        networkedActors.put(newActorId, networkedActors.get(oldActorId));
        networkedActors.get(oldActorId).setId(newActorId);
        networkedActors.remove(oldActorId);
    }
}
