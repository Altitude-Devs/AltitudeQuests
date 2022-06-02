package com.alttd.altitudequests.events;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.LocalConfig;
import com.alttd.altitudequests.util.BookOpener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TalkToQuest implements Listener {

    private static final Set<UUID> inProcess = new HashSet<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (LocalConfig.activeNPC == null || !LocalConfig.activeNPC.equals(event.getRightClicked().getUniqueId()))
            return;
        event.setCancelled(true);
        final Player player = event.getPlayer();
        final UUID uniqueId = player.getUniqueId();
        if (inProcess.contains(uniqueId))
            return;

        inProcess.add(uniqueId);
        new BukkitRunnable() {
            @Override
            public void run() {
                BookOpener.openBook(player);
                inProcess.remove(uniqueId);
            }
        }.runTaskAsynchronously(AQuest.getInstance());
    }
}