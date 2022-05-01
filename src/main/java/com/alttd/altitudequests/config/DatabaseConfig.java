package com.alttd.altitudequests.config;

import java.io.File;

public class DatabaseConfig extends AbstractConfig {

    static DatabaseConfig config;
    public DatabaseConfig() {
        super(new File(System.getProperty("user.home") + File.separator + "share" + File.separator + "configs"
                + File.separator + "AltitudeQuests"), "database.yml");
    }

    public static void reload() {
        config = new DatabaseConfig();
        config.readConfig(DatabaseConfig.class, null);
    }

    public static String DRIVER = "mysql";
    public static String IP = "localhost";
    public static String PORT = "3306";
    public static String DATABASE_NAME = "AltitudeQuests";
    public static String USERNAME = "root";
    public static String PASSWORD = "root";

    private static void loadDatabase() {
        DRIVER = config.getString("database.driver", DRIVER);
        IP = config.getString("database.ip", IP);
        PORT = config.getString("database.port", PORT);
        DATABASE_NAME = config.getString("database.name", DATABASE_NAME);
        USERNAME = config.getString("database.username", USERNAME);
        PASSWORD = config.getString("database.password", PASSWORD);
    }

}
