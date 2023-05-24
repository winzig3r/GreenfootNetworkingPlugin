import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import de.webspired.Enums.*;
/**
 * Write a description of class DungeonTwo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DungeonTwo extends Dungeon
{

    public DungeonTwo()
    {
        super(600, 400, 1, NetworkingOptions.AsServerClient, "localhost", 1);
        super.enemyAmount = 5;
        super.enemyHealth = 300;
        super.enemyDamage = 20;
        super.enemyAttackSpeed = 50;
        super.enemySpeed = 2;
        super.playerHealth = 150;
        super.playerDamage = 10;
        super.playerAttackSpeed = 20;
        super.playerSpeed = 3;
        super.stage = "Dungeon 2";
        super.prepare();
    }
    
    public void loadNext(){
        System.out.println("Won");
    }
}
