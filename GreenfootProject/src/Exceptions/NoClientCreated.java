package Exceptions;

public class NoClientCreated extends RuntimeException{
    public NoClientCreated(){
        super("There was no client created so it cant be retrieved");
    }
}
