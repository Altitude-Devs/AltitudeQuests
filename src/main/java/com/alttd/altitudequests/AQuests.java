package com.alttd.altitudequests;

import org.bukkit.plugin.java.JavaPlugin;

public final class AQuests extends JavaPlugin {

    public static AQuests instance;

    public static AQuests getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
