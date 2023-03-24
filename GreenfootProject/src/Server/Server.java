package Server;

public class Server {
    public static void main(String[] args) {
        UDPServer udpServer = new UDPServer(6968);
        TCPServer tcpServer = new TCPServer(6969);
        udpServer.start();
        tcpServer.start();
    }
}
