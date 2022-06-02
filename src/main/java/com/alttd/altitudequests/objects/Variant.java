package com.alttd.altitudequests.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;

public abstract class Variant {

    private final String internalName;
    private final Component name;
    private final int amount;
    private final List<String> questPages;
    private final List<String> donePages;

    public Variant(String internalName, String name, int amount, List<String> questPages, List<String> donePages) {
        this.internalName = internalName;
        this.name = MiniMessage.miniMessage().deserialize(name);
        this.amount = amount;
        this.questPages = questPages;
        this.donePages = donePages;
    }

    public String getInternalName() {
        return internalName;
    }

    public Component getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public List<String> getQuestPages() {
        return questPages;
    }

    public List<String> getDonePages() {
        return donePages;
    }
}
