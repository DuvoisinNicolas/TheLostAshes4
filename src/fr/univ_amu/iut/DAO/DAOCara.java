package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.UniqueConnection;
import fr.univ_amu.iut.beans.Caracter;
import fr.univ_amu.iut.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAOCara {

    private Connection connection;
    public DAOCara() throws SQLException {
        connection = UniqueConnection.getInstance().getConnection();
    }

    public void insert (Caracter caracter, User user) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CARACTER (NAME, HP , CURR_HP, FCE, AGI, CHARI, END, MAG, ID_USER) " +
                "VALUES (?,?,?,?,?,?,?,?,?)");
        preparedStatement.setString(1,caracter.getName());
        preparedStatement.setInt(2,100);
        preparedStatement.setInt(3,100);
        preparedStatement.setInt(4,caracter.getFCE());
        preparedStatement.setInt(5,caracter.getAGI());
        preparedStatement.setInt(6,caracter.getCHARI());
        preparedStatement.setInt(7,caracter.getEND());
        preparedStatement.setInt(8,caracter.getMAG());
        preparedStatement.setInt(9,user.getIdUser());
        preparedStatement.executeUpdate();

        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT ID_CARA FROM CARACTER WHERE NAME=? AND HP=? AND CURR_HP=? AND FCE=? AND AGI=? AND CHARI=? AND END=? AND MAG=? AND ID_USER=?");
        preparedStatement1.setString(1,caracter.getName());
        preparedStatement1.setInt(2,100);
        preparedStatement1.setInt(3,100);
        preparedStatement1.setInt(4,caracter.getFCE());
        preparedStatement1.setInt(5,caracter.getAGI());
        preparedStatement1.setInt(6,caracter.getCHARI());
        preparedStatement1.setInt(7,caracter.getEND());
        preparedStatement1.setInt(8,caracter.getMAG());
        preparedStatement1.setInt(9,user.getIdUser());
        ResultSet resultSet = preparedStatement1.executeQuery();
        resultSet.next();
        int idCara = resultSet.getInt("ID_CARA");


        PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO CARACTER (NAME, HP , CURR_HP , FCE, AGI, CHARI, END, MAG, SAVED_ID ,ID_USER) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)");
        preparedStatement2.setString(1,caracter.getName());
        preparedStatement2.setInt(2,100);
        preparedStatement2.setInt(3,100);
        preparedStatement2.setInt(4,caracter.getFCE());
        preparedStatement2.setInt(5,caracter.getAGI());
        preparedStatement2.setInt(6,caracter.getCHARI());
        preparedStatement2.setInt(7,caracter.getEND());
        preparedStatement2.setInt(8,caracter.getMAG());
        preparedStatement2.setInt(9,idCara);
        preparedStatement2.setInt(10,user.getIdUser());
        preparedStatement2.executeUpdate();
    }
}
