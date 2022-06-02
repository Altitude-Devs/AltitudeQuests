package com.alttd.altitudequests.events;

import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.objects.quests.CollectDropsQuest;
import com.alttd.altitudequests.objects.quests.KillMobsQuest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player player = entity.getKiller();
        if (player == null)
            return;
        Quest dailyQuest = Quest.getDailyQuest(player.getUniqueId());
        if (dailyQuest == null || dailyQuest.isDone())
            return;
        if (dailyQuest instanceof KillMobsQuest killMobsQuest) {
            killMobsQuest.kill(entity);
        } else if (dailyQuest instanceof CollectDropsQuest collectDropsQuest) {
            collectDropsQuest.collectDrops(event.getDrops());
        }
    }

}
