package com.alttd.altitudequests.database;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.DatabaseConfig;
import com.alttd.altitudequests.util.Logger;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static Database instance = null;
    private Connection connection = null;

    private Database() {}

    public static Database getDatabase(){
        if (instance == null)
        {
            instance = new Database();
            instance.init();
        }
        return (instance);
    }

    protected void init() {
        try {
            openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Run all create table functions
        for (Method method : Database.class.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw new RuntimeException(ex.getCause());
                    } catch (Exception ex) {
                        Logger.severe("Error invoking %.", method.toString());
                        ex.printStackTrace();
                    }
                }
            }
        }
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

    public Connection getConnection() {
        try {
            openConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static void createUserPointsTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS generic_quest_progress(" +
                    "year_day INT NOT NULL, " +
                    "uuid VARCHAR(36) NOT NULL, " +
                    "quest VARCHAR(36) NOT NULL, " +
                    "quest_variant VARCHAR(36) NOT NULL, " +
                    "step_1_progress INT NOT NULL, " +
                    "step_2_progress INT NOT NULL, " +
                    "amount INT NOT NULL, " +
                    "reward_received BIT(1) NOT NULL, " +
                    "PRIMARY KEY (uuid)" +
                    ")";
            getDatabase().getConnection().prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.severe("Error while trying to create user point table");
            Logger.severe("Shutting down AltitudeQuests");
            Bukkit.getPluginManager().disablePlugin(AQuest.getInstance());
        }
    }

    private static void createQuestLogTable() {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS quest_log(" +
                    "uuid VARCHAR(36) NOT NULL, " +
                    "year INT NOT NULL, " +
                    "month INT NOT NULL, " +
                    "day INT NOT NULL, " +
                    "PRIMARY KEY (uuid, year, month, day)" +
                    ")";
            getDatabase().getConnection().prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.severe("Error while trying to create quest log table");
            Logger.severe("Shutting down AltitudeQuests");
            Bukkit.getPluginManager().disablePlugin(AQuest.getInstance());
        }
    }

}
