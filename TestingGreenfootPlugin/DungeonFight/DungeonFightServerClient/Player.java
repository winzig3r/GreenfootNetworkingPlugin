import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import de.webspired.GreenfootNetworking.*;

public class Player extends NetworkedActor
{
    private final int ATTACK_SPEED;
    private final int START_HEALTH;
    private final int DAMAGE;
    private final int SPEED;
    private Dungeon dungeon;
    private int timeToNextShot = 0;
    private int health;
    
    public Player(Dungeon d, int health, int damage, int attackSpeed, int playerSpeed){
        this.ATTACK_SPEED = attackSpeed;
        this.START_HEALTH = health;
        this.DAMAGE = damage;
        this.health = health;
        this.SPEED = playerSpeed;
        
        this.dungeon = d;
        setImageSynced("images/ppl1.png");
        dungeon.updateHealthText(health);
    }
    
    public void act()
    {
        if(Greenfoot.isKeyDown("W")){
            setLocationSynced(this.getX(), this.getY() - SPEED);
        }
        if(Greenfoot.isKeyDown("A")){
            moveSynced(-SPEED);
        }
        if(Greenfoot.isKeyDown("S")){
            setLocationSynced(this.getX(), this.getY() + SPEED);
        }
        if(Greenfoot.isKeyDown("D")){
            moveSynced(SPEED);
        }
        
        if(timeToNextShot <= 0 && Greenfoot.getMouseInfo() != null && Greenfoot.getMouseInfo().getButton() == 1){
            //Shooting
            Bullet b = new Bullet(DAMAGE);
            this.getNetworkedWorld().addNetworkObject(b, this.getX(), this.getY());
            b.turnTowards(Greenfoot.getMouseInfo().getX(), Greenfoot.getMouseInfo().getY());
            timeToNextShot = ATTACK_SPEED;
        }
        
        timeToNextShot--;
    }
    
    public void removeHealth(int damage){
        health -= damage;
        if(health <= 0){
            dungeon.reset();
            this.health = START_HEALTH;
        }
        dungeon.updateHealthText(health);
    }
    
    public boolean isAlive(){
        return this.health > 0;
    }

}
