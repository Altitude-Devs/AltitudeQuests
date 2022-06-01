package com.alttd.altitudequests.events;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.config.LocalConfig;
import com.alttd.altitudequests.objects.Quest;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
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
                player.openBook(getBook(event));
                inProcess.remove(uniqueId);
            }
        }.runTaskAsynchronously(AQuest.getInstance());
        //TODO make it so everything can be done with commands and just don't let them tab complete and do them through the book instead
        //TODO in config allow a multitude of events to be prepared and randomly select from them at certain times of day?
    }

    private Book getBook (PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        return Book.builder()
                .author(miniMessage.deserialize(Config.QUEST_BOOK_AUTHOR))
                .title(miniMessage.deserialize(Config.QUEST_BOOK_TITLE))
                .pages(getPages(player))
                .build();
    }

    private static final Component error = MiniMessage.miniMessage().deserialize("<red>Error retrieving quest data</red>");
    private List<Component> getPages(Player player) {
        Quest dailyQuest = Quest.getDailyQuest(player.getUniqueId());
        if (dailyQuest == null)
            return List.of(error);
        TagResolver tagResolver = TagResolver.resolver(
                TagResolver.resolver(Placeholder.component("player", player.name())),
                TagResolver.resolver(Placeholder.parsed("br", "\n")),
                dailyQuest.getTagResolvers()
        );
        //TODO add weekly quest?
        List<Component> pages = dailyQuest.getPages().stream()
                .map(page -> miniMessage.deserialize(page, tagResolver))
                .collect(Collectors.toList());
        return (pages);
    }
}