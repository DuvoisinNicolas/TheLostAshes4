package fr.univ_amu.iut.Mappers;

import fr.univ_amu.iut.beans.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public User mapRow(ResultSet resultSet) throws SQLException {
        User etudiant = new User();
        return etudiant;
    }
}
