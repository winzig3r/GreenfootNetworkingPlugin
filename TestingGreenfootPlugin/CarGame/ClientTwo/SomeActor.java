import GreenfootNetworking.*;
import greenfoot.*;

public class SomeActor extends NetworkedActor
{
    private final int TIME_TO_NEXT_SPAWN = 1;
    private int timeToNextSpawn = 0;
    
    public SomeActor()
    {
        setImageSynced("images/car02.png");
    }

    public void act(){
        moveSynced(3);
        if(Greenfoot.isKeyDown("A")){
            turnSynced(-3);
        }
        
        if(Greenfoot.isKeyDown("D")){
            turnSynced(3);
        }
        
        if(Greenfoot.isKeyDown("C") && timeToNextSpawn <= 0){
            NetworkedWorld world = this.getNetworkedWorld();
            Coin c = new Coin();
            world.addNetworkObject(c, Greenfoot.getRandomNumber(world.getWidth()), Greenfoot.getRandomNumber(world.getHeight()));
        }
        timeToNextSpawn--;
    }
}
