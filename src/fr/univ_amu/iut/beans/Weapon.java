package fr.univ_amu.iut.beans;

import fr.univ_amu.iut.Exceptions.NoWeaponFoundException;

import java.util.ArrayList;
import java.util.List;

public class Weapon {

    private int idWeapon;
    private String name;
    private int dmg;
    private int prec;
    private int price;
    private int fce;
    private int agi;
    private int chari;
    private int end;
    private int mag;
    private static List<Weapon> allWeapons = new ArrayList<>();


    public static Weapon findWeaponById (int idWeapon) throws NoWeaponFoundException {
        for (Weapon weapon : Weapon.getAllWeapons()) {
            if (weapon.getIdWeapon() == idWeapon) {
                return weapon;
            }
        }
        throw new NoWeaponFoundException();
    }

    public int getIdWeapon() {
        return idWeapon;
    }

    public void setIdWeapon(int idWeapon) {
        this.idWeapon = idWeapon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public static List<Weapon> getAllWeapons() {
        return allWeapons;
    }

    public static void setAllWeapons(List<Weapon> allWeapons) {
        Weapon.allWeapons = allWeapons;
    }
}
