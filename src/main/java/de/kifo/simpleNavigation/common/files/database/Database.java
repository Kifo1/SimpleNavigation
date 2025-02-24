package de.kifo.simpleNavigation.common.files.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final String DATABASE_URL;

    public Database(String pluginName, String fileName) {
        this.DATABASE_URL = "jdbc:sqlite:plugins/" + pluginName + "/" + fileName + ".db";

        try {
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DATABASE_URL);
        return connection;
    }

    private void initialize() throws SQLException {
        getConnection();
    }
}
