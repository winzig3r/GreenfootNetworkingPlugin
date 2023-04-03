package Server;

import Enums.Parameters;
import GreenfootNetworking.NetworkedActor;
import GreenfootNetworking.NetworkedWorld;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static UDPServer udpServer;
    private static TCPServer tcpServer;

    private static final HashMap<Integer, ServerClient> serverClients = new HashMap<>();
    private static final HashMap<Integer, NetworkedActor> networkedActors = new HashMap<>();
    private static final HashMap<Integer, NetworkedWorld> networkedWorlds = new HashMap<>();

    public static void reset(){
        udpServer.stopUDP();
        tcpServer.stopServer();
        serverClients.clear();
        networkedActors.clear();
        networkedWorlds.clear();
        Server.tcpServer = null;
        Server.udpServer = null;
    }

    public static void start(){
        Server.udpServer = new UDPServer(6968);
        Server.tcpServer = new TCPServer(6969);
    }

    protected static void addNewClient(ServerClient client) {
        serverClients.put(client.getId(), client);
    }

    protected static ServerClient getClient(int id) {
        return serverClients.get(id);
    }

    protected static void addActorToWorld(int actorId, int worldId, int x, int y, String imageFile) {
        networkedActors.get(actorId).setWorldId(worldId);
        networkedActors.get(actorId).setImage(imageFile);
        networkedWorlds.get(worldId).addObject(networkedActors.get(actorId), x, y);
    }

    protected static void createNewActor(String newActorInfo) {
        NetworkedActor networkedActor = new NetworkedActor(newActorInfo);
        networkedActors.put(networkedActor.getId(), networkedActor);
    }

    protected static int getNewActorId() {
        return (networkedActors.size() == 0) ? 1 : Collections.max(networkedActors.keySet()) + 1;
    }

    protected static String getAllCurrentActors() {
        //"[{actorId: 1}, {actorId: 2}...]"
        JSONArray allActors = new JSONArray();
        for (Map.Entry<Integer, NetworkedActor> kv : networkedActors.entrySet()) {
            allActors.add(kv.getValue().toJsonString());
        }
        return allActors.toJSONString();
    }

    protected static void removeActorFromWorld(int actorId, int worldId, boolean removeCompletely) {
        if(networkedActors.get(actorId).getWorldId() == -1) return;
        networkedActors.get(actorId).setWorldId(-1);
        networkedWorlds.get(worldId).removeObject(networkedActors.get(actorId));
        if(removeCompletely) networkedActors.remove(actorId);
    }

    protected static void addNewWorld(String worldDataRaw) {
        JSONObject worldData = (JSONObject) JSONValue.parse(worldDataRaw);
        int newWorldId = ((Long) worldData.get(Parameters.WorldId.name())).intValue();
        if (!networkedWorlds.containsKey(newWorldId)) {
            int worldWidth = ((Long) worldData.get(Parameters.WorldWidth.name())).intValue();
            int worldHeight = ((Long) worldData.get(Parameters.WorldHeight.name())).intValue();
            int cellSize = ((Long) worldData.get(Parameters.CellSize.name())).intValue();
            boolean bounded = (boolean) worldData.getOrDefault(Parameters.Bounded.name(), true);
            NetworkedWorld networkedWorld = new NetworkedWorld(worldWidth, worldHeight, cellSize, bounded, newWorldId);
            networkedWorlds.put(newWorldId, networkedWorld);
        }
    }

    public static void addNewWorld(NetworkedWorld world){
        networkedWorlds.put(world.getWorldId(), world);
    }

    protected static int getNewClientId() {
        if (serverClients.size() == 0) return 0;
        return Collections.max(serverClients.keySet()) + 1;
    }

    protected static void broadcastTCP(String message) {
        for (ServerClient client : serverClients.values()) {
            client.sendTCP(message);
        }
    }

    protected static void broadcastUDP(String message) {
        for (ServerClient client : serverClients.values()) {
            client.sendTCP(message);
        }
    }

    protected static void informTCP(String message, int[] exclude) {
        for (ServerClient client : serverClients.values()) {
            if (isInArr(exclude, client.getId())) continue;
            client.sendTCP(message);
        }
    }

    protected static void informUDP(String message, int[] exclude) {
        for (ServerClient client : serverClients.values()) {
            if (isInArr(exclude, client.getId())) continue;
            client.sendUDP(message);
        }
    }

    protected static void sendToClientTCP(String message, int clientId) {
        serverClients.get(clientId).sendTCP(message);
    }

    protected static void sendToClientUDP(String message, int clientId) {
        serverClients.get(clientId).sendUDP(message);
    }

    private static boolean isInArr(int[] arr, int x) {
        for (int a : arr) {
            if (a == x) return true;
        }
        return false;
    }

    public static void updateActorImage(int actorId, String newImagePath) {
        networkedActors.get(actorId).setImage(newImagePath);
    }

    public static void updateActorRotation(int actorId, int newRotation) {
        networkedActors.get(actorId).setRotation(newRotation);
    }

    public static void updateActorPosition(int actorId, int newX, int newY) {
        networkedActors.get(actorId).setLocation(newX, newY);
    }
}
