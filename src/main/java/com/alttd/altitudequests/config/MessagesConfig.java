package com.alttd.altitudequests.config;

import java.io.File;

public class MessagesConfig extends AbstractConfig{

    static MessagesConfig config;

    public MessagesConfig() {
        super(new File(System.getProperty("user.home") + File.separator + "share" + File.separator + "configs"
                + File.separator + "AltitudeQuests"), "messages.yml");
    }

    public static void reload() {
        config = new MessagesConfig();
        config.readConfig(MessagesConfig.class, null);
    }

    public static String HELP_MESSAGE_WRAPPER = "<gold>AltitudeQuests help:\n<commands></gold>";
    public static String HELP_MESSAGE = "<green>Show this menu: <gold>/aquest help</gold></green>";
    public static String RELOAD_HELP_MESSAGE = "<green>Reload configs: <gold>/aquest reload</gold></green>";
    public static String CREATE_SCRUFF_MESSAGE = "<green>Create Scruff: <gold>/aquest createscruff <x> <y> <z> <yaw> <pitch> <world></gold></green>";
    public static String SET_QUEST_HELP = "<green>Set quest: <gold>/aquest setquest <player> <quest> <variant></gold></green>";

    private static void loadHelp() {
        HELP_MESSAGE_WRAPPER = config.getString("help.help-wrapper", HELP_MESSAGE_WRAPPER);
        HELP_MESSAGE = config.getString("help.help", HELP_MESSAGE);
        RELOAD_HELP_MESSAGE = config.getString("help.reload", RELOAD_HELP_MESSAGE);
        SET_QUEST_HELP = config.getString("help.set-quest", SET_QUEST_HELP);
        CREATE_SCRUFF_MESSAGE = config.getString("help.help-wrapper", CREATE_SCRUFF_MESSAGE);
    }


    public static String TOO_FAR_FROM_NPC = "<red>You are too far from <npc></red>";
    public static String DAILY_ALREADY_DONE = "<red>You already completed your daily quest";
    public static String RESETTING_QUESTS = "<white>[<gold>Mascot</gold>] <light_purple>Scruff</light_purple><gray>:</gray> <green>Thank you everyone that completed their daily quest! I will be handing out new ones now so come visit me at <gold>/spawn</gold>!</green></white>";
    public static String REWARD_ALREADY_RECEIVED = "<red>You already collected this reward</red>";
    public static String REWARD_SENT = "<green>Thank you for completing the quest! Your reward has been sent!";
    private static void loadMessages() {
        TOO_FAR_FROM_NPC = config.getString("messages.too-far-from-npc", TOO_FAR_FROM_NPC);
        DAILY_ALREADY_DONE = config.getString("messages.daily-already-done", DAILY_ALREADY_DONE);
        RESETTING_QUESTS = config.getString("messages.resetting-quests", RESETTING_QUESTS);
        REWARD_ALREADY_RECEIVED = config.getString("messages.reward-already-received", REWARD_ALREADY_RECEIVED);
        REWARD_SENT = config.getString("messages.reward-send", REWARD_SENT);
    }

    public static String NO_PERMISSION = "<red>You do not have permission to do that.</red>";
    public static String NO_CONSOLE = "<red>You cannot use this command from console.</red>";
    private static void loadGeneric() {
        NO_PERMISSION = config.getString("generic.no-permission", NO_PERMISSION);
        NO_CONSOLE = config.getString("generic.no-console", NO_CONSOLE);
    }
}
