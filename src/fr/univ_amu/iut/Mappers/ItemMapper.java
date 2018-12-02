package fr.univ_amu.iut.Mappers;

import fr.univ_amu.iut.beans.Item;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemMapper {
    public static Item mapRow(ResultSet resultSet) throws SQLException {
        Item item = new Item();
        item.setIdItem(resultSet.getInt("ID_ITEM"));
        item.setName(resultSet.getString("NAME"));
        item.setDesc(resultSet.getString("DESCR"));
        item.setConso(resultSet.getBoolean("CONSO"));
        item.setPrice(resultSet.getInt("PRICE"));
        return item;
    }
}
