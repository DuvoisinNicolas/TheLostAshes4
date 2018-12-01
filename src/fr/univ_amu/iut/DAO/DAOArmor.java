package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Exceptions.NoConnectionException;
import fr.univ_amu.iut.Mappers.ArmorMapper;
import fr.univ_amu.iut.UniqueConnection;
import fr.univ_amu.iut.beans.Armor;
import fr.univ_amu.iut.beans.Caracter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOArmor {

    private Connection connection;

    public DAOArmor() throws NoConnectionException {
        connection = UniqueConnection.getInstance().getConnection();
    }

    public Armor getArmorById(int idArmor) throws SQLException {
        Armor armor = new Armor();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ARMOR WHERE ID_ARMOR=?");
        preparedStatement.setInt(1, idArmor);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            armor = ArmorMapper.mapRow(resultSet);
        }
        return armor;
    }

    public Armor getEquipedArmor(Caracter caracter) throws SQLException {
        Armor armor = new Armor();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ARMORINVENTORY WHERE ID_CARA=? AND EQUIPED=1");
        preparedStatement.setInt(1, caracter.getIdCara());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            armor = getArmorById(resultSet.getInt("ID_ARMOR"));
        }
        return armor;
    }

    public List<Armor> getMyArmors(Caracter caracter) throws SQLException {
        List<Armor> armors = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ARMORINVENTORY WHERE ID_CARA=?");
        preparedStatement.setInt(1, caracter.getIdCara());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            armors.add(getArmorById(resultSet.getInt("ID_ARMOR")));
        }
        return armors;
    }

    public ArrayList<Armor> findAll() throws SQLException {
        ArrayList<Armor> armors = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ARMOR");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            armors.add(ArmorMapper.mapRow(resultSet));
        }
        return armors;
    }

    /**
     * Probablement useless
     */

    public void obtainArmor(Armor armor, Caracter caracter) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ARMORINVENTORY (ID_CARA, ID_ARMOR) VALUES (?,?)");
        preparedStatement.setInt(1, caracter.getIdCara());
        preparedStatement.setInt(2, armor.getIdArmor());
        preparedStatement.executeUpdate();
    }

    public void equipArmor(Armor armor, Caracter caracter) throws SQLException {
        PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE ARMORINVENTORY SET EQUIPED=0 WHERE ID_CARA=?");
        preparedStatement1.setInt(1, caracter.getIdCara());
        preparedStatement1.executeUpdate();

        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ARMORINVENTORY SET EQUIPED=1 WHERE ID_CARA=? AND ID_ARMOR=?");
        preparedStatement.setInt(1, caracter.getIdCara());
        preparedStatement.setInt(2, armor.getIdArmor());
        preparedStatement.executeUpdate();
    }


}
