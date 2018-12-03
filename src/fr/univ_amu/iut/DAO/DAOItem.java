package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Exceptions.NoConnectionException;
import fr.univ_amu.iut.Exceptions.NoItemFoundException;
import fr.univ_amu.iut.Mappers.ItemMapper;
import fr.univ_amu.iut.UniqueConnection;
import fr.univ_amu.iut.beans.Caracter;
import fr.univ_amu.iut.beans.Item;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOItem {
    private Connection connection;

    public DAOItem() throws NoConnectionException {
        connection = UniqueConnection.getInstance().getConnection();
    }

    public ArrayList<Pair<Item,Integer>> findCaraItems(Caracter caracter) throws SQLException, NoItemFoundException {
        List<Pair<Integer,Integer>> idItem = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ITEMINVENTORY WHERE ID_CARA=?");
        preparedStatement.setInt(1,caracter.getIdCara());
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Pair<Item,Integer>> list = new ArrayList<>();
        while (resultSet.next()) {
            Pair<Integer, Integer> pair = new Pair<>(resultSet.getInt("ID_ITEM"),resultSet.getInt("QUANTITY"));
            idItem.add(pair);
        }
        for (Pair<Integer, Integer> item : idItem) {
            list.add(new Pair<>(Item.findItemById(item.getKey()),item.getValue()));
        }
        return list;
    }


    public ArrayList<Item> findAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ITEM");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Item> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(ItemMapper.mapRow(resultSet));
        }
        return list;
    }
}
