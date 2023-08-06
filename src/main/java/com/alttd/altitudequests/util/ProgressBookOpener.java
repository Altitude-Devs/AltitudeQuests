package com.alttd.altitudequests.util;

import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.objects.Quest;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProgressBookOpener {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static void openProgressBook(Player player) {
        player.openBook(getProgressBook(player));
    }

    private static Book getProgressBook (Player player) {
        return Book.builder()
                .author(miniMessage.deserialize(Config.PROGRESS_BOOK_AUTHOR))
                .title(miniMessage.deserialize(Config.PROGRESS_BOOK_TITLE))
                .pages(getPages(player))
                .build();
    }

    private static final Component error = MiniMessage.miniMessage().deserialize("<red>Error retrieving quest data</red>");
    private static List<Component> getPages(Player player) {
        Quest dailyQuest = Quest.getDailyQuest(player.getUniqueId());
        if (dailyQuest == null)
            return List.of(error);
        TagResolver tagResolver = TagResolver.resolver(
                TagResolver.resolver(Placeholder.component("player", player.name())),
                TagResolver.resolver(Placeholder.component("quest", dailyQuest.getDisplayName())),
                TagResolver.resolver(Placeholder.component("variant", dailyQuest.getVariant().getName())),
                dailyQuest.getTagResolvers()
        );
        List<String> pages = new ArrayList<>();
        pages.addAll(Config.PROGRESS_PAGES);
        return (pages.stream()
                .map(page -> miniMessage.deserialize(page, tagResolver))
                .collect(Collectors.toList()));
    }
}