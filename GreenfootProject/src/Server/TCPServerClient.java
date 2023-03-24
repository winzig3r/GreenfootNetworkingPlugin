package Server;

import java.net.*;
import java.io.*;

public class TCPServerClient extends Thread {
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;

    public TCPServerClient(Socket socket) {
        System.out.println("TCPClient connected");
        this.clientSocket = socket;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Message received over TCP: " + inputLine);
                if (".".equals(inputLine)) {
                    sendMessage("bye");
                    break;
                }
                sendMessage(inputLine);
            }
        } catch (IOException ignored) {}
    }

    public void sendMessage(String message){
        out.println(message);
    }

    public void stopSelf() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
