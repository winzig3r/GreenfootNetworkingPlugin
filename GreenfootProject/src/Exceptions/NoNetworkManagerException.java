package Exceptions;

public class NoNetworkManagerException extends RuntimeException{
    public NoNetworkManagerException(){
        super("There was no Network Manager created and therefore no Server and Client");
    }
}
