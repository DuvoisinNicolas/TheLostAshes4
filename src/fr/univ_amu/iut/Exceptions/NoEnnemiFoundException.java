package fr.univ_amu.iut.Exceptions;

public class NoEnnemiFoundException extends Exception {
    public NoEnnemiFoundException () {
        super();
    }

    public NoEnnemiFoundException (String message) {
        super(message);
    }

    public NoEnnemiFoundException (String message,Throwable cause) {
        super(message,cause);
    }

    public NoEnnemiFoundException (Throwable cause) {
        super(cause);
    }
}
