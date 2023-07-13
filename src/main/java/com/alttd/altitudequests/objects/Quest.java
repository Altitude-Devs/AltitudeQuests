package com.alttd.altitudequests.objects;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.config.MessagesConfig;
import com.alttd.altitudequests.database.Database;
import com.alttd.altitudequests.objects.quests.*;
import com.alttd.altitudequests.util.Logger;
import com.alttd.altitudequests.util.Utilities;
import com.alttd.datalock.DataLockAPI;
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
//    private static Quest weeklyQuest = null;
    private static final List<Class<? extends Quest>> possibleQuests = new ArrayList<>();

    static { // maybe make this make more sense idk
        for (int i = 0; i < Config.MINE_QUEST_FREQ; i++)
            possibleQuests.add(MineQuest.class);
        for (int i = 0; i < Config.KILL_QUEST_FREQ; i++)
            possibleQuests.add(KillMobsQuest.class);
        for (int i = 0; i < Config.COLLECT_QUEST_FREQ; i++)
            possibleQuests.add(CollectDropsQuest.class);
        for (int i = 0; i < Config.BREED_QUEST_FREQ; i++)
            possibleQuests.add(BreedMobsQuest.class);
        for (int i = 0; i < Config.OTHER_QUEST_FRQ; i++)
            possibleQuests.add(OtherQuest.class);
    }

    private final UUID uuid;
    private int step1;
    private int step2;
    private final Variant variant;
    private boolean isDone;
    private boolean rewardReceived;
    private final int amount;

    public static synchronized void putDailyQuest(UUID uuid, Quest quest) {
        dailyQuests.put(uuid, quest);
    }

    public static synchronized Quest removeDailyQuest(UUID uuid) {
        return dailyQuests.remove(uuid);
    }

    public static synchronized Quest getDailyQuest(UUID uuid) {
        return dailyQuests.getOrDefault(uuid, null);
    }

    public static synchronized boolean dailyQuestContainsKey(UUID uuid) {
        return dailyQuests.containsKey(uuid);
    }

    public static synchronized Collection<Quest> getLoadedDailyQuests() {
        return dailyQuests.values();
    }

    public static synchronized void clearDailyQuests() {
        dailyQuests.clear();
    }

    public Quest(UUID uuid, int step1, int step2, Variant variant, int amount, boolean rewardReceived) {
        this.uuid = uuid;
        this.step1 = step1;
        this.step2 = step2;
        this.variant = variant;
        this.isDone = rewardReceived;
        this.rewardReceived = rewardReceived;
        if (variant == null) {
            Logger.warning("Created % quest without a variant for %", this.getClass().getName(), uuid.toString());
        }
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
            putDailyQuest(player.getUniqueId(), constructor.newInstance(player.getUniqueId()));
        } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            player.sendMiniMessage("<red>Unable to create quest, contact an admin</red>", null);
            e.printStackTrace();
            Logger.severe("% does not have a constructor with a UUID input or has improper access.", questClass.getName());
        }
    }

//    public static void setActiveWeeklyQuest(Quest newQuest) {
//        Quest.weeklyQuest = newQuest;
//    }

//    private static final HashSet<UUID> queriedUsers = new HashSet<>();
    public static void tryLoadDailyQuest(UUID uuid) {
//        if (queriedUsers.contains(uuid))
//            return;
        if (dailyQuestContainsKey(uuid))
            return;
//        queriedUsers.add(uuid);
        new BukkitRunnable() {
            @Override
            public void run() {
                DataLockAPI.get().tryLock("aquest:player-data", uuid.toString());
                if (Config.DEBUG)
                    Logger.info("Send lock request for %", uuid.toString());
            }
        }.runTaskAsynchronously(AQuest.getInstance());
    }

    public static void unloadUser(UUID uuid) { //Pls only run async
//        queriedUsers.remove(uuid);
        Quest quest = removeDailyQuest(uuid);
        if (quest == null)
            return;
        quest.save();
    }

    public static void saveAll() {
        for (Quest quest : getLoadedDailyQuests()) {
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
            if (Config.DEBUG) {
                Logger.info("quest: %, uuid: %, step1: %, step2: %, variant: %, amount: %, turnedIn:%",
                        quest,
                        uuid.toString(),
                        String.valueOf(step_1_progress),
                        String.valueOf(step_2_progress),
                        quest_variant,
                        String.valueOf(amount),
                        String.valueOf(turnedIn));
            }
            constructor = aClass.getConstructor(UUID.class, int.class, int.class, String.class, int.class, boolean.class);
            Quest quest1 = constructor.newInstance(uuid, step_1_progress, step_2_progress, quest_variant, amount, turnedIn);
            putDailyQuest(uuid, quest1);
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
                clearDailyQuests();
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
        if (!isDone())
            return;
        final Quest quest = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                saveDone();
                QuestCompleteEvent event = new QuestCompleteEvent(player, quest, true);
                event.callEvent();
            }
        }.runTaskAsynchronously(AQuest.getInstance());
    }

    private void saveDone() {
        try {
            String sql = "INSERT INTO quest_log " +
                    "(uuid, year, month, day) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE uuid = ?";
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(sql);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            String uuidString = uuid.toString();
            statement.setString(1, uuidString);
            statement.setInt(2, calendar.get(Calendar.YEAR));
            statement.setInt(3, calendar.get(Calendar.MONTH));
            statement.setInt(4, calendar.get(Calendar.DAY_OF_MONTH));
            statement.setString(5, uuidString);
            statement.executeUpdate();
            if (Config.DEBUG)
                Logger.info("% finished their quest", uuidString);
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
