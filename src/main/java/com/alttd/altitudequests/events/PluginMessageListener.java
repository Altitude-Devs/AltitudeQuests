package com.alttd.altitudequests.events;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.util.Logger;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PluginMessageListener implements org.bukkit.plugin.messaging.PluginMessageListener {

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
        if (!channel.equals("aquest:player-data")) {
            Logger.warning("Received plugin message on invalid channel");
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        switch (in.readUTF()) {
            case "try-lock-result" -> {
                if (!in.readBoolean()) {
                    Logger.warning("Unable to lock row");
                    return; //TODO handle
                }
                UUID uuid = UUID.fromString(in.readUTF());
                if (Config.DEBUG)
                    Logger.warning("Received positive log result for %, loading user", uuid.toString());
                loadUser(uuid);
            }
            case "queue-lock-failed" -> Logger.warning("Encountered uuid that was locked and had a lock queued: %, lock is from %", in.readUTF(), in.readUTF());
            case "try-unlock-result" -> {
                if (in.readBoolean()) {
                    // ignore?
                    return;
                }
                Logger.severe("Unable to unlock %.", in.readUTF());
            }
            case "locked-queue-lock" -> {
                if (!in.readBoolean()) {
                    Logger.warning("Got false back from locked queue lock");
                    return; //TODO handle
                }
                UUID uuid = UUID.fromString(in.readUTF());
                if (Config.DEBUG)
                    Logger.warning("Received positive log result for %, loading user", uuid.toString());
                loadUser(uuid);
            }
            case "check-lock-result" -> {

            }
        }
    }

    private void loadUser(UUID uuid) {
        new BukkitRunnable() {
            @Override
            public void run() {
                //TODO load user from database
                //TODO if no quest in database create one
                System.out.println("creating quest");
                Quest.createDailyQuest(Bukkit.getPlayer(uuid));
            }
        }.runTaskAsynchronously(AQuest.getInstance());
    }
}
