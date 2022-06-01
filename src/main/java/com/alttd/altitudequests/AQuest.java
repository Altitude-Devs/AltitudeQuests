package com.alttd.altitudequests;

import com.alttd.altitudequests.commands.CommandManager;
import com.alttd.altitudequests.config.*;
import com.alttd.altitudequests.events.*;
import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.util.Logger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
        reloadConfigs();
        registerEvents();
        new CommandManager();
        scheduleTasks();

        Logger.info("--------------------------------------------------");
        Logger.info("AQuest started");
        Logger.info("--------------------------------------------------");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void reloadConfigs() {
        Config.reload();
        DatabaseConfig.reload();
        MessagesConfig.reload();
        LocalConfig.reload();
        QuestsConfig.reload();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new TalkToQuest(), this);
        getServer().getPluginManager().registerEvents(new MineBlocks(), this);
        getServer().getPluginManager().registerEvents(new LoginEvent(), this);
        getServer().getPluginManager().registerEvents(new LogoutEvent(), this);
        getServer().getPluginManager().registerEvents(new QuestComplete(), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "aquest:player-data");
        getServer().getMessenger().registerIncomingPluginChannel(this, "aquest:player-data", new PluginMessageListener());
    }

    private void scheduleTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Config.DEBUG)
                    Logger.info("Syncing users.");
                Quest.saveAll();
            }
        }.runTaskTimerAsynchronously(getInstance(), 10 * 60 * 20L, 10 * 60 * 20L);
    }
}
