package Client;

import java.net.*;
import java.io.*;

public class TCPClient extends Thread {
    private Socket clientSocket;
    private final Client self;
    private PrintWriter out;
    private BufferedReader in;

    public TCPClient(String ip, int port, Client self) {
        this.self = self;
        startConnection(ip, port);
        this.start();
    }

    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println("TCP Client was started");
        while (true) {
            MessageDecoder.getInstance().decodeMessage(receiveMessage(), self);
        }
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public String receiveMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
