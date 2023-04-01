import GreenfootNetworking.*;
import greenfoot.*;

public class SomeActor extends NetworkedActor
{
    
    public SomeActor()
    {
        setImageSynced("images/car01.png");
    }

    public void act(){
        moveSynced(3);
        if(Greenfoot.isKeyDown("A")){
            turnSynced(-3);
        }
        
        if(Greenfoot.isKeyDown("D")){
            turnSynced(3);
        }
        
        if(Greenfoot.isKeyDown("C")){
            NetworkedWorld world = this.getNetworkedWord();
            world.addNetworkObject(new Coin(), Greenfoot.getRandomNumber(world.getWidth()), Greenfoot.getRandomNumber(world.getHeight()));
        }
    }
}
