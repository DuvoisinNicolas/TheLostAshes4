package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Exceptions.NoConnectionException;
import fr.univ_amu.iut.Mappers.EnnemiMapper;
import fr.univ_amu.iut.UniqueConnection;
import fr.univ_amu.iut.beans.Ennemi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOEnnemi {
    private Connection connection;

    public DAOEnnemi() throws NoConnectionException {
        connection = UniqueConnection.getInstance().getConnection();
    }

    public ArrayList<Ennemi> findAll() throws SQLException {
        ArrayList<Ennemi> ennemis = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ENNEMI");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            ennemis.add(EnnemiMapper.mapRow(resultSet));
        }
        return ennemis;
    }
}
