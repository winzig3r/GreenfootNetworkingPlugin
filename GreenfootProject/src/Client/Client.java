package Client;


public class Client {

    private final TCPClient tcpClient;
    private final UDPClient udpClient;
    private int id;

    public Client(String ip, int tcpPort, int udpPort) {
        tcpClient = new TCPClient(ip, tcpPort, this);
        udpClient = new UDPClient(ip, udpPort, this);
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

    public static void main(String[] args) {
        Client c = new Client("192.168.178.139", 6969, 6968);
//        Client c2 = new Client("192.168.178.139", 6969, 6968);
    }
}
