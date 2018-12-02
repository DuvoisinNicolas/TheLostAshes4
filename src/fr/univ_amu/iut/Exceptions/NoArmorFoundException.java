package fr.univ_amu.iut.Exceptions;

public class NoArmorFoundException extends Exception{

    public NoArmorFoundException () {
        super();
    }

    public NoArmorFoundException (String message) {
        super(message);
    }

    public NoArmorFoundException (String message,Throwable cause) {
        super(message,cause);
    }

    public NoArmorFoundException (Throwable cause) {
        super(cause);
    }

}
