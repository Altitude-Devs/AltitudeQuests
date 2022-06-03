package com.alttd.altitudequests.events;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.database.Database;
import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.util.Logger;
import com.alttd.altitudequests.util.Utilities;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                    return;
                }
                UUID uuid = UUID.fromString(in.readUTF());
                if (Config.DEBUG)
                    Logger.info("Received positive log result for %, loading user", uuid.toString());
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
                    return;
                }
                UUID uuid = UUID.fromString(in.readUTF());
                if (Config.DEBUG)
                    Logger.info("Received positive log result for %, loading user", uuid.toString());
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
                String sql = "SELECT * FROM generic_quest_progress WHERE uuid = ?";
                try {
                    PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(sql);
                    statement.setString(1, uuid.toString());
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next() && resultSet.getInt("year_day") == Utilities.getYearDay()) {
                        if (Quest.loadDailyQuest(
                                resultSet.getString("quest"),
                                resultSet.getString("quest_variant"),
                                resultSet.getInt("step_1_progress"),
                                resultSet.getInt("step_2_progress"),
                                uuid,
                                resultSet.getInt("reward_received") == 1)) {
                            if (Config.DEBUG)
                                Logger.info("Loading daily quest for %", uuid.toString());
                            return;
                        } else
                            Logger.warning("Unable to load quest for %, creating new quest...", uuid.toString());
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
                if (Config.DEBUG)
                    Logger.info("Creating new daily quest for %", uuid.toString());
                Quest.createDailyQuest(Bukkit.getPlayer(uuid));
            }
        }.runTaskAsynchronously(AQuest.getInstance());
    }
}
