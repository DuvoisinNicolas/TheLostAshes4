package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Mappers.UserMapper;
import fr.univ_amu.iut.UniqueConnection;
import fr.univ_amu.iut.beans.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DAOUser {

    private Connection connection;

    public DAOUser() throws SQLException {
        connection = UniqueConnection.getInstance().getConnection();

    }

    public List<User> findAll() {

    }
}
