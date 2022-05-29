package com.alttd.altitudequests.util;

import com.alttd.altitudequests.AQuest;

public class Logger {

    static private final java.util.logging.Logger logger;

    static {
        logger = AQuest.getInstance().getLogger();
    }

    public static void info(String info, String... variables)
    {
        for (String variable : variables) {
            info = info.replaceFirst("%", variable);
        }
        logger.info(info);
    }

    public static void warning(String warning, String... variables)
    {
        for (String variable : variables) {
            warning = warning.replaceFirst("%", variable);
        }
        logger.warning(warning);
    }

    public static void severe(String severe, String... variables)
    {
        for (String variable : variables) {
            severe = severe.replaceFirst("%", variable);
        }
        logger.severe(severe);
    }
}
