package Client;

public class StartClient {
    public static void main(String[] args) {
        TCPClient client = new TCPClient("192.168.178.139", 6969);
        TCPClient client1 = new TCPClient("192.168.178.139", 6969);
        client.sendMessage("hello server from client");
        client1.sendMessage("hello server from client1");
    }
}
