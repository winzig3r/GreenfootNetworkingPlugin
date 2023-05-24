import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Actor
{
    private final Player target;
    private final Dungeon dungeon;
    private final int DAMAGE;
    private final int ATTACK_SPEED;
    private final int SPEED;
    private int health;
    private int timeToNextAttack = 0;
    
    private boolean needsToBeRemoved = false;
    
    public Enemy(Player p, Dungeon d, int damage, int health, int attackSpeed, int speed){
        this.target = p;
        this.DAMAGE = damage;
        this.health = health;
        this.ATTACK_SPEED = attackSpeed;
        this.SPEED = speed;
        this.dungeon = d;
    }
    
    public void act()
    {
       turnTowards(target.getX(), target.getY());
       move(SPEED);
       setRotation(0);
       if(this.getOneIntersectingObject(Player.class) != null && timeToNextAttack <= 0){
           ((Player) this.getOneIntersectingObject(Player.class)).removeHealth(DAMAGE);
           timeToNextAttack = ATTACK_SPEED;
       }
       timeToNextAttack--;
    }
    
    public void removeHealth(int damage){
        health -= damage;
        if(health <= 0){
            dungeon.killedEnemy();
            this.getWorld().removeObject(this);
        }
    }
    
    public void fixPosition(){
        if(this.isTouching(Player.class) || this.isTouching(Spawner.class)){
            this.setLocation(this.getX() + 30, this.getY() - 30);
            if(this.isTouching(Player.class) || this.isTouching(Spawner.class)){
                this.fixPosition();
            }
        }
    }
}
