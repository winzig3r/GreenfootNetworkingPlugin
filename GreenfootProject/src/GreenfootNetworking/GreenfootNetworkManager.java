package GreenfootNetworking;

import Client.Client;
import Enums.NetworkingOptions;
import Exceptions.NoClientCreated;
import Exceptions.NoNetworkManagerException;
import Server.Server;

public class GreenfootNetworkManager {

    private Client client;
    private NetworkingOptions currentNetworkingOptions;

    private static GreenfootNetworkManager instance;

    public GreenfootNetworkManager(NetworkingOptions networkingOptions, String ip) {
        if (instance == null) {
            if (networkingOptions.equals(NetworkingOptions.AsServer) || networkingOptions.equals(NetworkingOptions.AsServerClient)) {
                Server.start();
                client = new Client("localhost", 6969, 6968);
            } else if (networkingOptions.equals(NetworkingOptions.AsClient)) {
                client = new Client(ip, 6969, 6968);
            }
            this.currentNetworkingOptions = networkingOptions;
            instance = this;
        }
    }

    public static boolean isServer() {
        return !GreenfootNetworkManager.instance.currentNetworkingOptions.equals(NetworkingOptions.AsClient);
    }

    public static boolean isClient() {
        return !GreenfootNetworkManager.instance.currentNetworkingOptions.equals(NetworkingOptions.AsServer);
    }


    public static GreenfootNetworkManager getInstance() throws NoNetworkManagerException {
        if (instance != null) {
            return instance;
        }
        throw new NoNetworkManagerException();
    }

    public Client getClient() {
        if (instance.client != null) {
            return instance.client;
        }
        throw new NoClientCreated();
    }
}
