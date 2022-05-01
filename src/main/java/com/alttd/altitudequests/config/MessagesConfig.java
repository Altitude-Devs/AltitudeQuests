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

    private static void loadHelp() {
        HELP_MESSAGE_WRAPPER = config.getString("help.help-wrapper", HELP_MESSAGE_WRAPPER);
        HELP_MESSAGE = config.getString("help.help", HELP_MESSAGE);
        RELOAD_HELP_MESSAGE = config.getString("help.reload", RELOAD_HELP_MESSAGE);
        CREATE_SCRUFF_MESSAGE = config.getString("help.help-wrapper", CREATE_SCRUFF_MESSAGE);
    }


    private static void loadCommandMessages() {

    }
}
