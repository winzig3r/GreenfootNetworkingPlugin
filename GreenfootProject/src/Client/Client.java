package Client;

public class Client {

    TCPClient tcpClient;
    UDPClient udpClient;

    public Client(String ip, int tcpPort, int udpPort){
        tcpClient = new TCPClient(ip, tcpPort);
        udpClient = new UDPClient(ip, udpPort);
    }

    public void sendTCPMessage(String msg){
        tcpClient.sendMessage(msg);
    }

    public void sendUDPMessage(String msg){
        udpClient.sendMessage(msg);
    }


    public static void main(String[] args) {
        Client c = new Client("192.168.178.139", 6969, 6968);
        c.sendTCPMessage("Hello from TCP");
        c.sendUDPMessage("Hello from UDP");
    }
}
