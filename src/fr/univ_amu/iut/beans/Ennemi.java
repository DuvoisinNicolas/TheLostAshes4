package fr.univ_amu.iut.beans;

import fr.univ_amu.iut.Exceptions.NoEnnemiFoundException;

import java.util.ArrayList;

public class Ennemi {

    private int idEnnemi;
    private String name;
    private int hp;
    private int dmg;
    private int prec;
    private int def;
    private boolean lethal;
    private static ArrayList<Ennemi> allEnnemis;

    public static Ennemi findItemById (int idEnnemi) throws NoEnnemiFoundException {
        for (Ennemi ennemi : Ennemi.getAllEnnemis()) {
            if (ennemi.getIdEnnemi() == idEnnemi) {
                return ennemi;
            }
        }
        throw new NoEnnemiFoundException();
    }

    public static ArrayList<Ennemi> getAllEnnemis() {
        return allEnnemis;
    }

    public static void setAllEnnemis(ArrayList<Ennemi> allEnnemis) {
        Ennemi.allEnnemis = allEnnemis;
    }

    public int getIdEnnemi() {
        return idEnnemi;
    }

    public void setIdEnnemi(int idEnnemi) {
        this.idEnnemi = idEnnemi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getDmg() {
        return dmg;
    }

    public void setDmg(int dmg) {
        this.dmg = dmg;
    }

    public int getPrec() {
        return prec;
    }

    public void setPrec(int prec) {
        this.prec = prec;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public boolean isLethal() {
        return lethal;
    }

    public void setLethal(boolean lethal) {
        this.lethal = lethal;
    }

    @Override
    public String toString() {
        return "Ennemi{" +
                "idEnnemi=" + idEnnemi +
                ", name='" + name + '\'' +
                ", hp=" + hp +
                ", dmg=" + dmg +
                ", prec=" + prec +
                ", def=" + def +
                '}';
    }
}
