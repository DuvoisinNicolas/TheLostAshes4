package fr.univ_amu.iut.Exceptions;

public class BadEntryException extends Exception {

    public BadEntryException () {
        super();
    }

    public BadEntryException (String message) {
        super(message);
    }

    public BadEntryException (String message,Throwable cause) {
        super(message,cause);
    }

    public BadEntryException (Throwable cause) {
        super(cause);
    }
}
