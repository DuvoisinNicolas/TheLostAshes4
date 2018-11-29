package fr.univ_amu.iut.Exceptions;

public class NoMapFoundException extends Exception {

    public NoMapFoundException () {
        super();
    }

    public NoMapFoundException (String message) {
        super(message);
    }

    public NoMapFoundException (String message,Throwable cause) {
        super(message,cause);
    }

    public NoMapFoundException (Throwable cause) {
        super(cause);
    }
}
