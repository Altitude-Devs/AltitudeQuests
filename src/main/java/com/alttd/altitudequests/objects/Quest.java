package com.alttd.altitudequests.objects;

import com.alttd.altitudequests.config.QuestsConfig;
import com.alttd.altitudequests.objects.quests.MineQuest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public abstract class Quest {

    private static final HashMap<UUID, Quest> dailyQuests = new HashMap<>();
    private static Quest weeklyQuest = null;
    private static final String[] possibleQuests;

    //TODO add all data every quest needs

    static {
        possibleQuests = new String[]{"MineQuest"};
    }

    public Quest() {
    }

    public static void createDailyQuest(Player player) {
        Random random = new Random();
        String questName = possibleQuests[random.nextInt(0, possibleQuests.length - 1)];
        Quest quest = null;
        switch (questName) {
            case "MineQuest" -> {
                quest = new MineQuest(QuestsConfig.MINE_QUESTS.get(random.nextInt(0, QuestsConfig.MINE_QUESTS.size() - 1)));
            }
        }
        if (quest == null)
            return; //TODO error
        dailyQuests.put(player.getUniqueId(), quest);
    }

    public static Quest getDailyQuest(UUID uuid) {
        if (!dailyQuests.containsKey(uuid)) {
           return null;
        }
        return dailyQuests.get(uuid);
    }

    public static void setActiveWeeklyQuest(Quest newQuest) {
        Quest.weeklyQuest = newQuest;
    }

    public abstract boolean isDone();
}
