import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import de.webspired.GreenfootNetworking.NetworkedActor;

public class Bullet extends NetworkedActor
{
    private final int DAMAGE;
    public Bullet(int damage){
        this.setImageSynced("images/steel-ball.png");
        this.DAMAGE = damage;
        this.getImage().scale(30, 30);
    }
    public void act()
    {
        moveSynced(5);
        if(this.isAtEdge()){
            this.getNetworkedWorld().removeNetworkObject(this);
        }
        if(this.getNetworkedWorld() != null && this.getOneIntersectingObject(Enemy.class) != null){
            this.getNetworkedWorld().removeNetworkObject(this);
            ((Enemy) this.getOneIntersectingObject(Enemy.class)).removeHealth(DAMAGE);
            System.out.println(this.getNetworkedWorld());
        }
    }
}
