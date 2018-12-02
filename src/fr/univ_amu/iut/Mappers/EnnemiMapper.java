package fr.univ_amu.iut.Mappers;

import fr.univ_amu.iut.beans.Ennemi;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EnnemiMapper {
    public static Ennemi mapRow(ResultSet resultSet) throws SQLException {
        Ennemi ennemi = new Ennemi();
        ennemi.setIdEnnemi(resultSet.getInt("ID_ENNEMI"));
        ennemi.setName(resultSet.getString("NAME"));
        ennemi.setHp(resultSet.getInt("HP"));
        ennemi.setDmg(resultSet.getInt("DMG"));
        ennemi.setPrec(resultSet.getInt("PREC"));
        ennemi.setDef(resultSet.getInt("DEF"));
        ennemi.setLethal(resultSet.getBoolean("LETHAL"));
        return ennemi;
    }
}