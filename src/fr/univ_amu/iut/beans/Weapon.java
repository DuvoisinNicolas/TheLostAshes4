package fr.univ_amu.iut.beans;

import fr.univ_amu.iut.Exceptions.NoWeaponFoundException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Weapon {

    private int idWeapon;
    private String name;
    private int dmg;
    private int prec;
    private int price;
    private IntegerProperty fce = new SimpleIntegerProperty();
    private IntegerProperty agi = new SimpleIntegerProperty();
    private IntegerProperty chari = new SimpleIntegerProperty();
    private IntegerProperty end = new SimpleIntegerProperty();
    private IntegerProperty mag = new SimpleIntegerProperty();

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
        return fce.get();
    }

    public int getAgi() {
        return agi.get();
    }

    public int getChari() {
        return chari.get();
    }

    public int getEnd() {
        return end.get();
    }

    public int getMag() {
        return mag.get();
    }

    public void setFce(int fce) {
        this.fce.set(fce);
    }

    public void setAgi(int agi) {
        this.agi.set(agi);
    }

    public void setChari(int chari) {
        this.chari.set(chari);
    }

    public void setEnd(int end) {
        this.end.set(end);
    }

    public void setMag(int mag) {
        this.mag.set(mag);
    }

    public IntegerProperty fceProperty() {
        return fce;
    }

    public IntegerProperty agiProperty() {
        return agi;
    }

    public IntegerProperty chariProperty() {
        return chari;
    }

    public IntegerProperty endProperty() {
        return end;
    }

    public IntegerProperty magProperty() {
        return mag;
    }

    public static List<Weapon> getAllWeapons() {
        return allWeapons;
    }

    public static void setAllWeapons(List<Weapon> allWeapons) {
        Weapon.allWeapons = allWeapons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weapon weapon = (Weapon) o;
        return idWeapon == weapon.idWeapon &&
                dmg == weapon.dmg &&
                prec == weapon.prec &&
                price == weapon.price &&
                Objects.equals(name, weapon.name) &&
                Objects.equals(fce, weapon.fce) &&
                Objects.equals(agi, weapon.agi) &&
                Objects.equals(chari, weapon.chari) &&
                Objects.equals(end, weapon.end) &&
                Objects.equals(mag, weapon.mag);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idWeapon, name, dmg, prec, price, fce, agi, chari, end, mag);
    }

    @Override
    public String toString() {
        return "Weapon{" +
                "idWeapon=" + idWeapon +
                ", name='" + name + '\'' +
                ", dmg=" + dmg +
                ", prec=" + prec +
                ", price=" + price +
                ", fce=" + fce +
                ", agi=" + agi +
                ", chari=" + chari +
                ", end=" + end +
                ", mag=" + mag +
                '}';
    }
}
