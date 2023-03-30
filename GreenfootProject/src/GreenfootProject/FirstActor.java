package GreenfootProject;

import GreenfootNetworking.NetworkedActor;
import greenfoot.Greenfoot;

public class FirstActor extends NetworkedActor {


    public FirstActor(){
        this.setImageSynced("images/car01.png");
    }


    public void act() {
        moveSynced(3);
        if(Greenfoot.isKeyDown("A")){
            turnSynced(-3);
        }
        if(Greenfoot.isKeyDown("D")){
            turnSynced(3);
        }
    }
}
