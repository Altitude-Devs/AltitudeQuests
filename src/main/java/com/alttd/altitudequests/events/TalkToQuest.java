package com.alttd.altitudequests.events;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.config.LocalConfig;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TalkToQuest implements Listener {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    private static final Set<UUID> tmp = new HashSet<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (LocalConfig.activeNPC == null || !LocalConfig.activeNPC.equals(event.getRightClicked().getUniqueId()))
            return;
        event.setCancelled(true);
        if (!tmp.remove(event.getPlayer().getUniqueId())) {
            tmp.add(event.getPlayer().getUniqueId());
            return;
        }
        event.getPlayer().sendMiniMessage("Interacted with scruff - DEBUG", null);
        new BukkitRunnable() {
            @Override
            public void run() {
                event.getPlayer().openBook(getBook(event));
            }
        }.runTaskTimerAsynchronously(AQuest.getInstance(), 0, 0);
        //TODO make it so there can be one book config per quest
        //TODO make it so everything can be done with commands and just don't let them tab complete and do them through the book instead
        //TODO in config allow a multitude of events to be prepared and randomly select from them at certain times of day?
    }

    private Book getBook (PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        return Book.builder()
                .author(miniMessage.deserialize(Config.QUEST_BOOK_AUTHOR))
                .title(miniMessage.deserialize(Config.QUEST_BOOK_TITLE))
                .pages(getPages(player).stream()
                        .map(page -> miniMessage.deserialize(page, TagResolver.resolver(Placeholder.component("player", player.name()))))
                        .collect(Collectors.toList()))
                .build();
    }

    private List<String> getPages(Player player) {
        return (Config.QUEST_PAGES);
    }
}