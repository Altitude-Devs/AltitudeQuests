package com.alttd.altitudequests.events;

import com.alttd.altitudequests.objects.QuestCompleteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class QuestComplete implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onQuestComplete(QuestCompleteEvent event) {
        event.getPlayer().sendMiniMessage("<green>You completed a quest gj.</green>", null);
    }

}
