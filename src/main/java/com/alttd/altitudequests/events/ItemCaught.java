package com.alttd.altitudequests.events;

import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.objects.quests.OtherQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class ItemCaught implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemCaught(PlayerFishEvent event) {
        if (event.getCaught() == null || event.getCaught().getType() != EntityType.DROPPED_ITEM) {
            return;                                                                                    //Only if an item was caught do further processing. Otherwise error might come up later
        }
        Player player = event.getPlayer();
        Quest dailyQuest = Quest.getDailyQuest(player.getUniqueId());
        if (dailyQuest == null || dailyQuest.isDone()) {
            return;
        }
        if (dailyQuest instanceof OtherQuest otherQuest) {
            Item item = (Item) event.getCaught();
            ItemStack caughtItem = item.getItemStack();
            otherQuest.fish(caughtItem);
        }
    }
}
