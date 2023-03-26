package Server;

public class Server {

    UDPServer udpServer;
    TCPServer tcpServer;

    public Server(){
        UDPServer udpServer = new UDPServer(6968);
        TCPServer tcpServer = new TCPServer(6969);
    }
    public static void main(String[] args) {
        Server s = new Server();
    }
}
