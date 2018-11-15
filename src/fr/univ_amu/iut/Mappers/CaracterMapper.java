package fr.univ_amu.iut.Mappers;

import fr.univ_amu.iut.beans.Caracter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CaracterMapper {
    public static Caracter mapRow(ResultSet resultSet) throws SQLException {
        Caracter caracter = new Caracter();
        caracter.setIdCara(resultSet.getInt("ID_CARA"));
        caracter.setName(resultSet.getString("NAME"));
        caracter.setHP(resultSet.getInt("HP"));
        caracter.setFCE(resultSet.getInt("FCE"));
        caracter.setAGI(resultSet.getInt("AGI"));
        caracter.setCHARI(resultSet.getInt("CHARI"));
        caracter.setEND(resultSet.getInt("END"));
        caracter.setMAG(resultSet.getInt("MAG"));
        caracter.setGolds(resultSet.getInt("GOLDS"));
        caracter.setSavedId(resultSet.getInt("SAVED_ID"));
        caracter.setIdUser(resultSet.getInt("ID_USER"));
        return caracter;
    }
}
