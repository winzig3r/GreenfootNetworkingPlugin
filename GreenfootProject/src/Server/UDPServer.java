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
    }

    @Override
    public void run() {
        System.out.println("UDP Server listening on port: " + PORT);
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Message received over UDP: " + received);
            if (received.equals("end")) {
                boolean running = false;
                continue;
            }
            try {
                socket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopUDP(){
        this.interrupt();
        socket.close();
    }
}
