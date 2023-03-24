package Server;

import java.net.InetAddress;

public class ServerClient {
    //TCP Information
    private final TCPServerClient tcpConnection;

    // UDP Information
    private InetAddress udpConnection;
    private int clientUdpPort;

    //Other class fields
    private final int id;
    ServerClient(int id, TCPServerClient tcpConnection){
        this.id = id;
        this.tcpConnection = tcpConnection;
    }

    public void recogniseUDPConnection(InetAddress udpConnection, int clientUdpPort){
        this.udpConnection = udpConnection;
        this.clientUdpPort = clientUdpPort;
    }

    public void sendTCP(String message){
        tcpConnection.sendMessage(message);
    }

    public void sendUDP(String message){

    }
}
