package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Exceptions.NoConnectionException;
import fr.univ_amu.iut.Exceptions.NoUserException;
import fr.univ_amu.iut.Mappers.UserMapper;
import fr.univ_amu.iut.Password;
import fr.univ_amu.iut.UniqueConnection;
import fr.univ_amu.iut.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOUser {

    private Connection connection;


    public DAOUser() throws SQLException, NoConnectionException {
        connection = UniqueConnection.getInstance().getConnection();

    }

    public Boolean checkifNotExistsUsernameAndMail (String pseudo) throws Exception {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USER WHERE USERNAME = ?");
        preparedStatement.setString(1,pseudo);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
                return false;
        }
        return true;
    }

    public User insert (User user) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USER (USERNAME, PWD, MAIL) VALUES (?,?,?)");
        preparedStatement.setString(1,user.getUsername());
        preparedStatement.setString(2,Password.getSaltedHash(user.getPassword()));
        preparedStatement.setString(3,user.getMail());
        preparedStatement.executeUpdate();
        return user;
    }


   public User findByUsernameAndPwd (String pseudo, String pwd) throws Exception {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USER WHERE USERNAME = ?");
        preparedStatement.setString(1,pseudo);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            if (Password.check(pwd, resultSet.getString("PWD"))) {
                return UserMapper.mapRow(resultSet);
            }
        }

        throw new NoUserException();
   }

    public List<User> findAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USER");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(UserMapper.mapRow(resultSet));
        }
        return list;
    }

}
