package Server;

import java.net.*;
import java.io.*;

public class Server {
    private ServerSocket serverSocket;

    public void start(int port) {
        System.out.println("Server listening on port: " + port);
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
