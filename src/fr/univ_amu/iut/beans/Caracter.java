package fr.univ_amu.iut.beans;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Objects;

public class Caracter {
    private int idCara;
    private String name;
    private IntegerProperty HP = new SimpleIntegerProperty();
    private IntegerProperty CURRHP = new SimpleIntegerProperty();
    private IntegerProperty FCE = new SimpleIntegerProperty();
    private IntegerProperty AGI = new SimpleIntegerProperty();
    private IntegerProperty CHARI = new SimpleIntegerProperty();
    private IntegerProperty END = new SimpleIntegerProperty();
    private IntegerProperty MAG = new SimpleIntegerProperty();
    private IntegerProperty golds = new SimpleIntegerProperty();
    private int idUser;

    public int getIdCara() {
        return idCara;
    }

    public void setIdCara(int idCara) {
        this.idCara = idCara;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IntegerProperty getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP.set(HP);
    }

    public IntegerProperty getFCE() {
        return FCE;
    }

    public void setFCE(int FCE) {
        this.FCE.set(FCE);
    }

    public IntegerProperty getAGI() {
        return AGI;
    }

    public void setAGI(int AGI) {
        this.AGI.set(AGI);
    }

    public IntegerProperty getCHARI() {
        return CHARI;
    }

    public void setCHARI(int CHARI) {
        this.CHARI.set(CHARI);
    }

    public IntegerProperty getEND() {
        return END;
    }

    public void setEND(int END) {
        this.END.set(END);
    }

    public IntegerProperty getMAG() {
        return MAG;
    }

    public void setMAG(int MAG) {
        this.MAG.set(MAG);
    }

    public IntegerProperty getGolds() {
        return golds;
    }

    public void setGolds(int golds) {
        this.golds.set(golds);
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public IntegerProperty getCURRHP() {
        return CURRHP;
    }

    public void setCURRHP(int CURRHP) {
        this.CURRHP.set(CURRHP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caracter caracter = (Caracter) o;
        return idCara == caracter.idCara &&
                idUser == caracter.idUser &&
                Objects.equals(name, caracter.name) &&
                Objects.equals(HP, caracter.HP) &&
                Objects.equals(CURRHP, caracter.CURRHP) &&
                Objects.equals(FCE, caracter.FCE) &&
                Objects.equals(AGI, caracter.AGI) &&
                Objects.equals(CHARI, caracter.CHARI) &&
                Objects.equals(END, caracter.END) &&
                Objects.equals(MAG, caracter.MAG) &&
                Objects.equals(golds, caracter.golds);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idCara, name, HP, CURRHP, FCE, AGI, CHARI, END, MAG, golds, idUser);
    }
}
