package GreenfootNetworking;

import Client.Client;
import Enums.NetworkingOptions;
import Enums.Parameters;
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

    public void addNetworkObject(NetworkedActor networkedActor, int x, int y) {
        if(!GreenfootNetworkManager.isClient()) return;
        networkedActor.setStartX(x);
        networkedActor.setStartY(y);
        networkedActor.setWorldId(this.worldId);
        super.addObject(networkedActor, x, y);
        GreenfootNetworkManager.getInstance().getClient().getActorHandler().scheduleAddActor(networkedActor.getId());
    }

    public void removeNetworkObject(NetworkedActor networkedActor){
        if(!GreenfootNetworkManager.isClient()) return;
        GreenfootNetworkManager.getInstance().getClient().getActorHandler().scheduleRemoveActor(networkedActor.getId());
    }

    public String toJsonString(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Parameters.WorldWidth.name(), this.getWidth());
        jsonObject.put(Parameters.WorldHeight.name(), this.getHeight());
        jsonObject.put(Parameters.CellSize.name(), this.getCellSize());
        jsonObject.put(Parameters.WorldId.name(), this.worldId);
        return jsonObject.toJSONString();
    }

    public int getWorldId(){
        return this.worldId;
    }
}
