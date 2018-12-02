package fr.univ_amu.iut.beans;

import fr.univ_amu.iut.Exceptions.NoArmorFoundException;
import fr.univ_amu.iut.Exceptions.NoWeaponFoundException;

import java.util.ArrayList;

public class Armor {
    private int idArmor;
    private String name;
    private int def;
    private int price;
    private int fce;
    private int agi;
    private int chari;
    private int end;
    private int mag;

    private static ArrayList<Armor> allArmors = new ArrayList<>();


    public static Armor findArmorById (int idArmor) throws NoArmorFoundException {
        for (Armor armor : Armor.getAllArmors()) {
            if (armor.getIdArmor() == idArmor) {
                return armor;
            }
        }
        throw new NoArmorFoundException();
    }

    public static ArrayList<Armor> getAllArmors() {
        return allArmors;
    }

    public static void setAllArmors(ArrayList<Armor> allArmors) {
        Armor.allArmors = allArmors;
    }

    public int getIdArmor() {
        return idArmor;
    }

    public void setIdArmor(int idArmor) {
        this.idArmor = idArmor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFce() {
        return fce;
    }

    public void setFce(int fce) {
        this.fce = fce;
    }

    public int getAgi() {
        return agi;
    }

    public void setAgi(int agi) {
        this.agi = agi;
    }

    public int getChari() {
        return chari;
    }

    public void setChari(int chari) {
        this.chari = chari;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getMag() {
        return mag;
    }

    public void setMag(int mag) {
        this.mag = mag;
    }

    @Override
    public String toString() {
        return "Armor{" +
                "idArmor=" + idArmor +
                ", name='" + name + '\'' +
                ", def=" + def +
                '}';
    }
}
