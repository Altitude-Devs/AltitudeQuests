package com.alttd.altitudequests.events;

import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.objects.quests.BreedMobsQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

public class EntityBreed implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityBreed(EntityBreedEvent event) {
        if (!(event.getBreeder() instanceof Player player))
            return;
        Quest dailyQuest = Quest.getDailyQuest(player.getUniqueId());
        if (dailyQuest == null || dailyQuest.isDone())
            return;
        if (dailyQuest instanceof BreedMobsQuest breedMobsQuest) {
            breedMobsQuest.breed(event.getMother());
        }
    }

}
