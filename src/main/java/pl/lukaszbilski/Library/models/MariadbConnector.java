package pl.lukaszbilski.Library.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariadbConnector {

    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    private static final String DB_URL = "jdbc:mariadb://217.61.109.83/test";
    //  Database credentials
    private static final String USER = "myroot";
    private static final String PASS = "Pozdro!2";

    private Connection connection;
    private static MariadbConnector connector = new MariadbConnector();

    private MariadbConnector(){
        connect();
    }

    public static MariadbConnector getInstance(){
        return connector;
    }

    private void connect(){
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

