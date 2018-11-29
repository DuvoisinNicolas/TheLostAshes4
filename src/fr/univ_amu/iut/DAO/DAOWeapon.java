package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Exceptions.NoConnectionException;
import fr.univ_amu.iut.Mappers.WeaponMapper;
import fr.univ_amu.iut.UniqueConnection;
import fr.univ_amu.iut.beans.Caracter;
import fr.univ_amu.iut.beans.Weapon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOWeapon {

    private Connection connection;

    public DAOWeapon() throws NoConnectionException {
        connection = UniqueConnection.getInstance().getConnection();
    }

    public Weapon getWeaponById (int idWeapon) throws SQLException {
        Weapon weapon = new Weapon();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM WEAPON WHERE ID_WEAPON=?");
        preparedStatement.setInt(1,idWeapon);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            weapon = WeaponMapper.mapRow(resultSet);
        }
        return weapon;
    }

    public Weapon getEquipedWeapon(Caracter caracter) throws SQLException {
        Weapon weapon = new Weapon();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM WEAPONINVENTORY WHERE ID_CARA=? AND EQUIPED = 0");
        preparedStatement.setInt(1,caracter.getIdCara());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            weapon = getWeaponById(resultSet.getInt("ID_WEAPON"));
        }
        return weapon;
    }

    public List<Weapon> getMyWeapons (Caracter caracter) throws SQLException {
        List<Weapon> weapons = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM WEAPONINVENTORY WHERE ID_CARA=?");
        preparedStatement.setInt(1,caracter.getIdCara());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            weapons.add(getWeaponById(resultSet.getInt("ID_WEAPON")));
        }
        return weapons;
    }

    public List<Weapon> findAll () throws SQLException {
        List<Weapon> weapons = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM WEAPON");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            weapons.add(WeaponMapper.mapRow(resultSet));
        }
        return weapons;
    }

    /**
     * Probablement useless
     */
    /*
    public void obtainWeapon (Weapon weapon,Caracter caracter) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO WEAPONINVENTORY (ID_CARA, ID_WEAPON) VALUES (?,?)");
        preparedStatement.setInt(1, caracter.getIdCara());
        preparedStatement.setInt(2, weapon.getIdWeapon());
        preparedStatement.executeUpdate();
    }

    public void equipWeapon (Weapon weapon, Caracter caracter) throws SQLException {
        PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE WEAPONINVENTORY SET EQUIPED=0 WHERE ID_CARA=?");
        preparedStatement1.setInt(1,caracter.getIdCara());
        preparedStatement1.executeUpdate();

        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE WEAPONINVENTORY SET EQUIPED=1 WHERE ID_CARA=? AND ID_WEAPON=?");
        preparedStatement.setInt(1,caracter.getIdCara());
        preparedStatement.setInt(2,weapon.getIdWeapon());
        preparedStatement.executeUpdate();
    }

*/

}
