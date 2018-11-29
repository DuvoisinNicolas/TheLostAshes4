package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Exceptions.NoConnectionException;
import fr.univ_amu.iut.Mappers.SpellMapper;
import fr.univ_amu.iut.UniqueConnection;
import fr.univ_amu.iut.beans.Caracter;
import fr.univ_amu.iut.beans.Spell;
import fr.univ_amu.iut.beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOSpell {

    private Connection connection;


    public DAOSpell() throws SQLException, NoConnectionException {
        connection = UniqueConnection.getInstance().getConnection();
    }

    public void learnSpell (Spell spell , Caracter caracter) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO LEARNEDSPELLS (ID_SPELL, ID_CARA) VALUES (?,?)");
        preparedStatement.setInt(1,spell.getIdSpell());
        preparedStatement.setInt(2,caracter.getIdCara());
        int rset = preparedStatement.executeUpdate();
        if (rset != 1) {
            throw new SQLException("Problème de requete");
        }
    }

    public void resetSpells (Caracter caracter) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE LEARNEDSPELLS SET USED=? WHERE ID_CARA=?");
        preparedStatement.setInt(1,0);
        preparedStatement.setInt(2,caracter.getIdCara());
        preparedStatement.executeUpdate();
    }

    public List<Spell> findByUser(User user) throws SQLException {
        ArrayList<Integer> idSpells = new ArrayList<>();
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID_SPELL FROM LEARNEDSPELLS WHERE ID_CARA = ?");
        preparedStatement.setInt(1,user.getIdUser());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
             idSpells.add(resultSet.getInt("ID_SPELL"));
        }

        List<Spell> list = new ArrayList<>();
        for (int idSpell : idSpells) {
            PreparedStatement preparedStatement1 = connection.prepareStatement("SELECT * FROM SPELL WHERE ID_SPELL = ?");
            preparedStatement.setInt(1,idSpell);
            ResultSet resultSet1 = preparedStatement1.executeQuery();
            while (resultSet1.next()) {
                list.add(SpellMapper.mapRow(resultSet1));
            }
        }
        return list;
    }
    public List<Spell> findAll() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM SPELL");
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Spell> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(SpellMapper.mapRow(resultSet));
        }
        return list;
    }
}
