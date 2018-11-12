package fr.univ_amu.iut.Mappers;

import fr.univ_amu.iut.beans.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public static User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setIdUser(resultSet.getInt("ID_USER"));
        user.setUsername(resultSet.getString("USERNAME"));
        user.setPassword(resultSet.getString("PWD"));
        user.setMail(resultSet.getString("MAIL"));
        user.setIdCara(resultSet.getInt("ID_CARA"));
        return user;
    }
}
