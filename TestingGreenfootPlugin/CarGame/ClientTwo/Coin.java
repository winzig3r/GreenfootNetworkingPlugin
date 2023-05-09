import GreenfootNetworking.*;
import greenfoot.*;
public class Coin extends NetworkedActor
{
    
    public Coin() {
        setImageSynced("images/yellow-draught-king.png");
    }

    @Override
    public void act() {
        this.getNetworkedWorld().removeNetworkObject(this);
    }
}
