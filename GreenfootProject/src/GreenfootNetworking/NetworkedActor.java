package GreenfootNetworking;

import Enums.Parameters;
import greenfoot.Actor;
import Client.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class NetworkedActor extends Actor {

    private int id;
    private int worldId;

    public NetworkedActor(String fromJson){
        JSONObject jsonMessage = (JSONObject) JSONValue.parse(fromJson);
        this.id = ((Long) jsonMessage.get(Parameters.ActorId.name())).intValue();
        //TODO: Maybe add Location and Rotation Information to the from Json (void Actor.toJson : String)
    }

    public NetworkedActor(int id){
       this.id = id;
    }

    public NetworkedActor(){
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendCreateActorTCP(myClient);
    }

    public void moveSynced(int distance) {
        super.move(distance);
        System.out.println("Sending movement packet");
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendPositionUpdateUDP(myClient, this.id);
    }
    public void setLocationSynced(int x, int y) {
        super.setLocation(x, y);
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendPositionUpdateUDP(myClient, this.id);
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

    public void setImageSynced(String filename) throws IllegalArgumentException {
        super.setImage(filename);
        Client myClient = GreenfootNetworkManager.getInstance().getClient();
        myClient.messageEncoder.sendImageUpdateTCP(myClient, this.id, filename);
    }

    public String toJsonString(){
        JSONObject json = new JSONObject();
        json.put(Parameters.ActorId.name(), this.id);
        return json.toJSONString();
    }

    public void act() {

    }

    public int getId() {
        return id;
    }
    public int getWorldId(){return worldId;}

    public void setWorldId(int worldId) {
        this.worldId = worldId;
    }
}
