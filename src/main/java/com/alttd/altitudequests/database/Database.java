package com.alttd.altitudequests.database;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.DatabaseConfig;
import com.alttd.altitudequests.util.Logger;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Database instance = null;
    public static Connection connection = null;

    private Database() {

    }

    public static Database getDatabase(){
        if (instance == null)
            instance = new Database();
        return (instance);
    }

    public void init() {
        try {
            openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Tables
        createUserPointsTable();
    }

    /**
     * Opens the connection if it's not already open.
     * @throws SQLException If it can't create the connection.
     */
    private void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + DatabaseConfig.IP + ":" + DatabaseConfig.PORT + "/" + DatabaseConfig.DATABASE_NAME +
                            "?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true",
                    DatabaseConfig.USERNAME, DatabaseConfig.PASSWORD);
        }
    }

    private static void createUserPointsTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS quest_progress(" +
                    "uuid VARCHAR(36) NOT NULL, " +
                    "quest VARCHAR(36) NOT NULL, " +
                    "prepare_progress INT NOT NULL, " +
                    "turn_in_progress INT NOT NULL, " +
                    "PRIMARY KEY (UUID, quest)" +
                    ")";
            connection.prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.severe("Error while trying to create user point table");
            Logger.severe("Shutting down AltitudeQuests");
            Bukkit.getPluginManager().disablePlugin(AQuest.getInstance());
        }
    }

}
