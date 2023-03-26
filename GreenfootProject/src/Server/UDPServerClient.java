package Server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServerClient {

    private final InetAddress address;
    private final int port;

    private final DatagramSocket socket;
    protected UDPServerClient(InetAddress address, int port, DatagramSocket socket){
        this.address = address;
        this.port = port;
        this.socket = socket;
    }

    protected void sendMessage(String message){
        try {
            socket.send(new DatagramPacket(message.getBytes(), message.getBytes().length, address, port));
        }catch (Exception ignored){}
    }
}
