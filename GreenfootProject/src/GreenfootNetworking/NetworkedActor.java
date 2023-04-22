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
    private int startX = 0;
    private int startY = 0;
    private int creatorClient;
    private boolean addableToWorld;
    private String imagePath = "";
    private String baseClass;

    public NetworkedActor(JSONObject fromJson){
        JSONObject jsonMessage =(JSONObject) JSONValue.parse(fromJson.toJSONString());
        this.id = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
        this.worldId = ((Long) jsonMessage.get(Parameters.WorldId.name())).intValue();
        this.imagePath = (String) jsonMessage.get(Parameters.NewImageFilePath.name());
        this.creatorClient = ((Long) jsonMessage.get(Parameters.ClientId.name())).intValue();
        this.baseClass = (String) jsonMessage.get(Parameters.BaseClass.name());
        this.setImage(this.imagePath);

        if(this.worldId != -1){
            int x = ((Long) jsonMessage.get(Parameters.NewXPosition.name())).intValue();
            int y = ((Long) jsonMessage.get(Parameters.NewYPosition.name())).intValue();
            GreenfootNetworkManager.getInstance().getClient().getNetworkedWorld(worldId).addObject(this, x, y);
            this.setRotation(((Long) jsonMessage.get(Parameters.NewRotation.name())).intValue());
        }
    }

    public NetworkedActor() {
        if(!GreenfootNetworkManager.isClient()) return;
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        while (!myClient.hasReceivedId()){
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        baseClass = this.getClass().getName();
        creatorClient = myClient.getId();
        myClient.createRealActor(this);
    }

    public void moveSynced(int distance) {
        super.move(distance);
        if(!GreenfootNetworkManager.isClient()) return;
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendPositionUpdateUDP(myClient, this);
    }
    public void setLocationSynced(int x, int y) {
        super.setLocation(x, y);
        if(!GreenfootNetworkManager.isClient()) return;
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendPositionUpdateUDP(myClient, this);
    }

    public void turnSynced(int amount) {
        super.turn(amount);
        if(!GreenfootNetworkManager.isClient()) return;
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendRotationUpdateUDP(myClient, this.id);
    }

    public void turnTowardsSynced(int x, int y) {
        super.turnTowards(x, y);
        if(!GreenfootNetworkManager.isClient()) return;
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendRotationUpdateUDP(myClient, this.id);
    }

    public void setRotationSynced(int rotation) {
        super.setRotation(rotation);
        if(!GreenfootNetworkManager.isClient()) return;
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
        if(!GreenfootNetworkManager.isClient()) return;
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
        json.put(Parameters.ClientId.name(), this.creatorClient);
        json.put(Parameters.BaseClass.name(), baseClass);
        if(this.worldId != -1){
            json.put(Parameters.NewXPosition.name(), this.getX());
            json.put(Parameters.NewYPosition.name(), this.getY());
            json.put(Parameters.NewRotation.name(), this.getRotation());
        }
        return json.toJSONString();
    }

    public boolean isOwnActor(){
        return creatorClient == GreenfootNetworkManager.getInstance().getClient().getId();
    }

    public int getCreatorClient(){
        return creatorClient;
    }

    public String getBaseClass() {
        return baseClass;
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
    public void setAddableToWorld(boolean addableToWorld) {
        this.addableToWorld = addableToWorld;
    }
    public boolean isAddableToWorld() {
        return addableToWorld;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }
}
