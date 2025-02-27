package de.kifo.simpleNavigation.common.files.database;

import de.kifo.simpleNavigation.common.enums.NavigationType;
import de.kifo.simpleNavigation.common.files.database.entities.NaviPlayer;
import de.kifo.simpleNavigation.common.files.database.entities.NaviPoint;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
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

    public boolean addNaviPoint(NaviPoint naviPoint) {
        if (nonNull(getNaviPoint(naviPoint.getNaviPointName(), naviPoint.getPlayer()))) {
            return false;
        }

        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("INSERT INTO navi_point (name, world, x, y, z, playerUuid) VALUES (?, ?, ?, ?, ?, ?)");

            statement.setString(1, naviPoint.getNaviPointName().toLowerCase());
            statement.setString(2, naviPoint.getWorldName());
            statement.setInt(3, naviPoint.getX());
            statement.setInt(4, naviPoint.getY());
            statement.setInt(5, naviPoint.getZ());
            statement.setString(6, naviPoint.getPlayer().toString());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Nullable
    public NaviPoint getNaviPoint(String name, UUID playerUuid) {
        name = name.toLowerCase();
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("SELECT * FROM navi_point WHERE name = ? AND playerUuid = ?");
            statement.setString(1, name);
            statement.setString(2, playerUuid.toString());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String worldName = resultSet.getString("world");
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                int z = resultSet.getInt("z");
                return new NaviPoint(name, worldName, x, y, z, playerUuid);
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Set<NaviPoint> getNaviPointsByPlayer(UUID playerUuid) {
        Set<NaviPoint> naviPoints = new HashSet<>();
        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("SELECT * FROM navi_point WHERE playerUuid = ?");
            statement.setString(1, playerUuid.toString());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String worldName = resultSet.getString("world");
                int x = resultSet.getInt("x");
                int y = resultSet.getInt("y");
                int z = resultSet.getInt("z");
                naviPoints.add(new NaviPoint(name, worldName, x, y, z, playerUuid));
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return naviPoints;
    }
}
