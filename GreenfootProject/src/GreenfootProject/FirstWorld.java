package GreenfootProject;

import Enums.NetworkingOptions;
import GreenfootNetworking.NetworkedWorld;


public class FirstWorld extends NetworkedWorld {
    public FirstWorld() {
        super(600, 600, 1, NetworkingOptions.AsServerClient, "localhost", 0);
        FirstActor firstActor = new FirstActor();
        addNetworkObject(firstActor, 300, 300);
        //System.out.println("-------------- World Constructor finished --------------");
    }
}
