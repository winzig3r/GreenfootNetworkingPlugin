import GreenfootNetworking.*;
import greenfoot.*;
public class Coin extends NetworkedActor
{
     private final int LIFETIME;
    private int lived = 0;
    private int direction;
    private int changeDirection;

    public Coin() {
        LIFETIME = Greenfoot.getRandomNumber(300);
        changeDirection = Greenfoot.getRandomNumber(100);
        direction = (Greenfoot.getRandomNumber(2) == 1) ? 1 : -1;
        setImageSynced("images/yellow-draught-king.png");
    }

    @Override
    public void act() {
        if (lived >= LIFETIME) {
            this.getNetworkedWord().removeNetworkObject(this);
        }
        if (changeDirection <= 0) {
            changeDirection = Greenfoot.getRandomNumber(100);
            direction *= -1;
        }
        lived++;
        changeDirection--;
        moveSynced(3 * direction);
    }
}
