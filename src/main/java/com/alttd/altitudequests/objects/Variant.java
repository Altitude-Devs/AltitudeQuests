package com.alttd.altitudequests.objects;

import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.util.Logger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.*;

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
        double difficultyOffset = ((rangeMax - rangeMin) / (double) getDaysInMonth());
        int min = Math.max(rangeMin, rangeMin + (int) (difficultyOffset * (questsCompleted - 5)));
        int max = Math.min(rangeMax, rangeMin + (int) (difficultyOffset * (questsCompleted + 5)));
        if (Config.DEBUG)
            Logger.info("variant: %, min: %, max: %", internalName, String.valueOf(min), String.valueOf(max));
        return min == max ? min : new Random().nextInt(min, max);
    }

    private int getDaysInMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
