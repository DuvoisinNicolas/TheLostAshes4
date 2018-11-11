package fr.univ_amu.iut.Mappers;

import fr.univ_amu.iut.beans.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public User mapRow(ResultSet resultSet) throws SQLException {
        User etudiant = new User();
        etudiant.setNumEt(resultSet.getInt("NUM_ET"));
        etudiant.setNomEt(resultSet.getString("NOM_ET"));
        etudiant.setPrenomEt(resultSet.getString("PRENOM_ET"));
        etudiant.setCpEt(resultSet.getString("CP_ET"));
        etudiant.setVilleEt(resultSet.getString("VILLE_ET"));
        etudiant.setAnnee(resultSet.getInt("ANNEE"));
        etudiant.setGroupe(resultSet.getInt("GROUPE"));
        return etudiant;
    }
}
