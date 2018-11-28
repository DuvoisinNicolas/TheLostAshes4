package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Exceptions.NoConnectionException;
import fr.univ_amu.iut.Exceptions.NoUserException;
import fr.univ_amu.iut.Mappers.CaracterMapper;
import fr.univ_amu.iut.Mappers.UserMapper;
import fr.univ_amu.iut.UniqueConnection;
import fr.univ_amu.iut.beans.Caracter;
import fr.univ_amu.iut.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOCara {

    private Connection connection;
    public static int VALHPMAX = 100;
    public DAOCara() throws NoConnectionException {
        connection = UniqueConnection.getInstance().getConnection();
    }

    public Caracter insert (Caracter caracter, User user) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CARACTER (NAME, HP , CURR_HP, FCE, AGI, CHARI, END, MAG, ID_USER) " +
                "VALUES (?,?,?,?,?,?,?,?,?)");
        preparedStatement.setString(1,caracter.getName());
        preparedStatement.setInt(2,VALHPMAX);
        preparedStatement.setInt(3,VALHPMAX);
        preparedStatement.setInt(4,caracter.getFCE().get());
        preparedStatement.setInt(5,caracter.getAGI().get());
        preparedStatement.setInt(6,caracter.getCHARI().get());
        preparedStatement.setInt(7,caracter.getEND().get());
        preparedStatement.setInt(8,caracter.getMAG().get());
        preparedStatement.setInt(9,user.getIdUser());
        preparedStatement.executeUpdate();

        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT ID_CARA FROM CARACTER WHERE NAME=? AND HP=? AND CURR_HP=? AND FCE=? AND AGI=? AND CHARI=? AND END=? AND MAG=? AND ID_USER=?");
        preparedStatement1.setString(1,caracter.getName());
        preparedStatement1.setInt(2,VALHPMAX);
        preparedStatement1.setInt(3,VALHPMAX);
        preparedStatement1.setInt(4,caracter.getFCE().get());
        preparedStatement1.setInt(5,caracter.getAGI().get());
        preparedStatement1.setInt(6,caracter.getCHARI().get());
        preparedStatement1.setInt(7,caracter.getEND().get());
        preparedStatement1.setInt(8,caracter.getMAG().get());
        preparedStatement1.setInt(9,user.getIdUser());
        ResultSet resultSet = preparedStatement1.executeQuery();
        resultSet.next();
        int idCara = resultSet.getInt("ID_CARA");

        //J'ai pas pu résister
        PreparedStatement preparedStatement666 = connection.prepareStatement("UPDATE USER SET ID_CARA=?");
        preparedStatement666.setInt(1,idCara);
        preparedStatement666.executeUpdate();

        PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO CARACTER (NAME, HP , CURR_HP , FCE, AGI, CHARI, END, MAG, SAVED_ID ,ID_USER) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)");
        preparedStatement2.setString(1,caracter.getName());
        preparedStatement2.setInt(2,VALHPMAX);
        preparedStatement2.setInt(3,VALHPMAX);
        preparedStatement2.setInt(4,caracter.getFCE().get());
        preparedStatement2.setInt(5,caracter.getAGI().get());
        preparedStatement2.setInt(6,caracter.getCHARI().get());
        preparedStatement2.setInt(7,caracter.getEND().get());
        preparedStatement2.setInt(8,caracter.getMAG().get());
        preparedStatement2.setInt(9,idCara);
        preparedStatement2.setInt(10,user.getIdUser());
        preparedStatement2.executeUpdate();

        caracter.setIdCara(idCara);
        caracter.setHP(VALHPMAX);
        caracter.setCURRHP(caracter.getHP().get());
        return caracter;
    }

    public Caracter getMyCara (User user) throws SQLException, NoUserException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CARACTER WHERE ID_CARA = ?");
        preparedStatement.setInt(1,user.getIdCara());
        ResultSet resultSet1 = preparedStatement.executeQuery();
        if (resultSet1.next()) {
            return CaracterMapper.mapRow(resultSet1);
        }
        throw new NoUserException();
    }

    public void save (Caracter caracter,User user) throws NoUserException, SQLException {
        /*
         * TODO : Le checkpoint
         */

        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT ID_CARA FROM CARACTER WHERE ID_USER=?");
        preparedStatement1.setInt(1,user.getIdUser());
        ResultSet resultSet = preparedStatement1.executeQuery();
        resultSet.next();
        int idCara = resultSet.getInt("ID_CARA");



        Caracter savedUser = getSaveUser(caracter,user);
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE CARACTER SET HP=? , CURR_HP=? , FCE=? , AGI=? , CHARI=? , END=? , MAG=? , GOLDS=? WHERE ID_CARA=?");
        preparedStatement.setInt(1,caracter.getHP().get());
        preparedStatement.setInt(2,caracter.getCURRHP().get());
        preparedStatement.setInt(3,caracter.getFCE().get());
        preparedStatement.setInt(4,caracter.getAGI().get());
        preparedStatement.setInt(5,caracter.getCHARI().get());
        preparedStatement.setInt(6,caracter.getEND().get());
        preparedStatement.setInt(7,caracter.getMAG().get());
        preparedStatement.setInt(8,caracter.getGolds().get());
        preparedStatement.setInt(9,savedUser.getIdCara());
        preparedStatement.executeUpdate();
    }

    public Caracter getSaveUser (Caracter caracter , User user) throws SQLException, NoUserException {

        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT ID_CARA FROM CARACTER WHERE ID_USER=?");
        preparedStatement1.setInt(1,user.getIdUser());
        ResultSet resultSet = preparedStatement1.executeQuery();

        resultSet.next();
        int idCara = resultSet.getInt("ID_CARA");


        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CARACTER WHERE SAVED_ID = ?");
        preparedStatement.setInt(1,idCara);
        ResultSet resultSet1 = preparedStatement.executeQuery();
        System.out.println(preparedStatement);
        if (resultSet1.next()) {
            return CaracterMapper.mapRow(resultSet1);
        }
        throw new NoUserException();
    }
}
