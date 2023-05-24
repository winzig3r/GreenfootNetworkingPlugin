import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import de.webspired.Enums.*;
/**
 * Write a description of class DungeonOne here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DungeonOne extends Dungeon
{
    public DungeonOne()
    {
        super(600, 400, 1, NetworkingOptions.AsClient, "localhost", 0);
        super.enemyAmount = 10;
        super.enemyHealth = 100;
        super.enemyDamage = 10;
        super.enemyAttackSpeed = 100;
        super.enemySpeed = 1;
        super.playerHealth = 100;
        super.playerDamage = 20;
        super.playerAttackSpeed = 10;
        super.playerSpeed = 3;
        super.stage = "Dungeon 1";
        super.prepare();
    }
    
    public void loadNext(){
        Greenfoot.setWorld(new DungeonTwo());
    }
}
