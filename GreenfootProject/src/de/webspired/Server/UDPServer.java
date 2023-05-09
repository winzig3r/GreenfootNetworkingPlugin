package de.webspired.Server;

import java.io.IOException;
import java.net.*;

public class UDPServer extends Thread{

    private final int PORT;
    private boolean running;
    private final DatagramSocket socket;
    private final byte[] buf = new byte[2048];

    public UDPServer(int port) {
        this.running = true;
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
        while (running) {
            receiveMessage();
        }
    }

    public void receiveMessage(){
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            running = false;
            return;
        }
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        MessageDecoder.getInstance().decodeMessage(new String(packet.getData(), 0, packet.getLength()), address, port, socket);
    }

    public void stopUDP(){
        running = false;
        socket.disconnect();
        socket.close();
    }
}
