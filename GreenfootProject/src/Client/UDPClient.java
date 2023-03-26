package Client;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {
    private final DatagramSocket socket;
    private final InetAddress address;
    private byte[] buf = new byte[256];

    private final int PORT;

    public UDPClient(String ip, int port) {
        this.PORT = port;
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(ip);
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.start();
    }
    @Override
    public void run(){
        System.out.println("UDP Client was started");
        while (true){
            receiveMessage();
        }
    }

    public void sendMessage(String message){
        buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void receiveMessage(){
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String received = new String(packet.getData(), 0, packet.getLength());
        System.out.println(received);
    }

    public void close() {
        socket.close();
    }
}
