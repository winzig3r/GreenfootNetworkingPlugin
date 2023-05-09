import GreenfootNetworking.*;
import Enums.*;

public class World extends NetworkedWorld
{
    public World()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1, NetworkingOptions.AsServerClient, "192.168.178.139", 0); 
        SomeActor s = new SomeActor();
        addNetworkObject(s, 300, 100);
    }
}
