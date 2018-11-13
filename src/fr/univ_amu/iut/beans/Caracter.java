package fr.univ_amu.iut.beans;

import java.util.Objects;

public class Caracter {
    private int idCara;
    private String name;
    private int HP;
    private int FCE;
    private int AGI;
    private int CHARI;
    private int END;
    private int MAG;
    private int golds;
    private int savedId;
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

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public int getFCE() {
        return FCE;
    }

    public void setFCE(int FCE) {
        this.FCE = FCE;
    }

    public int getAGI() {
        return AGI;
    }

    public void setAGI(int AGI) {
        this.AGI = AGI;
    }

    public int getCHARI() {
        return CHARI;
    }

    public void setCHARI(int CHARI) {
        this.CHARI = CHARI;
    }

    public int getEND() {
        return END;
    }

    public void setEND(int END) {
        this.END = END;
    }

    public int getMAG() {
        return MAG;
    }

    public void setMAG(int MAG) {
        this.MAG = MAG;
    }

    public int getGolds() {
        return golds;
    }

    public void setGolds(int golds) {
        this.golds = golds;
    }

    public int getSavedId() {
        return savedId;
    }

    public void setSavedId(int savedId) {
        this.savedId = savedId;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caracter caracter = (Caracter) o;
        return idCara == caracter.idCara &&
                HP == caracter.HP &&
                FCE == caracter.FCE &&
                AGI == caracter.AGI &&
                CHARI == caracter.CHARI &&
                END == caracter.END &&
                MAG == caracter.MAG &&
                golds == caracter.golds &&
                savedId == caracter.savedId &&
                idUser == caracter.idUser &&
                Objects.equals(name, caracter.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCara, name, HP, FCE, AGI, CHARI, END, MAG, golds, savedId, idUser);
    }
}
