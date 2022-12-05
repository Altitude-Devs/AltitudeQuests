package com.alttd.altitudequests.util;

import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.database.Database;
import com.alttd.altitudequests.objects.Quest;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class LoadUser extends BukkitRunnable {

    UUID uuid;

    public LoadUser(UUID uuid) {
        this.uuid = uuid;
    }

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
                        resultSet.getInt("amount"),
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
}
