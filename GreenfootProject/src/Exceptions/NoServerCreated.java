package Exceptions;

public class NoServerCreated extends RuntimeException{
    public NoServerCreated(){
        super("There was no server created so it cant be retrieved");
    }
}
