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

    protected void setId(int newId) {
        this.id = newId;
    }

    protected int getId() {
        return id;
    }

    protected NetworkedWorld getWorld(int worldId) { return networkedWorlds.get(worldId); }

    protected void addGhostWorld(NetworkedWorld world){
        networkedWorlds.put(world.getWorldId(), world);
    }

    public void addRealWorld(NetworkedWorld world){
        world.setWorldId((networkedWorlds.size() == 0) ? 1 : Collections.max(networkedWorlds.keySet()) + 1);
        networkedWorlds.put(world.getWorldId(), world);
        messageEncoder.sendAddWorldTCP(this, world);
    }
    protected NetworkedActor getActor(int actorId) {
        return networkedActors.get(actorId);
    }
    protected void addActorToWorld(int actorId, int worldId, int startX, int startY) {
        System.out.println("In Client.class Adding actor ("+ actorId + " " + networkedActors.get(actorId) +") to world (" + worldId + " " + networkedWorlds.get(worldId) + ")");
        networkedWorlds.get(worldId).addObject(networkedActors.get(actorId), startX, startY);
    }

    protected void createGhostActor(NetworkedActor actor){
        networkedActors.put(actor.getId(), actor);
    }

    public void createRealActor(NetworkedActor actor){
        int newActorId = (networkedActors.size() == 0) ? 1 : Collections.max(networkedActors.keySet()) + 1;
        actor.setId(newActorId);
        networkedActors.put(actor.getId(), actor);
        this.messageEncoder.sendCreateActorTCP(this, actor);
    }

    protected void removeActor(int actorId, int worldId) {
        networkedActors.remove(actorId);
        networkedWorlds.get(worldId).removeObject(networkedActors.get(actorId));
    }

    public static void main(String[] args) {
        Client c = new Client("192.168.178.139", 6969, 6968);
//        Client c2 = new Client("192.168.178.139", 6969, 6968);
    }
}
