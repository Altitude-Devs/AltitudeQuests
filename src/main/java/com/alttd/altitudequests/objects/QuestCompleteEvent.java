package com.alttd.altitudequests.objects;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class QuestCompleteEvent extends Event{
    private static final HandlerList handlers = new HandlerList();
    final private Player player;
    final private Quest quest;
    final boolean daily;

    public QuestCompleteEvent(Player player, Quest quest, boolean daily) {
        this.player = player;
        this.quest = quest;
        this.daily = daily;
    }

    public Player getPlayer() {
        return player;
    }

    public Quest getQuest() {
        return quest;
    }

    public boolean isDaily() {
        return daily;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
