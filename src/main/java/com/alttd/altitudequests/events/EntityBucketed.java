package com.alttd.altitudequests.events;

import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.objects.quests.CollectDropsQuest;
import com.alttd.altitudequests.objects.quests.OtherQuest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityBucketed implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBucketFill(PlayerBucketEntityEvent event) {
        Player player = event.getPlayer();
        Quest dailyQuest = Quest.getDailyQuest(player.getUniqueId());
        if (dailyQuest == null || dailyQuest.isDone())
            return;
        if (dailyQuest instanceof OtherQuest otherQuest) {
            ItemStack itemStack = event.getEntityBucket();
            Entity entity = event.getEntity();
            if (itemStack != null && entity != null)
                otherQuest.bucket(itemStack, entity);
        }
    }

}
