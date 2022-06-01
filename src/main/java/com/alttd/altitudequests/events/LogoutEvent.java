package com.alttd.altitudequests.events;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.util.Logger;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class LogoutEvent implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (Config.DEBUG)
                    Logger.info("Syncing %", event.getPlayer().getName());
                Quest dailyQuest = Quest.getDailyQuest(uuid);
                if (dailyQuest != null)
                    dailyQuest.save();
                Quest.unloadUser(uuid);
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("try-unlock");
                out.writeUTF(uuid.toString());
                Bukkit.getServer().sendPluginMessage(AQuest.getInstance(),
                        "aquest:player-data",
                        out.toByteArray());
            }
        }.runTaskAsynchronously(AQuest.getInstance());
    }
}
