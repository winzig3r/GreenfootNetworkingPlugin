package Client;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {
    private final DatagramSocket socket;
    private final InetAddress address;
    private final Client self;
    private final int PORT;

    private final byte[] receiveBuffer = new byte[2048];

    public UDPClient(String ip, int port, Client self) {
        this.PORT = port;
        this.self = self;
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
        //System.out.println("UDP Client was started");
        while (true){
            MessageDecoder.getInstance().decodeMessage(receiveMessage(), self);
        }
    }

    public void sendMessage(String message){
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String receiveMessage(){
        DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        try {
            socket.receive(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(packet.getData(), 0, packet.getLength());
    }

    public void close() {
        socket.close();
    }
}
