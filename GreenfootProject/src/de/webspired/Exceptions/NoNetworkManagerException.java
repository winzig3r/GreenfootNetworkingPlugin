package de.webspired.Exceptions;

public class NoNetworkManagerException extends RuntimeException{
    public NoNetworkManagerException(){
        super("There was no Network Manager created and therefore no de.webspired.Server and de.webspired.Client");
    }
}
