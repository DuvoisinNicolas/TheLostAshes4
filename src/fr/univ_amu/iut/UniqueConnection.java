package fr.univ_amu.iut;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UniqueConnection {
    private Connection connection;
    private static UniqueConnection instance;

    private static final String CONNECT_URL = "jdbc:mysql://mysql-thelostashes4.alwaysdata.net:3306/thelostashes4_db";
    private static final String LOGIN = "170447";
    private static final String PASSWORD = "tla";

    private UniqueConnection() {
        try {
            connection = DriverManager.getConnection(CONNECT_URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public static UniqueConnection getInstance () {
        if(instance == null)
            instance = new UniqueConnection();
        return instance;

    }
}