package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Exceptions.NoConnectionException;
import fr.univ_amu.iut.Mappers.MapMapper;
import fr.univ_amu.iut.UniqueConnection;
import fr.univ_amu.iut.beans.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOMap {

    private Connection connection;

    public DAOMap() throws NoConnectionException {
        connection = UniqueConnection.getInstance().getConnection();
    }

    public ArrayList<Map> findAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM MAP");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Map> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(MapMapper.mapRow(resultSet));
        }
        return list;
    }
}
