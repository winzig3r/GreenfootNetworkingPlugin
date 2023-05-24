import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import de.webspired.GreenfootNetworking.*;
import de.webspired.Enums.*;
/**
 * Write a description of class Dungeon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Dungeon extends NetworkedWorld
{
    public int enemyAmount;
    public int enemyHealth;
    public int enemyAttackSpeed;
    public int enemyDamage;
    public int enemySpeed;
    public int playerHealth;
    public int playerAttackSpeed;
    public int playerDamage;
    public int playerSpeed;
    public String stage;
    
    private int enemiesKilled = 0;
    
    Spawner s;
    Player p;
    
    public Dungeon(int width, int height, int cellSize, NetworkingOptions options, String ip, int worldId)
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(width, height, cellSize, options, ip, worldId); 
    }
    
    public void prepare(){
        p = new Player(this, playerHealth, playerDamage, playerAttackSpeed, playerSpeed);
        addNetworkObject(p, 144, 305);
        s = new Spawner(p, this);
        addObject(s, 290, 190);
        s.spawnEnemies(enemyAmount, enemyHealth, enemyDamage, enemyAttackSpeed, enemySpeed);
        updateEnemyText();
        updateStageText();
    }
    
    public void reset(){
        p.setLocationSynced(144, 305);
        enemiesKilled = 0;
        s.spawnEnemies(enemyAmount, enemyHealth, enemyDamage, enemyAttackSpeed, enemySpeed);
        updateEnemyText();
    }
    
    public void killedEnemy(){
        enemiesKilled += 1;
        updateEnemyText();
        if(enemiesKilled == enemyAmount){
            loadNext();
        }
    }
    
    public void updateHealthText(int health){
        this.showText("Player Health: " + health, 100, 30);
    }
    
    public void updateEnemyText(){
        int enemiesLeft = enemyAmount - enemiesKilled;
        this.showText("Enemies left: " + enemiesLeft, 100, 50);
    }
    
    public void updateStageText(){
        this.showText("Stage: " + stage, 100, 70);
    }
    
    public abstract void loadNext();
}
