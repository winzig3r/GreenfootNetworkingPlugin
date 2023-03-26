package Server;

import java.io.IOException;
import java.net.*;

public class UDPServer extends Thread{

    private final int PORT;
    private final DatagramSocket socket;
    private final byte[] buf = new byte[256];

    public UDPServer(int port) {
        this.PORT = port;
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        this.start();
    }

    @Override
    public void run() {
        System.out.println("UDP Server listening on port: " + PORT);
        while (true) {
            receiveMessage();
        }
    }

    public void receiveMessage(){
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        MessageDecoder.getInstance().decodeUDPMessage(new String(packet.getData(), 0, packet.getLength()), address, port, socket);
    }

    public void stopUDP(){
        this.interrupt();
        socket.close();
    }
}
