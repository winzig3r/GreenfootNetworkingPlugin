package GreenfootProject;

import Enums.NetworkingOptions;
import GreenfootNetworking.NetworkedWorld;

import java.util.concurrent.TimeUnit;

public class FirstWorld extends NetworkedWorld {
    public FirstWorld() {
        super(600, 600, 1, NetworkingOptions.AsServerClient, "localhost");
        FirstActor firstActor = new FirstActor();
        addNetworkObject(firstActor, 300, 300);
        System.out.println("After the object was added there are: " + this.numberOfObjects() + " objects in the world");
    }
}
