package com.alttd.altitudequests.config;

import java.io.File;
import java.util.List;

public final class Config extends AbstractConfig {

    static Config config;
    static int version;
    public Config() {
        super(new File(System.getProperty("user.home") + File.separator + "share" + File.separator + "configs" + File.separator + "AltitudeQuests"), "config.yml");
    }
    public static void reload() {
        config = new Config();

        version = config.getInt("config-version", 1);
        config.set("config-version", 1);

        config.readConfig(Config.class, null);
    }

    public static String NO_PERMISSION = "<red>You do not have permission to do that.</red>";
    public static String NO_CONSOLE = "<red>You cannot use this command from console.</red>";
    private static void loadGeneric() {
        NO_PERMISSION = config.getString("generic.no-permission", NO_PERMISSION);
        NO_CONSOLE = config.getString("generic.no-console", NO_CONSOLE);
    }

    public static String QUEST_BOOK_AUTHOR = "<magenta>Scruff</magenta>";
    public static String QUEST_BOOK_TITLE = "<green>Quest Title</green>";
    public static List<String> QUEST_PAGES = List.of("Example");
    private static void loadBook() {
        QUEST_BOOK_AUTHOR = config.getString("book.author", QUEST_BOOK_AUTHOR);
        QUEST_BOOK_TITLE = config.getString("book.title", QUEST_BOOK_TITLE);
        QUEST_PAGES = config.getStringList("book.pages", QUEST_PAGES);
    }

    public static String TOO_FAR_FROM_NPC = "<red>You are too far from Scruff";//TODO replace scruff with <npc>?
    public static String DAILY_ALREADY_DONE = "<red>You already completed your daily quest";
    private static void loadMessages() {
        TOO_FAR_FROM_NPC = config.getString("messages.too-far-from-npc", TOO_FAR_FROM_NPC);
        DAILY_ALREADY_DONE = config.getString("messages.daily-already-done", DAILY_ALREADY_DONE);
    }

    private static void loadGUIText() {
    }

    public static String NPC_NAME = "<light_purple>Scruff</light_purple>";
    public static boolean DEBUG = false;
    private static void loadSettings() {
        NPC_NAME = config.getString("settings.npd-name", NPC_NAME);
        DEBUG = config.getBoolean("settings.debug", DEBUG);
    }
}
