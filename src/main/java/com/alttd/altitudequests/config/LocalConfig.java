package com.alttd.altitudequests.config;

import java.util.UUID;

public class LocalConfig extends AbstractConfig{

    static LocalConfig config;

    public LocalConfig() {
        super("LocalConfig");
    }

    public static void reload() {
        config = new LocalConfig();
        config.readConfig(LocalConfig.class, null);
    }

    public static UUID activeNPC = null;

    private static void loadActiveNPC() {
        String string = config.getString("active-npc", null);
        if (string == null)
            return;
        activeNPC = UUID.fromString(string);
    }

    public static void removeActiveNPC() {
        config.set("active-npc", null);
    }

    public static void setActiveNPC(UUID uuid) {
        config.set("active-npc", uuid.toString());
        activeNPC = uuid;
    }
}
