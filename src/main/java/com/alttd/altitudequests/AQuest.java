package com.alttd.altitudequests;

import com.alttd.altitudequests.commands.CommandManager;
import com.alttd.altitudequests.config.*;
import com.alttd.altitudequests.events.*;
import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.util.Logger;
import com.alttd.altitudequests.util.Utilities;
import com.alttd.datalock.DataLockAPI;
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
        DataLockAPI dataLockAPI = DataLockAPI.get();
        if (dataLockAPI == null) {
            Logger.severe("Unable to load datalockapi");
        } else if (dataLockAPI.isActiveChannel("aquest:player-data")) {
            Logger.warning("Unable to register aquest channel");
        } else {
            dataLockAPI.registerChannel("aquest:player-data");
        }

        Logger.info("--------------------------------------------------");
        Logger.info("AQuest started");
        Logger.info("--------------------------------------------------");
    }

    @Override
    public void onDisable() {
        if (Config.DEBUG)
            Logger.info("Syncing users.");
        Quest.saveAll();
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
        getServer().getPluginManager().registerEvents(new EntityDeath(), this);
        getServer().getPluginManager().registerEvents(new EntitySheared(), this);
        getServer().getPluginManager().registerEvents(new EntityBucketed(), this);
        getServer().getPluginManager().registerEvents(new EntityBreed(), this);
        getServer().getPluginManager().registerEvents(new DonNotMessWithNPC(), this);
        getServer().getPluginManager().registerEvents(new DataLock(), this);
        getServer().getPluginManager().registerEvents(new ItemCaught(), this);
//        getServer().getMessenger().registerOutgoingPluginChannel(this, "aquest:player-data");
//        getServer().getMessenger().registerIncomingPluginChannel(this, "aquest:player-data", new PluginMessageListener());
    }

    private static int yearDay = Utilities.getYearDay();
    private void scheduleTasks() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (Config.DEBUG)
                    Logger.info("Syncing users.");
                Quest.saveAll();
            }
        }.runTaskTimerAsynchronously(getInstance(), 10 * 60 * 20L, 10 * 60 * 20L); //every 10 minutes

        new BukkitRunnable() {
            @Override
            public void run() {
                int tmp = Utilities.getYearDay();
                if (tmp == yearDay)
                    return;
                yearDay = tmp;
                Quest.resetQuests();
            }
        }.runTaskTimerAsynchronously(getInstance(), 100, 100); //every 5 seconds
    }
}
