package com.alttd.altitudequests;

import com.alttd.altitudequests.commands.CommandManager;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.config.DatabaseConfig;
import com.alttd.altitudequests.config.LocalConfig;
import com.alttd.altitudequests.config.MessagesConfig;
import com.alttd.altitudequests.events.TalkToQuest;
import com.alttd.altitudequests.util.Logger;
import org.bukkit.plugin.java.JavaPlugin;

public final class AQuest extends JavaPlugin {

    public static AQuest instance;

    public static AQuest getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        Config.reload();
        DatabaseConfig.reload();
        MessagesConfig.reload();
        LocalConfig.reload();

        registerEvents();
        CommandManager commandManager = new CommandManager();

        Logger.info("--------------------------------------------------");
        Logger.info("AQuest started");
        Logger.info("--------------------------------------------------");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new TalkToQuest(), this);
    }
}
