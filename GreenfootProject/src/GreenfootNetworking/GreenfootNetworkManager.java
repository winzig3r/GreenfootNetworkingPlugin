package GreenfootNetworking;

import Client.Client;
import Enums.NetworkingOptions;
import Exceptions.NoClientCreated;
import Exceptions.NoNetworkManagerException;
import Exceptions.NoServerCreated;
import Server.Server;

public class GreenfootNetworkManager {

    private Server server;
    private Client client;

    private static GreenfootNetworkManager instance;

    public GreenfootNetworkManager(NetworkingOptions networkingOptions, String ip){
        if(instance == null){
            if (networkingOptions.equals(NetworkingOptions.AsServer)) {
                server = new Server();
            } else if (networkingOptions.equals(NetworkingOptions.AsClient)) {
                System.out.println("Creating client");
                client = new Client(ip, 6969, 6968);
            } else if (networkingOptions.equals(NetworkingOptions.AsServerClient)) {
                System.out.println("Creating client");
                server = new Server();
                client = new Client("localhost", 6969, 6968);
            }
           instance = this;
        }
    }

    public static boolean isServer(){
        return GreenfootNetworkManager.instance.server != null;
    }
    public static boolean isClient(){
        return GreenfootNetworkManager.instance.client != null;
    }
    public static boolean notInstantiated() {return instance == null;}
    public static GreenfootNetworkManager getInstance() throws NoNetworkManagerException {
        if(instance != null){
            return instance;
        }
        throw new NoNetworkManagerException();
    }

    public Client getClient(){
        if(this.client != null){
            return instance.client;
        }
        throw new NoClientCreated();
    }

    public Server getServer(){
        if(this.server != null){
            return instance.server;
        }
        throw new NoServerCreated();
    }
}
