package com.alttd.altitudequests.objects;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.List;
import java.util.Random;

public abstract class Variant {

    private final String internalName;
    private final Component name;
    private final int rangeMin;
    private final int rangeMax;
    private final List<String> questPages;
    private final List<String> donePages;

    public Variant(String internalName, String name, List<String> questPages, List<String> donePages, int rangeMin, int rangeMax) {
        this.internalName = internalName;
        this.name = MiniMessage.miniMessage().deserialize(name);
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;
        this.questPages = questPages;
        this.donePages = donePages;
    }

    public String getInternalName() {
        return internalName;
    }

    public Component getName() {
        return name;
    }

    public List<String> getQuestPages() {
        return questPages;
    }

    public List<String> getDonePages() {
        return donePages;
    }

    public int calculateAmount(int questsCompleted) {
        int difficultyOffset = ((rangeMax - rangeMin) / 40) * questsCompleted;
        return new Random().nextInt(Integer.min(rangeMax - 1, rangeMin + difficultyOffset), rangeMax);
    }
}
