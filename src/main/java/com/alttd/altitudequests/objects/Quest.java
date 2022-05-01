package com.alttd.altitudequests.objects;

import java.util.HashMap;
import java.util.UUID;

public abstract class Quest {

    private static Quest dailyQuest = null;
    private static final HashMap<UUID, Quest> dailyQuests = new HashMap<>();
    private static Quest weeklyQuest = null;

    private String name;
    private GoalType goalType;
    //TODO add all data every quest needs


    public Quest(String name, GoalType goalType) {
        this.name = name;
        this.goalType = goalType;
    }

    public static Quest getDailyQuest(UUID uuid) {
        if (!dailyQuests.containsKey(uuid))
            dailyQuests.put(uuid, dailyQuest.initQuest());
        return dailyQuests.get(uuid);
    }

    public static void setActiveDailyQuest(Quest newQuest) {
        Quest.dailyQuest = newQuest;
    }
    public static void setActiveWeeklyQuest(Quest newQuest) {
        Quest.weeklyQuest = newQuest;
    }

    public abstract boolean isDone();

    public abstract Quest initQuest();
}
