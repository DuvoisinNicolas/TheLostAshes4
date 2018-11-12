package fr.univ_amu.iut.Exceptions;

public class NoUserException extends Exception {

    public NoUserException () {
        super();
    }

    public NoUserException (String message) {
        super(message);
    }

    public NoUserException (String message,Throwable cause) {
        super(message,cause);
    }

    public NoUserException (Throwable cause) {
        super(cause);
    }
}
