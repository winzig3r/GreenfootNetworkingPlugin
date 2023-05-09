package de.webspired.Client;

import java.io.IOException;
import java.net.*;

public class UDPClient extends Thread {
    private DatagramSocket socket;
    private InetAddress address;
    private int PORT;
    private final Client self;

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

    public void changeConnection(String ip, int port){
        this.interrupt();
        this.PORT = port;
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(ip);
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.start();
    }
}
