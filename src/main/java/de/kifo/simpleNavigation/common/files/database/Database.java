package de.kifo.simpleNavigation.common.files.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Database {

    private final String DATABASE_URL;
    private Connection connection;

    public Database(String pluginName, String fileName) {
        this.DATABASE_URL = "jdbc:sqlite:plugins/" + pluginName + "/" + fileName + ".db";

        try {
            initialize();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        if (isNull(connection) || connection.isClosed()) {
            this.connection = DriverManager.getConnection(DATABASE_URL);
        }

        return this.connection;
    }

    public void closeConnection() {
        if (nonNull(connection)) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void initialize() throws SQLException {
        initializeNaviPlayerTable();
    }

    private void initializeNaviPlayerTable() throws SQLException {
        Connection connection = getConnection();

        PreparedStatement statement = connection
                .prepareStatement("CREATE TABLE IF NOT EXISTS " +
                        "navi_player(uuid varchar(36) primary key, preferred_navigation_type varchar(100))");

        statement.executeUpdate();
        statement.close();
    }
}
