package com.alttd.altitudequests.objects;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.objects.quests.MineQuest;
import com.alttd.altitudequests.util.Logger;
import com.alttd.altitudequests.util.Utilities;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Quest {

    private static final HashMap<UUID, Quest> dailyQuests = new HashMap<>();
    private static Quest weeklyQuest = null;
    private static final List<Class<? extends Quest>> possibleQuests = new ArrayList<>();

    //TODO add all data every quest needs

    static {
        possibleQuests.add(MineQuest.class);
    }

    public static void createDailyQuest(Player player) {
        if (possibleQuests.size() == 0) {
            player.sendMiniMessage("<red>Unable to create quest, no quests in config</red>", null);
            return;
        }

        Class<? extends Quest> questClass = possibleQuests.get(Utilities.randomOr0(possibleQuests.size() - 1));
        try {
            Constructor<? extends Quest> constructor = questClass.getDeclaredConstructor(UUID.class);
            dailyQuests.put(player.getUniqueId(), constructor.newInstance(player.getUniqueId()));
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            player.sendMiniMessage("<red>Unable to create quest, contact an admin</red>", null);
            e.printStackTrace();
            Logger.severe("% does not have a constructor with a UUID input or has improper access.", questClass.getName());
        }
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

    private static HashSet<UUID> queriedUsers = new HashSet<>();
    public static void tryLoadDailyQuest(UUID uuid) { //TODO set up a way to listen to the response and load stuff
        if (queriedUsers.contains(uuid) || dailyQuests.containsKey(uuid))
            return;
        queriedUsers.add(uuid);
        new BukkitRunnable() {
            @Override
            public void run() {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("try-lock");
                out.writeUTF(uuid.toString());
                Bukkit.getServer().sendPluginMessage(AQuest.getInstance(),
                        "aquest:player-data",
                        out.toByteArray());
                if (Config.DEBUG)
                    Logger.info("Send lock request for %", uuid.toString());
            }
        }.runTaskAsynchronously(AQuest.getInstance());
    }

    public static void unloadUser(UUID uuid) {
        queriedUsers.remove(uuid);
        Quest quest = dailyQuests.remove(uuid);
        if (quest == null)
            return;
        new BukkitRunnable() {
            @Override
            public void run() {
                quest.save();
            }
        }.runTaskAsynchronously(AQuest.getInstance());
    }

    public static void saveAll() {
        for (Quest quest : dailyQuests.values()) {
            quest.save();
        }
    }

    public static void loadDailyQuest(String quest, String quest_variant, int step_1_progress, int step_2_progress, UUID uuid) {
        Optional<Class<? extends Quest>> any = possibleQuests.stream().filter(q -> q.getSimpleName().equals(quest)).findAny();
        if (any.isEmpty()) {
            //TODO error
            return;
        }
        Class<? extends Quest> aClass = any.get();
        Constructor<? extends Quest> constructor;
        try {
            constructor = aClass.getConstructor(String.class, int.class, int.class, UUID.class);
            Quest quest1 = constructor.newInstance(quest_variant, step_1_progress, step_2_progress, uuid);
            dailyQuests.put(uuid, quest1);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public abstract void save();

    public abstract boolean isDone();

    public abstract List<String> getPages();

    public abstract TagResolver getTagResolvers();

    public abstract int turnIn(Player player);
}
