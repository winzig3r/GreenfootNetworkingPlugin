import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import GreenfootNetworking.*;

/**
 * Write a description of class Player here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player extends NetworkedActor
{
    public void act()
    {
        if(Greenfoot.isKeyDown("W")){
            this.setLocationSynced(this.getX(), this.getY() - 3); 
        }
        if(Greenfoot.isKeyDown("A")){
            this.setLocationSynced(this.getX() - 3, this.getY()); 
        }
        if(Greenfoot.isKeyDown("S")){
            this.setLocationSynced(this.getX(), this.getY() + 3); 
        }
        if(Greenfoot.isKeyDown("D")){
            this.setLocationSynced(this.getX() + 3, this.getY()); 
        }
        if(Greenfoot.isKeyDown("C")){
            this.getNetworkedWorld().addNetworkObject(new TestObject(), this.getX(), this.getY());
        }
        
        
    }
}
