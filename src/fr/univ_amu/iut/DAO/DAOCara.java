package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Exceptions.NoConnectionException;
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

        DAOArmor daoArmor = new DAOArmor();
        daoArmor.obtainArmor(caracter.getArmor(),caracter);
        daoArmor.equipArmor(caracter.getArmor(),caracter);
        /**
         * TODO: Insérer l'armure et les objets
         */
        return caracter;
    }

    public Caracter getMyCara (User user) throws SQLException, NoWeaponFoundException, NoConnectionException {
        // Récupération du personnage
        Caracter caracter = new Caracter();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CARACTER WHERE ID_USER = ?");
        preparedStatement.setInt(1,user.getIdUser());
        ResultSet resultSet1 = preparedStatement.executeQuery();
        if (resultSet1.next()) {
            caracter = CaracterMapper.mapRow(resultSet1);
        }

        // Récupération de l'arme équipée
        PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT ID_WEAPON FROM WEAPONINVENTORY WHERE ID_CARA = ? AND EQUIPED=?");
        preparedStatement1.setInt(1,caracter.getIdCara());
        preparedStatement1.setInt(2,1);
        ResultSet resultSet2 = preparedStatement1.executeQuery();
        if (resultSet2.next()) {
            caracter.setWeapon(Weapon.findWeaponById(resultSet2.getInt("ID_WEAPON")));
        }

        // Récupération des armes de l'inventaire
        ArrayList<Weapon> weapons = new ArrayList<>();
        PreparedStatement preparedStatement2 = connection.prepareStatement("SELECT ID_WEAPON FROM WEAPONINVENTORY WHERE ID_CARA = ?");
        preparedStatement2.setInt(1,caracter.getIdCara());
        ResultSet resultSet3 = preparedStatement2.executeQuery();
        while (resultSet3.next()) {
            weapons.add(Weapon.findWeaponById(resultSet2.getInt("ID_WEAPON")));
        }
        caracter.setWeapons(weapons);

        // Récupération des sorts appris du type
        DAOSpell daoSpell = new DAOSpell();
        caracter.setSpells(daoSpell.findByCara(caracter));

        return caracter;

        /**
         * TODO: Ajouter l'initalisation de l'armure et compagnie
         */
    }

    public boolean hasACara (User user) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM CARACTER WHERE ID_USER = ?");
        preparedStatement.setInt(1,user.getIdUser());
        ResultSet resultSet1 = preparedStatement.executeQuery();
        return resultSet1.next();
    }

    public void save (Caracter caracter) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE CARACTER SET HP=? , CURR_HP=? , FCE=? , AGI=? , CHARI=? , END=? , MAG=? , GOLDS=? WHERE ID_CARA=?");
        preparedStatement.setInt(1,caracter.getHP().get());
        preparedStatement.setInt(2,caracter.getCURRHP().get());
        preparedStatement.setInt(3,caracter.getFCE().get());
        preparedStatement.setInt(4,caracter.getAGI().get());
        preparedStatement.setInt(5,caracter.getCHARI().get());
        preparedStatement.setInt(6,caracter.getEND().get());
        preparedStatement.setInt(7,caracter.getMAG().get());
        preparedStatement.setInt(8,caracter.getGolds().get());
        preparedStatement.setInt(9,caracter.getIdCara());
        preparedStatement.executeUpdate();
    }

}
