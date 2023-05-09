import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Trigger here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Trigger extends Actor
{    
    public void act()
    {
        if(this.isTouching(Player.class) && this.getWorld() instanceof WorldOne){
            Greenfoot.setWorld(new WorldTwo());
        }
        
        if(this.isTouching(Player.class) && this.getWorld() instanceof WorldTwo){
            Greenfoot.setWorld(new WorldOne());
        }
    }
}
