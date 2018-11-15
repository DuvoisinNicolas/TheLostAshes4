package fr.univ_amu.iut;

import fr.univ_amu.iut.Exceptions.NoConnectionException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UniqueConnection {
    private Connection connection;
    private static UniqueConnection instance;

    private static final String CONNECT_URL = "jdbc:mysql://mysql-thelostashes4.alwaysdata.net:3306/thelostashes4_db";
    private static final String LOGIN = "170447";
    private static final String PASSWORD = "tla";

    private UniqueConnection() throws NoConnectionException {
        try {
            if (!netIsAvailable()) {
                throw new NoConnectionException() ;
            }
            connection = DriverManager.getConnection(CONNECT_URL, LOGIN, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public static UniqueConnection getInstance () throws NoConnectionException {
        if (!netIsAvailable()) {
            throw new NoConnectionException() ;
        }
        if(instance == null)
            instance = new UniqueConnection();
        return instance;

    }

    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }
}