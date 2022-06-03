package com.alttd.altitudequests.objects;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class QuestCompleteEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    final private Player player;
    final private Variant variant;
    final private int amount;
    final boolean daily;

    public QuestCompleteEvent(Player player, Quest quest, boolean daily) {
        this.player = player;
        this.variant = quest.getVariant();
        this.amount = quest.getAmount();
        this.daily = daily;
    }

    public Player getPlayer() {
        return player;
    }

    public Variant getVariant() {
        return variant;
    }

    public int getAmount() {
        return amount;
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
