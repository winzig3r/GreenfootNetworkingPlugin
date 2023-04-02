package GreenfootNetworking;

import Enums.Parameters;
import greenfoot.Actor;
import Client.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.concurrent.TimeUnit;

public class NetworkedActor extends Actor {

    private int id;
    private int worldId = -1;
    private String imagePath = "";

    public NetworkedActor(String fromJson){
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(fromJson);
        this.id = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
        this.worldId = ((Long) jsonMessage.get(Parameters.WorldId.name())).intValue();
        this.imagePath = (String) jsonMessage.get(Parameters.NewImageFilePath.name());
        this.setImage(this.imagePath);
        if(this.worldId != -1){
            int x = ((Long) jsonMessage.get(Parameters.NewXPosition.name())).intValue();
            int y = ((Long) jsonMessage.get(Parameters.NewYPosition.name())).intValue();
            GreenfootNetworkManager.getInstance().getClient().getNetworkedWorld(worldId).addObject(this, x, y);
        }
    }

    public NetworkedActor() {
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        while (!myClient.hasReceivedId()){
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        myClient.createRealActor(this);
    }

    public void moveSynced(int distance) {
        super.move(distance);
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendPositionUpdateUDP(myClient, this);
    }
    public void setLocationSynced(int x, int y) {
        super.setLocation(x, y);
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendPositionUpdateUDP(myClient, this);
    }

    public void turnSynced(int amount) {
        super.turn(amount);
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendRotationUpdateUDP(myClient, this.id);
    }

    public void turnTowardsSynced(int x, int y) {
        super.turnTowards(x, y);
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendRotationUpdateUDP(myClient, this.id);
    }

    public void setRotationSynced(int rotation) {
        super.setRotation(rotation);
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendRotationUpdateUDP(myClient, this.id);
    }

    @Override
    public void setImage(String filename) throws IllegalArgumentException {
        if(filename.isEmpty()) return;
        this.imagePath = filename;
        super.setImage(filename);
    }

    public void setImageSynced(String filename) throws IllegalArgumentException {
        this.setImage(filename);
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendImageUpdateTCP(myClient, this.id, filename);
    }

    public NetworkedWorld getNetworkedWorld(){
        return GreenfootNetworkManager.getInstance().getClient().getNetworkedWorld(this.getWorldId());
    }

    public String toJsonString(){
        JSONObject json = new JSONObject();
        json.put(Parameters.ActorId.name(), this.id);
        json.put(Parameters.WorldId.name(), this.worldId);
        json.put(Parameters.NewImageFilePath.name(), this.imagePath);
        if(this.worldId != -1){
            json.put(Parameters.NewXPosition.name(), this.getX());
            json.put(Parameters.NewYPosition.name(), this.getY());
        }
        return json.toJSONString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorldId(){return worldId;}

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }

    public String getImagePath() {
        return imagePath;
    }
}
