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
    public static String PROGRESS_BOOK_AUTHOR = "<magenta>Scruff</magenta>";
    public static String PROGRESS_BOOK_TITLE = "<green>Quest Title</green>";
    public static List<String> QUEST_PAGES = List.of("""
            <bold><gold>Hey <player></gold></bold>
    
            Active quest summary:
            * Quest: <quest>
            * Type: <variant>
            * <step_1>: <step_1_progress>/<step_1_total>
            * <step_2>: <step_2_progress>/<step_2_total>
            
            <click:run_command:/aquest turnin><turn_in_text></click>
            """);
    public static List<String> PROGRESS_PAGES = List.of("""
            <bold><gold>Hey <player></gold></bold>
    
            Your quest progress:
            * Quest: <quest>
            * Type: <variant>
            * <step_1>: <step_1_progress>/<step_1_total>
            * <step_2>: <step_2_progress>/<step_2_total>
            
            """);
    private static void loadBook() {
        QUEST_BOOK_AUTHOR = config.getString("book.author", QUEST_BOOK_AUTHOR);
        QUEST_BOOK_TITLE = config.getString("book.title", QUEST_BOOK_TITLE);
        QUEST_PAGES = config.getStringList("book.pages", QUEST_PAGES);
    }

    private static void loadProgressBook() {
        PROGRESS_BOOK_AUTHOR = config.getString("progressBook.author", PROGRESS_BOOK_AUTHOR);
        PROGRESS_BOOK_TITLE = config.getString("progressBook.title", PROGRESS_BOOK_TITLE);
        PROGRESS_PAGES = config.getStringList("progressBook.pages", PROGRESS_PAGES);
    }

    public static String NPC_NAME = "<light_purple>Scruff</light_purple>";
    public static boolean DEBUG = false;
    private static void loadSettings() {
        NPC_NAME = config.getString("settings.npc-name", NPC_NAME);
        DEBUG = config.getBoolean("settings.debug", DEBUG);
    }

    public static int MINE_QUEST_FREQ = 1;
    public static int KILL_QUEST_FREQ = 1;
    public static int COLLECT_QUEST_FREQ = 1;
    public static int BREED_QUEST_FREQ = 1;
    public static int OTHER_QUEST_FRQ = 1;
    private static void loadQuestTypeFrequency() {
        MINE_QUEST_FREQ = config.getInt("quest-type-frequency.mine", MINE_QUEST_FREQ);
        KILL_QUEST_FREQ = config.getInt("quest-type-frequency.kill", KILL_QUEST_FREQ);
        COLLECT_QUEST_FREQ = config.getInt("quest-type-frequency.collect", COLLECT_QUEST_FREQ);
        BREED_QUEST_FREQ = config.getInt("quest-type-frequency.breed", BREED_QUEST_FREQ);
        OTHER_QUEST_FRQ = config.getInt("quest-type-frequency.other", OTHER_QUEST_FRQ);
    }
}
