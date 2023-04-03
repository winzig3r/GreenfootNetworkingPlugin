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
            instance = this;
        }else{
            if(instance.currentNetworkingOptions == networkingOptions) return;
            //There was already an instance created => networking Options were changed
            if((instance.currentNetworkingOptions.equals(NetworkingOptions.AsServer) || instance.currentNetworkingOptions.equals(NetworkingOptions.AsServerClient)) && networkingOptions.equals(NetworkingOptions.AsClient)){
                //Changed from being a Server or a Server and a client to being only a client
                System.out.println("Changing from Server to ServerClient || Client");
                Server.reset();
                instance.client.changeConnection(ip, 6969, 6968);
            } else if (instance.currentNetworkingOptions.equals(NetworkingOptions.AsClient) && (networkingOptions.equals(NetworkingOptions.AsServer) || networkingOptions.equals(NetworkingOptions.AsServerClient))) {
                //Changed from being only a client to being a server or a server and a client
                System.out.println("Changing from Client to ServerClient || Server");
                Server.start();
                instance.client.changeConnection("localhost", 6969, 6968);
            }
        }
        instance.currentNetworkingOptions = networkingOptions;
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
