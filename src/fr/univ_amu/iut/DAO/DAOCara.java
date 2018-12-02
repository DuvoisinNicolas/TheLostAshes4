package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Exceptions.NoConnectionException;
import fr.univ_amu.iut.Exceptions.NoMapFoundException;
import fr.univ_amu.iut.Exceptions.NoUserException;
import fr.univ_amu.iut.Exceptions.NoWeaponFoundException;
import fr.univ_amu.iut.Main;
import fr.univ_amu.iut.Mappers.CaracterMapper;
import fr.univ_amu.iut.Mappers.UserMapper;
import fr.univ_amu.iut.UniqueConnection;
import fr.univ_amu.iut.beans.Caracter;
import fr.univ_amu.iut.beans.Spell;
import fr.univ_amu.iut.beans.User;
import fr.univ_amu.iut.beans.Weapon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DAOCara {

    private Connection connection;
    public static int VALHPMAX = 100;
    public DAOCara() throws NoConnectionException {
        connection = UniqueConnection.getInstance().getConnection();
    }

    public Caracter insert (Caracter caracter, User user) throws Exception {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO CARACTER (NAME, HP , CURR_HP, FCE, AGI, CHARI, END, MAG, ID_USER,CURR_MAP) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)");
        preparedStatement.setString(1,caracter.getName());
        preparedStatement.setInt(2,VALHPMAX);
        preparedStatement.setInt(3,VALHPMAX);
        preparedStatement.setInt(4,caracter.getFCE().get());
        preparedStatement.setInt(5,caracter.getAGI().get());
        preparedStatement.setInt(6,caracter.getCHARI().get());
        preparedStatement.setInt(7,caracter.getEND().get());
        preparedStatement.setInt(8,caracter.getMAG().get());
        preparedStatement.setInt(9,user.getIdUser());
        preparedStatement.setInt(10,caracter.getCurrentMap().getIdMap());
        System.out.println(preparedStatement);
        preparedStatement.executeUpdate();

        PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT * FROM CARACTER WHERE ID_USER=?");
        preparedStatement2.setInt(1,user.getIdUser());
        ResultSet resultSet = preparedStatement2.executeQuery();
        if (resultSet.next()) {
            caracter.setIdCara(resultSet.getInt("ID_CARA"));
        }

        // Ajout des sorts
        DAOSpell daoSpell = new DAOSpell();
        for (Spell spell : caracter.getSpells()) {
            daoSpell.learnSpell(spell,caracter);
        }

        // Ajout de l'arme
        DAOWeapon daoWeapon = new DAOWeapon();
        daoWeapon.obtainWeapon(caracter.getWeapon(),caracter);
        daoWeapon.equipWeapon(caracter.getWeapon(),caracter);

        // Ajout de l'armure
        DAOArmor daoArmor = new DAOArmor();
        daoArmor.obtainArmor(caracter.getArmor(),caracter);
        daoArmor.equipArmor(caracter.getArmor(),caracter);

        return caracter;
    }

    public Caracter getMyCara (User user) throws SQLException, NoMapFoundException {
        // Récupération du personnage
        Caracter caracter = new Caracter();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CARACTER WHERE ID_USER = ?");
        preparedStatement.setInt(1,user.getIdUser());
        ResultSet resultSet1 = preparedStatement.executeQuery();
        if (resultSet1.next()) {
            caracter = CaracterMapper.mapRow(resultSet1);
        }

        return caracter;

    }

    public boolean hasACara (User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CARACTER WHERE ID_USER = ?");
        preparedStatement.setInt(1,user.getIdUser());
        ResultSet resultSet1 = preparedStatement.executeQuery();
        return resultSet1.next();
    }

    public void save (Caracter caracter) throws SQLException {
        //Actualisation du caracter
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE CARACTER SET HP=?, CURR_HP=?, FCE=?, AGI=?, CHARI=?, END=?, MAG=?, GOLDS=?, CURR_MAP =? WHERE ID_CARA=?");
        preparedStatement.setInt(1,caracter.getHP().get());
        preparedStatement.setInt(2,caracter.getCURRHP().get());
        preparedStatement.setInt(3,caracter.getFCE().get());
        preparedStatement.setInt(4,caracter.getAGI().get());
        preparedStatement.setInt(5,caracter.getCHARI().get());
        preparedStatement.setInt(6,caracter.getEND().get());
        preparedStatement.setInt(7,caracter.getMAG().get());
        preparedStatement.setInt(8,caracter.getGolds().get());
        preparedStatement.setInt(9,caracter.getIdCara());
        preparedStatement.setInt(10,caracter.getCurrentMap().getIdMap());
        preparedStatement.executeUpdate();

        // Suppression des anciennes armes
        PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM WEAPONINVENTORY WHERE ID_CARA = ?");
        preparedStatement1.setInt(1,caracter.getIdCara());
        preparedStatement1.executeUpdate();

        //Ajout des armes
        for (Weapon weapon : caracter.getWeapons()) {
            PreparedStatement preparedStatement2 = connection.prepareStatement("INSERT INTO WEAPONINVENTORY (ID_CARA, ID_WEAPON, EQUIPED) VALUES (?,?,?)");
            preparedStatement2.setInt(1, caracter.getIdCara());
            preparedStatement2.setInt(2, weapon.getIdWeapon());
            preparedStatement2.setBoolean(3, caracter.getWeapon() == weapon);
            //preparedStatement2.executeUpdate();
        }


        /*
         * TODO : Sauvegarder aussiles armures , les items , et la map actuelle
         */
    }
}
