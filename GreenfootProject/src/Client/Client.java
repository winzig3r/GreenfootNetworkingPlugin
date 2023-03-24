package Client;

public class Client {
    public static void main(String[] args) {
        TCPClient client = new TCPClient("192.168.178.139", 6969);
        TCPClient client1 = new TCPClient("192.168.178.139", 6969);
        UDPClient udpClient = new UDPClient("192.168.178.139", 6968);
        udpClient.start();
        client.sendMessage("hello server from client");
        client1.sendMessage("hello server from client1");
        udpClient.sendMessage("hello from udp");
    }
}
