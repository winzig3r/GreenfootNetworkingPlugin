package Client;


import GreenfootNetworking.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    public boolean isWorldExisting(int worldId) {
        return networkedWorlds.containsKey(worldId);
    }

    protected void setId(int newId) {
        hasReceivedId = true;
        this.id = newId;
    }

    protected int getId() {
        return id;
    }

    public void addGhostWorld(NetworkedWorld world) {
        networkedWorlds.put(world.getWorldId(), world);
        if(networkedWorlds.containsKey(world.getWorldId())){
            //The reset button was pressed on a client. All Actors get removed from the world so they have to be added back by code
            for(Map.Entry<Integer, NetworkedActor> n : networkedActors.entrySet()){
                if(n.getValue().getWorldId() == world.getWorldId()){
                    //We found an actor that got removed by greenfoot so lets add it back
                    addActorToWorld(n.getValue().getId(), world.getWorldId(), n.getValue().getX(), n.getValue().getY(), n.getValue().getImagePath());
                }
            }
        }
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

    protected void removeActorFromWorld(int actorId, int worldId) {
        networkedWorlds.get(worldId).removeObject(networkedActors.get(actorId));
        networkedActors.get(actorId).setWorldId(-1);
    }

    public void updateActorId(int oldActorId, int newActorId) {
        if (oldActorId == newActorId) return;
        networkedActors.put(newActorId, networkedActors.get(oldActorId));
        networkedActors.get(oldActorId).setId(newActorId);
        networkedActors.remove(oldActorId);
    }
}
