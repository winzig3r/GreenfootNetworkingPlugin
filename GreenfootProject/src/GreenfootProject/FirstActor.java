package GreenfootProject;

import GreenfootNetworking.NetworkedActor;
import GreenfootNetworking.NetworkedWorld;
import greenfoot.Greenfoot;

public class FirstActor extends NetworkedActor {

    public FirstActor(){
        //System.out.println("Called constructor of first actor");
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
        if(Greenfoot.isKeyDown("C")){
            NetworkedWorld nw = this.getNetworkedWord();
            nw.addNetworkObject(new Coin(), Greenfoot.getRandomNumber(nw.getWidth()), Greenfoot.getRandomNumber(nw.getHeight()));
        }
    }
}
