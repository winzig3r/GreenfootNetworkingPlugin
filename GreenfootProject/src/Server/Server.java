package Server;

import java.util.Collections;
import java.util.HashMap;

public class Server {

    private static final UDPServer udpServer = new UDPServer(6968);
    private static final TCPServer tcpServer = new TCPServer(6969);

    private static final HashMap<Integer, ServerClient> serverClients = new HashMap<>();

    protected static void addNewClient(ServerClient client){serverClients.put(client.getId(), client);}
    protected static ServerClient getClient(int id){return serverClients.get(id);}

    protected static int getNewClientId(){
        if(serverClients.size() == 0) return 0;
        return Collections.max(serverClients.keySet()) + 1;
    }

    public static void main(String[] args) {
        Server s = new Server();
    }
}
