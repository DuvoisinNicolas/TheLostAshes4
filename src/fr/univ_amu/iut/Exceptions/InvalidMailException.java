package fr.univ_amu.iut.Exceptions;

public class InvalidMailException extends Exception {

    public InvalidMailException () {
        super();
    }

    public InvalidMailException (String message) {
        super(message);
    }

    public InvalidMailException (String message,Throwable cause) {
        super(message,cause);
    }

    public InvalidMailException (Throwable cause) {
        super(cause);
    }
}
