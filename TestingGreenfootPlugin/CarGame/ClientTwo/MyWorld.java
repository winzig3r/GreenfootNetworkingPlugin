import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import GreenfootNetworking.*;
import Enums.*;
/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends NetworkedWorld
{
    public MyWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1, NetworkingOptions.AsClient, "127.0.0.1", 0); 
        SomeActor s = new SomeActor();
        addNetworkObject(s, 300, 300);
    }
}
