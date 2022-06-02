package com.alttd.altitudequests.config;;

import com.alttd.altitudequests.objects.quests.CollectDropsQuest;
import com.alttd.altitudequests.objects.variants.BreedMobsQuestObject;
import com.alttd.altitudequests.objects.variants.CollectDropsQuestObject;
import com.alttd.altitudequests.objects.variants.KillMobsQuestObject;
import com.alttd.altitudequests.objects.variants.MineQuestObject;
import com.alttd.altitudequests.util.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuestsConfig extends AbstractConfig {
    static QuestsConfig config;

    public QuestsConfig() {
        super(new File(System.getProperty("user.home") + File.separator + "share" + File.separator + "configs"
                + File.separator + "AltitudeQuests"), "quests.yml");
    }

    public static void reload() {
        config = new QuestsConfig();
        config.readConfig(QuestsConfig.class, null);
    }

    public static List<MineQuestObject> MINE_QUESTS = new ArrayList<>();
    public static String MINE_QUEST_NAME = "<green>Mine quest</green>";

    private static void loadMineQuests() {
        MINE_QUESTS.clear();
        ConfigurationSection configurationSection = config.getConfigurationSection("mining.possible_tasks");
        if (configurationSection == null) {
            Logger.warning("No mining quests in config");
            return;
        }
        Set<String> keys = configurationSection.getKeys(false);
        for (String key : keys) {
            try {
                Material material = Material.valueOf(configurationSection.getString(key + ".material"));
                MINE_QUESTS.add(new MineQuestObject(key,
                        configurationSection.getString(key + ".name"),
                        material,
                        configurationSection.getInt(key + ".amount"),
                        configurationSection.getStringList(key + ".quest-pages"),
                        configurationSection.getStringList(key + ".done-pages")));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        MINE_QUEST_NAME = config.getString("mining.name", MINE_QUEST_NAME);
    }

    public static List<KillMobsQuestObject> KILL_MOB_QUEST = new ArrayList<>();
    public static String KILL_MOB_QUEST_NAME = "<green>Kill mobs quest</green>";
    private static void loadKillMobQuests() {
        KILL_MOB_QUEST.clear();
        ConfigurationSection configurationSection = config.getConfigurationSection("kill_mobs.possible_tasks");
        if (configurationSection == null) {
            Logger.warning("No mob kill quests in config");
            return;
        }
        Set<String> keys = configurationSection.getKeys(false);
        for (String key : keys) {
            try {
                EntityType entityType = EntityType.valueOf(configurationSection.getString(key + ".mob"));
                KILL_MOB_QUEST.add(new KillMobsQuestObject(key,
                        configurationSection.getString(key + ".name"),
                        entityType,
                        configurationSection.getInt(key + ".amount"),
                        configurationSection.getStringList(key + ".quest-pages"),
                        configurationSection.getStringList(key + ".done-pages")));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        KILL_MOB_QUEST_NAME = config.getString("kill_mobs.name", KILL_MOB_QUEST_NAME);
    }

    public static List<CollectDropsQuestObject> COLLECT_DROPS_QUEST = new ArrayList<>();
    public static String COLLECT_DROPS_QUEST_NAME = "<green>Collect drops quest</green>";
    private static void loadCollectQuests() {
        COLLECT_DROPS_QUEST.clear();
        ConfigurationSection configurationSection = config.getConfigurationSection("collect_drops.possible_tasks");
        if (configurationSection == null) {
            Logger.warning("No collect drops quests in config");
            return;
        }
        Set<String> keys = configurationSection.getKeys(false);
        for (String key : keys) {
            try {
                Material item = Material.valueOf(configurationSection.getString(key + ".item"));
                COLLECT_DROPS_QUEST.add(new CollectDropsQuestObject(key,
                        configurationSection.getString(key + ".name"),
                        item,
                        configurationSection.getInt(key + ".amount"),
                        configurationSection.getStringList(key + ".quest-pages"),
                        configurationSection.getStringList(key + ".done-pages")));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        COLLECT_DROPS_QUEST_NAME = config.getString("collect_drops.name", COLLECT_DROPS_QUEST_NAME);
    }

    public static List<BreedMobsQuestObject> BREED_MOB_QUEST = new ArrayList<>();
    public static String BREED_MOB_QUEST_NAME = "<green>Breed mobs quest</green>";
    private static void loadBreedMobQuests() {
        BREED_MOB_QUEST.clear();
        ConfigurationSection configurationSection = config.getConfigurationSection("breed_mobs.possible_tasks");
        if (configurationSection == null) {
            Logger.warning("No mob breed quests in config");
            return;
        }
        Set<String> keys = configurationSection.getKeys(false);
        for (String key : keys) {
            try {
                EntityType entityType = EntityType.valueOf(configurationSection.getString(key + ".mob"));
                BREED_MOB_QUEST.add(new BreedMobsQuestObject(key,
                        configurationSection.getString(key + ".name"),
                        entityType,
                        configurationSection.getInt(key + ".amount"),
                        configurationSection.getStringList(key + ".quest-pages"),
                        configurationSection.getStringList(key + ".done-pages")));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        BREED_MOB_QUEST_NAME = config.getString("breed_mobs.name", BREED_MOB_QUEST_NAME);
    }
}
