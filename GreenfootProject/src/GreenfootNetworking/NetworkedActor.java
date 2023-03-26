package GreenfootNetworking;

import Enums.ActorSyncOptions;
import greenfoot.Actor;
import greenfoot.GreenfootImage;

public class NetworkedActor extends Actor {

    private boolean syncPosition = false;
    private boolean syncRotation = false;
    private boolean syncImage = false;

    @Override
    public void act() {
        super.act();
        //TODO: "Somehow let the other clients know what exactly changed"
    }

    public void synchronize(ActorSyncOptions[] sync) {
        for (ActorSyncOptions syncOption : sync) {
            switch (syncOption) {
                case IMAGE:
                    syncImage = true;
                    break;
                case POSITION:
                    syncPosition = true;
                    break;
                case ROTATION:
                    syncRotation = true;
                    break;
            }
        }
    }

    public void asynchronize(ActorSyncOptions[] sync) {
        for (ActorSyncOptions syncOption : sync) {
            switch (syncOption) {
                case IMAGE:
                    syncImage = false;
                    break;
                case POSITION:
                    syncPosition = false;
                    break;
                case ROTATION:
                    syncRotation = false;
                    break;
            }
        }
    }

    @Override
    public void move(int distance) {
        super.move(distance);
        if(syncPosition){

        }
    }

    @Override
    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        if(syncPosition){

        }
    }

    @Override
    public void turn(int amount) {
        super.turn(amount);
        if(syncRotation){

        }
    }

    @Override
    public void turnTowards(int x, int y) {
        super.turnTowards(x, y);
        if(syncRotation){

        }
    }

    @Override
    public void setRotation(int rotation) {
        super.setRotation(rotation);
        if(syncRotation){

        }
    }

    @Override
    public void setImage(String filename) throws IllegalArgumentException {
        super.setImage(filename);
        if(syncImage){

        }
    }

    @Override
    public void setImage(GreenfootImage image) {
        super.setImage(image);
        if(syncImage){

        }
    }
}
