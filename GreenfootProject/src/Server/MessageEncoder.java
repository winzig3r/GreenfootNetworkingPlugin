package Server;

import Enums.Actions;
import Enums.Parameters;
import org.json.simple.JSONObject;

class MessageEncoder {

    private static MessageEncoder instance;

    protected static MessageEncoder getInstance(){
        if(instance == null){
            instance = new MessageEncoder();
        }
        return instance;
    }

    protected void sendHandshakeTCP(int clientId){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.HANDSHAKE.name());
        message.put(Parameters.ClientId.name(), clientId);
        Server.getClient(clientId).sendTCP(message.toJSONString());
    }

    protected void welcomeNewClientUDP(int clientId){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), "WelcomeClient");
        message.put("Message", "Hey client whats up");
        Server.getClient(clientId).sendUDP(message.toJSONString());
    }


}
