package com.alttd.altitudequests.events;

import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.config.LocalConfig;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.stream.Collectors;

public class TalkToQuest implements Listener {

    private static final Book book;
    static {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        book = Book.builder()
                .author(miniMessage.deserialize(Config.QUEST_BOOK_AUTHOR))
                .title(miniMessage.deserialize(Config.QUEST_BOOK_TITLE))
                .pages(Config.QUEST_PAGES.stream()
                        .map(miniMessage::deserialize)
                        .collect(Collectors.toList()))
                .build();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (LocalConfig.activeNPC == null || !LocalConfig.activeNPC.equals(event.getRightClicked().getUniqueId()))
            return;
        event.setCancelled(true);
        event.getPlayer().openBook(book);
        //TODO make it so there can be one book config per quest
        //TODO make it so everything can be done with commands and just don't let them tab complete and do them through the book instead
        //TODO in config allow a multitude of events to be prepared and randomly select from them at certain times of day?
    }
}