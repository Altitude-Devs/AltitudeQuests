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

public class BookOpener {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static void openBook(Player player) {
        player.openBook(getBook(player));
    }

    private static Book getBook (Player player) {
        return Book.builder()
                .author(miniMessage.deserialize(Config.QUEST_BOOK_AUTHOR))
                .title(miniMessage.deserialize(Config.QUEST_BOOK_TITLE))
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
        //TODO add weekly quest?
        List<String> pages = new ArrayList<>(Config.QUEST_PAGES);
        if (dailyQuest.isDone())
            pages.addAll(dailyQuest.getDonePages());
        else
            pages.addAll(dailyQuest.getQuestPages());
        return (pages.stream()
                .map(page -> miniMessage.deserialize(page, tagResolver))
                .collect(Collectors.toList()));
    }
}
