package de.webspired.Server;


public class ServerClient {

    private final TCPServerClient tcpConnection;
    private UDPServerClient udpConnection;

    //Other class fields
    private final int id;
    public ServerClient(int id, TCPServerClient tcpConnection){
        this.id = id;
        this.tcpConnection = tcpConnection;
    }

    public void recogniseUDPConnection(UDPServerClient udpConnection){
        this.udpConnection = udpConnection;
    }

    public void sendTCP(String message){
        tcpConnection.sendMessage(message);
    }

    public void sendUDP(String message){
        udpConnection.sendMessage(message);
    }

    protected int getId(){
        return this.id;
    }
}
