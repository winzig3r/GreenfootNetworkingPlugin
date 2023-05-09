import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import GreenfootNetworking.*;
import Enums.*;
/**
 * Write a description of class WorldTwo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class WorldTwo extends World
{

    public WorldTwo()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1, NetworkingOptions.AsServerClient, "127.0.0.1", 1); 
        prepare();
        this.showText("World 2", this.getWidth() / 2 + 5, 10);
    }
    
    private void prepare()
    {
        Player player = new Player();
        addObject(player,260,203);
        Trigger trigger = new Trigger();
        addObject(trigger,497,205);
    }
}
