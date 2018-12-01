package fr.univ_amu.iut.Mappers;

import fr.univ_amu.iut.beans.Armor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArmorMapper {
    public static Armor mapRow(ResultSet resultSet) throws SQLException {
        Armor armor = new Armor();
        armor.setIdArmor(resultSet.getInt("ID_ARMOR"));
        armor.setName(resultSet.getString("NAME"));
        armor.setDef(resultSet.getInt("DEF"));
        armor.setFce(resultSet.getInt("FCE"));
        armor.setAgi(resultSet.getInt("AGI"));
        armor.setChari(resultSet.getInt("CHARI"));
        armor.setEnd(resultSet.getInt("END"));
        armor.setMag(resultSet.getInt("MAG"));
        return armor;
    }
}
