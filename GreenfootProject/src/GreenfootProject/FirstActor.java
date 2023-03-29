package GreenfootProject;

import GreenfootNetworking.NetworkedActor;

public class FirstActor extends NetworkedActor {

    private final int TIME_TO_STOP = 5000;
    private int currentTime = 0;

    public FirstActor(){
        this.setImageSynced("images/car01.png");
    }


    public void act() {
        System.out.println("Acting");
        if (currentTime < TIME_TO_STOP) {
            moveSynced(3);
            currentTime++;
        }
    }
}
