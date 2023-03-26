package Server;

import Global.Actions;
import Global.Parameters;
import org.json.simple.*;

import java.net.DatagramSocket;
import java.net.InetAddress;

class MessageDecoder {
    private static MessageDecoder instance;

    protected static MessageDecoder getInstance(){
        if(instance == null){
            instance = new MessageDecoder();
        }
        return instance;
    }

    protected void decodeUDPMessage(String message, InetAddress address, int port, DatagramSocket socket){
        System.out.println("Received message: " + message);
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(message);
        Actions action = Actions.valueOf((String)jsonMessage.get(Parameters.Action.name()));
        if(action.equals(Actions.HANDSHAKE)){
            int clientId = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
            Server.getClient(clientId).recogniseUDPConnection(new UDPServerClient(address, port, socket));
            MessageEncoder.getInstance().welcomeNewClientUDP(clientId);
        }else{
            decodeMessage(message);
        }
    }

    protected void decodeMessage(String message){
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(message);
        Actions action = Actions.valueOf((String)jsonMessage.get(Parameters.Action.name()));
        //TODO: Add "Action not known"
    }
}
