import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import de.webspired.GreenfootNetworking.*;
import de.webspired.Enums.*;
/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class WorldOne extends NetworkedWorld
{
    public WorldOne()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1, NetworkingOptions.AsClient, "127.0.0.1", 0); 
        prepare();
        this.showText("World 1", this.getWidth() / 2 + 5, 10);
    }
    
    private void prepare()
    {
        Player player = new Player();
        addNetworkObject(player,260,203);
        Trigger trigger = new Trigger();
        addObject(trigger,497,205);
    }
}
