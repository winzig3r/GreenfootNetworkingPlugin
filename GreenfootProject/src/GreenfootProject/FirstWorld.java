package GreenfootProject;

import Enums.NetworkingOptions;
import GreenfootNetworking.GreenfootNetworkManager;
import GreenfootNetworking.NetworkedWorld;

public class FirstWorld extends NetworkedWorld {
    public FirstWorld() {
        super(600, 600, 1, 0, NetworkingOptions.AsServerClient, "localhost");
        addNetworkObject(new FirstActor(), 300, 300);
    }
}
