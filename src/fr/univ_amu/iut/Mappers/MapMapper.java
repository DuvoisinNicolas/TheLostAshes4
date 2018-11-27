package fr.univ_amu.iut.Mappers;

import fr.univ_amu.iut.beans.Map;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapMapper {
    public static Map mapRow(ResultSet resultSet) throws SQLException {
        Map map = new Map();
        map.setIdMap(resultSet.getInt("ID_MAP"));
        map.setName(resultSet.getString("NAME"));
        map.setCheckpoint(resultSet.getBoolean("CHECKPOINT"));
        map.setText(resultSet.getString("TEXT"));
        map.setIdWeapon(resultSet.getInt("ID_WEAPON"));
        map.setIdArmor(resultSet.getInt("ID_ARMOR"));
        map.setIdEnnemi(resultSet.getInt("ID_ENNEMI"));
        map.setGolds(resultSet.getInt("GOLDS"));
        map.setTestStat(resultSet.getString("TESTSTAT"));
        map.setChoix1(resultSet.getString("CHOIX1"));
        map.setChoix2(resultSet.getString("CHOIX2"));
        map.setChoix3(resultSet.getString("CHOIX3"));
        map.setChoix4(resultSet.getString("CHOIX4"));
        map.setMap1(resultSet.getInt("MAP1"));
        map.setMap2(resultSet.getInt("MAP2"));
        map.setMap3(resultSet.getInt("MAP3"));
        map.setMap4(resultSet.getInt("MAP4"));
        return map;
    }
}