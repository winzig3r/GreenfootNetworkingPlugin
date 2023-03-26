package Client;
import Global.Actions;
import Global.Parameters;
import org.json.simple.*;


public class MessageDecoder {
    private static MessageDecoder instance;

    protected static MessageDecoder getInstance(){
        if(instance == null){
            instance = new MessageDecoder();
        }
        return instance;
    }

    /**
     * @param message: The message received by self
     * @param self: The Client who received the message
     */
    protected void decodeMessage(String message, Client self){
        System.out.println(message);
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(message);
        Actions action = Actions.valueOf((String)jsonMessage.get(Parameters.Action.name()));
        if(action.equals(Actions.HANDSHAKE)){
            self.setId(((Long) jsonMessage.get(Parameters.ClientId.name())).intValue());
            MessageEncoder.getInstance().sendHandshakeUDP(self);
        }else{
            System.out.println("Action not known: " + jsonMessage);
        }
    }
}
