package Client;

import Global.Actions;
import Global.Parameters;
import Server.Server;
import org.json.simple.JSONObject;

public class MessageEncoder {

    private static MessageEncoder instance;

    protected static MessageEncoder getInstance(){
        if(instance == null){
            instance = new MessageEncoder();
        }
        return instance;
    }

    protected void sendHandshakeUDP(Client self){
        JSONObject message = new JSONObject();
        message.put(Parameters.Action.name(), Actions.HANDSHAKE.name());
        message.put(Parameters.ClientId.name(), self.getId());
        self.sendUDPMessage(message.toJSONString());
    }


}
