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
                int newId = Server.getNewClientId();
                TCPServerClient newConnection = new TCPServerClient(newId, serverSocket.accept());
                Server.addNewClient(new ServerClient(newId, newConnection));
                MessageEncoder.getInstance().sendHandshakeTCP(newId);
                newConnection.start();
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
