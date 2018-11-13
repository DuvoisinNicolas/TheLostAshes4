package fr.univ_amu.iut.Mappers;


import fr.univ_amu.iut.beans.Spell;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpellMapper {
    public static Spell mapRow(ResultSet resultSet) throws SQLException {
        Spell spell = new Spell();
        spell.setIdSpell(resultSet.getInt("ID_SPELL"));
        spell.setName(resultSet.getString("NAME"));
        spell.setDescr(resultSet.getString("DESCR"));
        return spell;
    }
}
