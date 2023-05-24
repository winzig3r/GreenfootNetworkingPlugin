import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
/**
 * Write a description of class Spawner here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Spawner extends Actor
{
    private final Player target;
    private final Dungeon dungeon;
    private final ArrayList<Enemy> currentEnemies = new ArrayList<Enemy>();
    public Spawner(Player target, Dungeon dungeon){
        this.target = target;
        this.dungeon = dungeon;
    }
    
    public void spawnEnemies(int amount, int enemyHealth, int enemyDamage, int enemyAttackSpeed, int speed){
        removeCurrentEnemies();
        for(int i = 0; i < amount; i++){
            int worldHeight = this.getWorld().getHeight();
            int worldWidth = this.getWorld().getWidth();
            int x = ThreadLocalRandom.current().nextInt(50, worldWidth - 50 + 1);
            int y = ThreadLocalRandom.current().nextInt(50, worldHeight - 50 + 1);
            Enemy e = new Enemy(target, dungeon, enemyDamage, enemyHealth, enemyAttackSpeed, speed);
            this.getWorld().addObject(e, x, y);
            currentEnemies.add(e);
            e.fixPosition();
        }
    }
    
    public void removeCurrentEnemies(){
        for(Enemy e : currentEnemies){
            this.getWorld().removeObject(e);
        }
        currentEnemies.clear();
    }
}
