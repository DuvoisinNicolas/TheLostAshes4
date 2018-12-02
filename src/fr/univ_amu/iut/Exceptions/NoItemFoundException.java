package fr.univ_amu.iut.Exceptions;

public class NoItemFoundException extends Exception{
    public NoItemFoundException () {
        super();
    }

    public NoItemFoundException (String message) {
        super(message);
    }

    public NoItemFoundException (String message,Throwable cause) {
        super(message,cause);
    }

    public NoItemFoundException (Throwable cause) {
        super(cause);
    }
}
