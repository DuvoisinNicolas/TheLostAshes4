package fr.univ_amu.iut.Exceptions;

public class NoSpellFoundException extends Exception {
    public NoSpellFoundException () {
        super();
    }

    public NoSpellFoundException (String message) {
        super(message);
    }

    public NoSpellFoundException (String message,Throwable cause) {
        super(message,cause);
    }

    public NoSpellFoundException (Throwable cause) {
        super(cause);
    }
}
