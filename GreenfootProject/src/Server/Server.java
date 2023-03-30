package Server;

import Enums.Parameters;
import GreenfootNetworking.NetworkedActor;
import GreenfootNetworking.NetworkedWorld;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.Collections;
import java.util.HashMap;

public class Server {

    private static final UDPServer udpServer = new UDPServer(6968);
    private static final TCPServer tcpServer = new TCPServer(6969);

    private static final HashMap<Integer, ServerClient> serverClients = new HashMap<>();
    private static final HashMap<Integer, NetworkedActor> networkedActors = new HashMap<>();
    private static final HashMap<Integer, NetworkedWorld> networkedWorlds = new HashMap<>();
    protected static void addNewClient(ServerClient client){serverClients.put(client.getId(), client);}
    protected static ServerClient getClient(int id){return serverClients.get(id);}

    protected static void addActorToWorld(int actorId, int worldId){
        networkedActors.get(actorId).setWorldId(worldId);
    }
    protected static void createNewActor(String newActorInfo){
        NetworkedActor networkedActor = new NetworkedActor(newActorInfo);
        networkedActors.put(networkedActor.getId(), networkedActor);
    }
    protected static NetworkedActor getActor(int actorId){return networkedActors.get(actorId);}
    protected static void removeActor(int actorId){networkedActors.remove(actorId);}

    protected static String addNewWorld(String worldDataRaw){
        JSONObject worldData = (JSONObject) JSONValue.parse(worldDataRaw);
        int worldWidth = ((Long) worldData.get(Parameters.WorldWidth.name())).intValue();
        int worldHeight = ((Long) worldData.get(Parameters.WorldHeight.name())).intValue();
        int cellSize = ((Long) worldData.get(Parameters.CellSize.name())).intValue();
        int newWorldId = ((Long) worldData.get(Parameters.WorldId.name())).intValue();
        boolean bounded = (boolean) worldData.getOrDefault(Parameters.Bounded.name(), true);
        NetworkedWorld networkedWorld = new NetworkedWorld(worldWidth, worldHeight, cellSize, bounded, newWorldId);
        networkedWorlds.put(newWorldId, networkedWorld);
        return networkedWorld.toJsonString();
    }

    protected NetworkedWorld getWorld(int worldId){return networkedWorlds.get(worldId);}

    protected static int getNewClientId(){
        if(serverClients.size() == 0) return 0;
        return Collections.max(serverClients.keySet()) + 1;
    }

    protected static void broadcastTCP(String message){
        for(ServerClient client : serverClients.values()){
            client.sendTCP(message);
        }
    }

    protected static void broadcastUDP(String message){
        for(ServerClient client : serverClients.values()){
            client.sendTCP(message);
        }
    }

    protected static void informTCP(String message, int[] exclude){
        for(ServerClient client : serverClients.values()){
            if(isInArr(exclude, client.getId())) continue;
            client.sendTCP(message);
        }
    }

    protected static void informUDP(String message, int[] exclude){
        for(ServerClient client : serverClients.values()){
            if(isInArr(exclude, client.getId())) continue;
            client.sendUDP(message);
        }
    }

    protected static void sendToClientTCP(String message, int clientId){
        serverClients.get(clientId).sendTCP(message);
    }

    protected static void sendToClientUDP(String message, int clientId){
        serverClients.get(clientId).sendUDP(message);
    }

    private static boolean isInArr(int[] arr, int x){
        for(int a : arr){
            if(a == x) return true;
        }
        return false;
    }

    public static void main(String[] args) {
    }
}
