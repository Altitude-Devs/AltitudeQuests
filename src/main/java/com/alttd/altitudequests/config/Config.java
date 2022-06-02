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

    public static String QUEST_BOOK_AUTHOR = "<magenta>Scruff</magenta>";
    public static String QUEST_BOOK_TITLE = "<green>Quest Title</green>";
    public static List<String> QUEST_PAGES = List.of("""
            <bold><gold>Hey <player></gold></bold>
    
            Here is a quick summary of your quest progress so far!
            * Quest: <quest>
            * Variant: <variant>
            * Items obtained: <step_1_progress>/<step_1_total>
            * Items turned in: <step_2_progress>/<step_2_total>
            
            <click:run_command:/aquest turnin><gold>Click here to turn in more items</gold></click>
            """);
    private static void loadBook() {
        QUEST_BOOK_AUTHOR = config.getString("book.author", QUEST_BOOK_AUTHOR);
        QUEST_BOOK_TITLE = config.getString("book.title", QUEST_BOOK_TITLE);
        QUEST_PAGES = config.getStringList("book.pages", QUEST_PAGES);
    }

    public static String NPC_NAME = "<light_purple>Scruff</light_purple>";
    public static boolean DEBUG = false;
    private static void loadSettings() {
        NPC_NAME = config.getString("settings.npc-name", NPC_NAME);
        DEBUG = config.getBoolean("settings.debug", DEBUG);
    }
}
