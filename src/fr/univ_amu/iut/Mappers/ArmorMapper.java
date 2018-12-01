package fr.univ_amu.iut.Mappers;

import fr.univ_amu.iut.beans.Armor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArmorMapper {
    public static Armor mapRow(ResultSet resultSet) throws SQLException {
        Armor armor = new Armor();
        armor.setIdArmor(resultSet.getInt("ID_ARMOR"));
        return armor;
    }
}
