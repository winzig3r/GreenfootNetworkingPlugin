package Server;

import java.net.*;
import java.io.*;

public class TCPServer extends Thread{
    private ServerSocket serverSocket;

    private final int PORT;

    public TCPServer(int port){
        this.PORT = port;
        this.start();
    }
    @Override
    public void run() {
        System.out.println("Server listening on port: " + PORT);
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket newConnection = serverSocket.accept();
                int newId = Server.getNewClientId();
                System.out.println("New connection! Assigned id: " + newId);
                TCPServerClient newTCPClient = new TCPServerClient(newId, newConnection);
                Server.addNewClient(new ServerClient(newId, newTCPClient));
                MessageEncoder.getInstance().sendHandshakeTCP(newId);
                newTCPClient.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopServer() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
