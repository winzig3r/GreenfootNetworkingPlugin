package GreenfootNetworking;

import Client.Client;
import Enums.NetworkingOptions;
import Enums.Parameters;
import Server.Server;
import greenfoot.World;
import org.json.simple.JSONObject;

public class NetworkedWorld extends World {

    //HAS TO BE THE SAME ACROSS ALL CLIENTS!!!
    private final int worldId;
    public NetworkedWorld(int worldWidth, int worldHeight, int cellSize, NetworkingOptions options, String ip, int worldId) {
        super(worldWidth, worldHeight, cellSize);
        this.worldId = worldId;
        GreenfootNetworkManager greenfootNetworkManager = new GreenfootNetworkManager(options, ip);
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        if(GreenfootNetworkManager.isServer()){
            myClient.addRealWorld(this);
        }else{
            myClient.addGhostWorld(this);
        }
    }

    public NetworkedWorld(int worldWidth, int worldHeight, int cellSize, boolean bounded, int worldId) {
        super(worldWidth, worldHeight, cellSize, bounded);
        this.worldId = worldId;
    }

    public void addNetworkObject(NetworkedActor networkedActor, int x, int y){
        if(!GreenfootNetworkManager.isClient()) return;
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendAddActorTCP(myClient, networkedActor.getId(), this.worldId, x, y, networkedActor.getImagePath());
        //Information gets Broadcasted by the server to everyone including the client sending the message => There is no need to call super.addActor();
        //Because it is called after the message was received in the Client.addActor() method
    }

    public void removeNetworkObject(NetworkedActor networkedActor){
        if(!GreenfootNetworkManager.isClient()) return;
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendRemoveActorTCP(myClient, networkedActor.getId(), this.getWorldId(), false);
        //Information gets Broadcasted by the server to everyone including the client sending the message => There is no need to call super.removeActor();
        //Because it is called after the message was received in the Client.removeActor() method
    }

    public String toJsonString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Parameters.WorldWidth.name(), this.getWidth());
        jsonObject.put(Parameters.WorldHeight.name(), this.getHeight());
        jsonObject.put(Parameters.CellSize.name(), this.getCellSize());
        jsonObject.put(Parameters.WorldId.name(), this.worldId);
        //TODO: Add bounded parameter
        return jsonObject.toJSONString();
    }

    public int getWorldId(){
        return this.worldId;
    }
}
