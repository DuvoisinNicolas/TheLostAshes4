package fr.univ_amu.iut.Exceptions;

public class NoWeaponFoundException extends Exception {

    public NoWeaponFoundException () {
        super();
    }

    public NoWeaponFoundException (String message) {
        super(message);
    }

    public NoWeaponFoundException (String message,Throwable cause) {
        super(message,cause);
    }

    public NoWeaponFoundException (Throwable cause) {
        super(cause);
    }
}
