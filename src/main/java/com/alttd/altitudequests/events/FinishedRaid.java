package com.alttd.altitudequests.events;

import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.objects.quests.OtherQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.raid.RaidFinishEvent;

import java.util.List;
import java.util.UUID;

public class FinishedRaid implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRaidFinish(RaidFinishEvent event) {
        List<Player> winners = event.getWinners();
        for (Player player : winners) {
            UUID uuid = player.getUniqueId();
            Quest dailyQuest = Quest.getDailyQuest(uuid);
            if (dailyQuest == null || dailyQuest.isDone()) {
                return;
            }
            if (dailyQuest instanceof OtherQuest otherQuest) {
                otherQuest.raid();
            }
        }
    }
}
