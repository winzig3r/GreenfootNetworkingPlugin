package GreenfootProject;

import Enums.NetworkingOptions;
import GreenfootNetworking.NetworkedWorld;

public class FirstWorld extends NetworkedWorld {
    public FirstWorld() {
        super(600, 600, 1, 0, NetworkingOptions.AsServerClient, "localhost");
        FirstActor firstActor = new FirstActor();
        addNetworkObject(firstActor, 300, 300);
    }
}
