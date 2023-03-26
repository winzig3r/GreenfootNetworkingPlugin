package GreenfootNetworking;

import Client.Client;
import Enums.NetworkingOptions;
import Server.Server;
import greenfoot.World;

public class NetworkedWorld extends World {

    public NetworkedWorld(int worldWidth, int worldHeight, int cellSize, NetworkingOptions options) {
        super(worldWidth, worldHeight, cellSize);
        if(options.equals(NetworkingOptions.AsServer)){
            createServer();
        } else if (options.equals(NetworkingOptions.AsServerClient)) {
            createServerClient();
        }
    }

    public NetworkedWorld(int worldWidth, int worldHeight, int cellSize, boolean bounded, NetworkingOptions options) {
        super(worldWidth, worldHeight, cellSize, bounded);
        if(options.equals(NetworkingOptions.AsServer)){
            createServer();
        } else if (options.equals(NetworkingOptions.AsServerClient)) {
            createServerClient();
        }
    }

    public NetworkedWorld(int worldWidth, int worldHeight, int cellSize, NetworkingOptions options, String ip) {
        super(worldWidth, worldHeight, cellSize);
        createClient(ip);
    }

    public NetworkedWorld(int worldWidth, int worldHeight, int cellSize, boolean bounded, NetworkingOptions options, String ip) {
        super(worldWidth, worldHeight, cellSize, bounded);
        createClient(ip);
    }

    public void createServer(){
        Server s = new Server();
    }

    public void createClient(String ip){
        Client c = new Client(ip, 6969, 6968);
    }

    public void createServerClient(){
        Server s = new Server();
        Client c = new Client("localhost", 6969, 6968);
    }
}
