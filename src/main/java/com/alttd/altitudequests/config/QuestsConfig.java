package com.alttd.altitudequests.config;;

import com.alttd.altitudequests.objects.variants.BreedMobsQuestObject;
import com.alttd.altitudequests.objects.variants.CollectDropsQuestObject;
import com.alttd.altitudequests.objects.variants.KillMobsQuestObject;
import com.alttd.altitudequests.objects.variants.MineQuestObject;
import com.alttd.altitudequests.objects.variants.OtherQuestObject;
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
    public static String MINE_QUEST_NAME = "<green>Mining</green>";
    public static String MINE_STEP_1 = "Mined";
    public static String MINE_STEP_2 = "Turned in";
    public static String MINE_TURN_IN = "<gold>Click here to turn in your <block></gold>";
    public static List<String> MINE_COMMANDS = List.of("broadcast <player> Finished their daily quest!");
    public static Material material;
    public static Material turnInMaterial;

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
                material = Material.valueOf(configurationSection.getString(key + ".material"));
                if (configurationSection.getString(key + ".turnInMaterial") == null) {
                    turnInMaterial = material;
                }
                else {
                    turnInMaterial = Material.valueOf(configurationSection.getString(key + ".turnInMaterial"));
                }

                MINE_QUESTS.add(new MineQuestObject(key,
                        configurationSection.getString(key + ".name"),
                        material,
                        turnInMaterial,
                        configurationSection.getStringList(key + ".quest-pages"),
                        configurationSection.getStringList(key + ".done-pages"),
                        configurationSection.getInt(key + ".amount-min"),
                        configurationSection.getInt(key + ".amount-max")));
                if (Config.DEBUG)
                    Logger.info("Loaded Mine quest " + key);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        MINE_QUEST_NAME = config.getString("mining.name", MINE_QUEST_NAME);
        MINE_STEP_1 = config.getString("mining.step-1", MINE_STEP_1);
        MINE_STEP_2 = config.getString("mining.step-2", MINE_STEP_2);
        MINE_TURN_IN = config.getString("mining.turn-in", MINE_TURN_IN);
        MINE_COMMANDS = config.getStringList("mining.commands", MINE_COMMANDS);
    }

    public static List<KillMobsQuestObject> KILL_MOB_QUEST = new ArrayList<>();
    public static String KILL_MOB_QUEST_NAME = "<green>Kill mobs</green>";
    public static String KILL_MOB_STEP_1 = "Killed";
    public static String KILL_MOB_STEP_2 = "Confirmed";
    public static String KILL_MOB_TURN_IN = "<gold>Click here to confirm the killed <mob>s</gold>";
    public static List<String> KILL_MOB_COMMANDS = List.of("broadcast <player> Finished their daily quest!");

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
                        configurationSection.getStringList(key + ".quest-pages"),
                        configurationSection.getStringList(key + ".done-pages"),
                        configurationSection.getInt(key + ".amount-min"),
                        configurationSection.getInt(key + ".amount-max")));
                if (Config.DEBUG)
                    Logger.info("Loaded Kill mob quest " + key);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        KILL_MOB_QUEST_NAME = config.getString("kill_mobs.name", KILL_MOB_QUEST_NAME);
        KILL_MOB_STEP_1 = config.getString("kill_mobs.step-1", KILL_MOB_STEP_1);
        KILL_MOB_STEP_2 = config.getString("kill_mobs.step-2", KILL_MOB_STEP_2);
        KILL_MOB_TURN_IN = config.getString("kill_mobs.turn-in", KILL_MOB_TURN_IN);
        KILL_MOB_COMMANDS = config.getStringList("kill_mobs.commands", KILL_MOB_COMMANDS);
    }

    public static List<CollectDropsQuestObject> COLLECT_DROPS_QUEST = new ArrayList<>();
    public static String COLLECT_DROPS_QUEST_NAME = "<green>Collect drops quest</green>";
    public static String COLLECT_DROPS_STEP_1 = "Obtained";
    public static String COLLECT_DROPS_STEP_2 = "Turned in";
    public static String COLLECT_DROPS_TURN_IN = "<gold>Click here to turn in your <item></gold>";
    public static List<String> COLLECT_DROPS_COMMANDS = List.of("broadcast <player> Finished their daily quest!");

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
                        configurationSection.getStringList(key + ".quest-pages"),
                        configurationSection.getStringList(key + ".done-pages"),
                        configurationSection.getInt(key + ".amount-min"),
                        configurationSection.getInt(key + ".amount-max")));
                if (Config.DEBUG)
                    Logger.info("Loaded Collect drops quest " + key);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        COLLECT_DROPS_QUEST_NAME = config.getString("collect_drops.name", COLLECT_DROPS_QUEST_NAME);
        COLLECT_DROPS_STEP_1 = config.getString("collect_drops.step-1", COLLECT_DROPS_STEP_1);
        COLLECT_DROPS_STEP_2 = config.getString("collect_drops.step-2", COLLECT_DROPS_STEP_2);
        COLLECT_DROPS_TURN_IN = config.getString("collect_drops.turn-in", COLLECT_DROPS_TURN_IN);
        COLLECT_DROPS_COMMANDS = config.getStringList("collect_drops.commands", COLLECT_DROPS_COMMANDS);
    }

    public static List<OtherQuestObject> OTHER_QUEST = new ArrayList<>();
    public static String OTHER_QUEST_NAME = "<green>Other quest</green>";
    public static String OTHER_TURN_IN = "<gold>Click here to turn in your <item></gold>";
    public static List<String> OTHER_COMMANDS = List.of("broadcast <player> Finished their daily quest!");
    public static  Material item;
    public static  EntityType entityType;

    private static void loadOtherQuests() {
        OTHER_QUEST.clear();
        ConfigurationSection configurationSection = config.getConfigurationSection("other.possible_tasks");
        if (configurationSection == null) {
            Logger.warning("No other quests in config");
            return;
        }
        Set<String> keys = configurationSection.getKeys(false);
        for (String key : keys) {
            try {
                if (configurationSection.getString(key + ".item") == null) {
                    item = null;
                }
                else {
                    item = Material.valueOf(configurationSection.getString(key + ".item"));
                }
                if (configurationSection.getString(key + ".mob") == null) {
                    entityType = null;
                }
                else {
                    entityType = EntityType.valueOf(configurationSection.getString(key + ".mob"));
                }
                OTHER_QUEST.add(new OtherQuestObject(key,
                        configurationSection.getString(key + ".name"),
                        configurationSection.getString(key + ".category"),
                        item,
                        entityType,
                        configurationSection.getStringList(key + ".quest-pages"),
                        configurationSection.getStringList(key + ".done-pages"),
                        configurationSection.getInt(key + ".amount-min"),
                        configurationSection.getInt(key + ".amount-max"),
                        configurationSection.getString(key + ".step-1"),
                        configurationSection.getString(key + ".step-2")));
                if (Config.DEBUG)
                    Logger.info("Loaded Collect drops quest " + key);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        OTHER_QUEST_NAME = config.getString("other.name", OTHER_QUEST_NAME);
        //OTHER_STEP_1 = config.getString("other.step-1", OTHER_STEP_1);
        //OTHER_STEP_2 = config.getString("other.step-2", OTHER_STEP_2);
        OTHER_TURN_IN = config.getString("other.turn-in", OTHER_TURN_IN);
        OTHER_COMMANDS = config.getStringList("other.commands", OTHER_COMMANDS);
    }

    public static List<BreedMobsQuestObject> BREED_MOB_QUEST = new ArrayList<>();
    public static String BREED_MOB_QUEST_NAME = "<green>Breed mobs quest</green>";
    public static String BREED_STEP_1 = "Bred";
    public static String BREED_STEP_2 = "Confirmed";
    public static String BREED_TURN_IN = "<gold>Click here to confirm the baby <mob>s</gold>";
    public static List<String> BREED_MOB_COMMANDS = List.of("broadcast <player> Finished their daily quest!");

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
                        configurationSection.getStringList(key + ".quest-pages"),
                        configurationSection.getStringList(key + ".done-pages"),
                        configurationSection.getInt(key + ".amount-min"),
                        configurationSection.getInt(key + ".amount-max")));
                if (Config.DEBUG)
                    Logger.info("Loaded Breed mob quest " + key);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        BREED_MOB_QUEST_NAME = config.getString("breed_mobs.name", BREED_MOB_QUEST_NAME);
        BREED_STEP_1 = config.getString("breed_mobs.step-1", BREED_STEP_1);
        BREED_STEP_2 = config.getString("breed_mobs.step-2", BREED_STEP_2);
        BREED_TURN_IN = config.getString("breed_mobs.turn-in", BREED_TURN_IN);
        BREED_MOB_COMMANDS = config.getStringList("breed_mobs.commands", BREED_MOB_COMMANDS);
    }
}
