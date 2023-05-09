package de.webspired.Server;

import java.net.*;
import java.io.*;

public class TCPServer extends Thread{
    private ServerSocket serverSocket;

    private final int PORT;
    private boolean running;

    public TCPServer(int port){
        this.PORT = port;
        this.running = true;
        this.start();
    }
    @Override
    public void run() {
        if(!this.running) return;
        try {
            serverSocket = new ServerSocket(PORT);
            while (this.running) {
                Socket newConnection = serverSocket.accept();
                int newId = Server.getNewClientId();
                TCPServerClient newTCPClient = new TCPServerClient(newId, newConnection);
                Server.addNewClient(new ServerClient(newId, newTCPClient));
                MessageEncoder.getInstance().sendHandshakeTCP(newId);
                newTCPClient.start();
            }
        } catch (IOException e) {
            this.running = false;
        }
    }

    public void stopServer() {
        this.interrupt();
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
