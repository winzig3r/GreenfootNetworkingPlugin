package Server;

import java.net.*;
import java.io.*;

public class TCPServerClient extends Thread {
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;

    public TCPServerClient(int id, Socket socket) {
        this.clientSocket = socket;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        while (true){
            receiveMessage();
        }
    }

    private void receiveMessage(){
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                MessageDecoder.getInstance().decodeMessage(inputLine);
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
