package com.alttd.altitudequests.objects;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.config.MessagesConfig;
import com.alttd.altitudequests.database.Database;
import com.alttd.altitudequests.objects.quests.BreedMobsQuest;
import com.alttd.altitudequests.objects.quests.CollectDropsQuest;
import com.alttd.altitudequests.objects.quests.KillMobsQuest;
import com.alttd.altitudequests.objects.quests.MineQuest;
import com.alttd.altitudequests.util.Logger;
import com.alttd.altitudequests.util.Utilities;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Quest {

    private static final HashMap<UUID, Quest> dailyQuests = new HashMap<>();
    private static Quest weeklyQuest = null;
    private static final List<Class<? extends Quest>> possibleQuests = new ArrayList<>();

    static {
        possibleQuests.add(MineQuest.class);
        possibleQuests.add(KillMobsQuest.class);
        possibleQuests.add(CollectDropsQuest.class);
        possibleQuests.add(BreedMobsQuest.class);
    }

    private final UUID uuid;
    private int step1;
    private int step2;
    private final Variant variant;
    private boolean isDone;
    private boolean rewardReceived;
    private final int amount;

    public Quest(UUID uuid, int step1, int step2, Variant variant, int amount, boolean rewardReceived) {
        this.uuid = uuid;
        this.step1 = step1;
        this.step2 = step2;
        this.variant = variant;
        this.isDone = rewardReceived;
        this.rewardReceived = rewardReceived;
        if (variant != null && amount == -1)
            this.amount = variant.calculateAmount(loadQuestsDoneThisMonth(uuid));
        else
            this.amount = amount;
    }

    private int loadQuestsDoneThisMonth(UUID uuid) {
        String sql = "SELECT COUNT(uuid) AS total FROM quest_log WHERE uuid = ? AND year = ? AND month = ?";
        try {
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(sql);
            statement.setString(1, uuid.toString());
            Calendar instance = Calendar.getInstance();
            instance.setTime(new Date());
            statement.setInt(2, instance.get(Calendar.YEAR));
            statement.setInt(3, instance.get(Calendar.MONTH));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
                return resultSet.getInt("total");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return 0;
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
    public static void tryLoadDailyQuest(UUID uuid) {
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

    public static void unloadUser(UUID uuid) { //Pls only run async
        queriedUsers.remove(uuid);
        Quest quest = dailyQuests.remove(uuid);
        if (quest == null)
            return;
        quest.save();
    }

    public static void saveAll() {
        for (Quest quest : dailyQuests.values()) {
            quest.save();
        }
    }

    public static boolean loadDailyQuest(String quest, String quest_variant, int step_1_progress, int step_2_progress, UUID uuid, int amount, boolean turnedIn) {
        Optional<Class<? extends Quest>> any = possibleQuests.stream()
                .filter(q -> q.getSimpleName().equals(quest))
                .findAny();
        if (any.isEmpty()) {
            Logger.warning("Unable to find % quest giving up loading the quest for %", quest, uuid.toString());
            return false;
        }
        Class<? extends Quest> aClass = any.get();
        Constructor<? extends Quest> constructor;
        try {
            constructor = aClass.getConstructor(UUID.class, int.class, int.class, String.class, int.class, boolean.class);
            Quest quest1 = constructor.newInstance(uuid, step_1_progress, step_2_progress, quest_variant, amount, turnedIn);
            dailyQuests.put(uuid, quest1);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Collection<String> getTypes() {
        return possibleQuests.stream().map(Class::getSimpleName).collect(Collectors.toList());
    }

    public static void resetQuests() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getServer().sendMessage(MiniMessage.miniMessage().deserialize(MessagesConfig.RESETTING_QUESTS));
                dailyQuests.clear();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    createDailyQuest(player);
                }
            }
        }.runTaskAsynchronously(AQuest.getInstance());
    }

    public abstract void save();

    public abstract TagResolver getTagResolvers();

    public abstract int turnIn(Player player);

    public abstract Component getDisplayName();

    public abstract List<String> getRewardCommand();

    public List<String> getQuestPages() {
        return variant.getQuestPages();
    }

    public List<String> getDonePages() {
        return variant.getDonePages();
    }

    protected void checkDone() {
        if (isDone())
            return;
        if (getStep1() == getAmount() && getStep2() == getAmount()) {
            setDone(true);
        }
    }

    public void checkDone(Player player) {
        checkDone();
        if (!isDone)
            return;
        saveDone();
        QuestCompleteEvent event = new QuestCompleteEvent(player, this, true);
        event.callEvent();
    }

    private void saveDone() {
        try {
            String sql = "INSERT INTO quest_log " +
                    "(uuid, year, month, day) " +
                    "VALUES (?, ?, ?, ?)";
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(sql);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            statement.setString(1, uuid.toString());
            statement.setInt(2, calendar.get(Calendar.YEAR));
            statement.setInt(3, calendar.get(Calendar.MONTH));
            statement.setInt(4, calendar.get(Calendar.DAY_OF_MONTH));
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.severe("Error while trying to create quest log table");
            Logger.severe("Shutting down AltitudeQuests");
            Bukkit.getPluginManager().disablePlugin(AQuest.getInstance());
        }
    }

    public Variant getVariant() {
        return variant;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getStep1() {
        return step1;
    }

    public void addStep1(int step1) {
        this.step1 += step1;
    }

    public int getStep2() {
        return step2;
    }

    public void addStep2(int step2) {
        this.step2 += step2;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isDone() {
        return isDone;
    }

    public boolean isRewardReceived() {
        return rewardReceived;
    }

    public void setRewardReceived(boolean rewardReceived) {
        this.rewardReceived = rewardReceived;
    }

    public int getMaxToTurnIn() {
        return Math.min(getAmount() - getStep2(), getStep1() - getStep2());
    }

    public int getAmount() {
        return amount;
    }
}
