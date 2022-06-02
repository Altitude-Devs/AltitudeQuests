package com.alttd.altitudequests.events;

import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.objects.quests.CollectDropsQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.util.List;

public class EntitySheared implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityShear(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        Quest dailyQuest = Quest.getDailyQuest(player.getUniqueId());
        if (dailyQuest == null || dailyQuest.isDone())
            return;
        if (dailyQuest instanceof CollectDropsQuest collectDropsQuest) {
            collectDropsQuest.collectDrops(List.of(event.getItem()));
        }
    }
}
