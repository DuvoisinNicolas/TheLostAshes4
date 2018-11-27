package fr.univ_amu.iut.Exceptions;

public class MapErrorException extends Exception {

    public MapErrorException () {
        super();
    }

    public MapErrorException (String message) {
        super(message);
    }

    public MapErrorException (String message,Throwable cause) {
        super(message,cause);
    }

    public MapErrorException (Throwable cause) {
        super(cause);
    }
}
