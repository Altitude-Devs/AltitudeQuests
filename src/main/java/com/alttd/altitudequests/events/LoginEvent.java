package com.alttd.altitudequests.events;

import com.alttd.altitudequests.objects.Quest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginEvent implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Quest.tryLoadDailyQuest(event.getPlayer().getUniqueId());
    }
}
