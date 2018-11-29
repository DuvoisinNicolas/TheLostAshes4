package fr.univ_amu.iut.Mappers;

import fr.univ_amu.iut.beans.Weapon;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WeaponMapper {
    public static Weapon mapRow(ResultSet resultSet) throws SQLException {
        Weapon weapon = new Weapon();
        weapon.setIdWeapon(resultSet.getInt("ID_WEAPON"));
        weapon.setName(resultSet.getString("NAME"));
        weapon.setDmg(resultSet.getInt("DMG"));
        weapon.setPrec(resultSet.getInt("PREC"));
        weapon.setPrice(resultSet.getInt("PRICE"));
        weapon.setFce(resultSet.getInt("FCE"));
        weapon.setAgi(resultSet.getInt("AGI"));
        weapon.setMag(resultSet.getInt("MAG"));
        weapon.setEnd(resultSet.getInt("END"));
        weapon.setChari(resultSet.getInt("CHARI"));
        return weapon;
    }
}
