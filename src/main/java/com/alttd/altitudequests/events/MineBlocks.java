package com.alttd.altitudequests.events;

import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.objects.quests.MineQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class MineBlocks implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        Quest quest = Quest.getDailyQuest(uuid);
        if (quest == null || quest.isDone())
            return;
        if (quest instanceof MineQuest mineQuest)
            mineQuest.mine(event.getBlock());
    }
}
