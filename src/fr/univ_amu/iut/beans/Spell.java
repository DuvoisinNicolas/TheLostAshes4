package fr.univ_amu.iut.beans;

import java.util.Objects;

public class Spell {

    private int idSpell;
    private String name;
    private String descr;

    public int getIdSpell() {
        return idSpell;
    }

    public void setIdSpell(int idSpell) {
        this.idSpell = idSpell;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Spell spell = (Spell) o;
        return idSpell == spell.idSpell &&
                Objects.equals(name, spell.name) &&
                Objects.equals(descr, spell.descr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSpell, name, descr);
    }
}
