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
        
        NetworkedActor a = (NetworkedActor) getOneIntersectingObject(null);
        if(a != null){
            System.out.println(a.getCreatorClient() + " " + a.getBaseClass());
        }
    }
}
