package fr.univ_amu.iut.DAO;

import fr.univ_amu.iut.Mappers.SpellMapper;
import fr.univ_amu.iut.UniqueConnection;
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


    public DAOSpell() throws SQLException {
        connection = UniqueConnection.getInstance().getConnection();
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
