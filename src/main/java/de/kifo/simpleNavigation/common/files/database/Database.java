package de.kifo.simpleNavigation.common.files.database;

import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.files.database.entities.NaviPlayer;
import de.kifo.simpleNavigation.common.files.database.entities.NaviPoint;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

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
        initializeNaviPointTable();
    }

    /**
     *  {@link NaviPlayer}
     */

    private void initializeNaviPlayerTable() throws SQLException {
        Connection connection = getConnection();

        PreparedStatement statement = connection
                .prepareStatement("CREATE TABLE IF NOT EXISTS " +
                        "navi_player(uuid varchar(36) primary key, preferred_navigation_type varchar(100))");

        statement.executeUpdate();
        statement.close();
    }

    public void saveNaviPlayer(NaviPlayer naviPlayer) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("INSERT OR REPLACE INTO navi_player (uuid, preferred_navigation_type) VALUES (?, ?)");

            statement.setString(1, naviPlayer.getUuid().toString());
            statement.setString(2, naviPlayer.getPreferredNavigationType().toString());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public NaviPlayer loadNaviPlayer(UUID uuid) {
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("SELECT * FROM navi_player WHERE uuid = ?");

            statement.setString(1, uuid.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new NaviPlayer(uuid, NavigationType.valueOf(resultSet.getString("preferred_navigation_type")));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new NaviPlayer(uuid);
    }

    /**
     *  {@link NaviPoint}
     */

    public void initializeNaviPointTable() throws SQLException {
        Connection connection = getConnection();

        PreparedStatement statement = connection
                .prepareStatement("CREATE TABLE IF NOT EXISTS " +
                        "navi_point(name varchar(100), world varchar(100), x int, y int, z int, playerUuid varchar(36), PRIMARY KEY (name, playerUuid))");

        statement.executeUpdate();
        statement.close();
    }
}
