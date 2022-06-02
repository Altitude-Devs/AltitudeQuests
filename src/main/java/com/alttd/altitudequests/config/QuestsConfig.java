package com.alttd.altitudequests.config;;

import com.alttd.altitudequests.objects.variants.MineQuestObject;
import com.alttd.altitudequests.util.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

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
            Material material = Material.valueOf(configurationSection.getString(key + ".material"));
            MINE_QUESTS.add(new MineQuestObject(key,
                    configurationSection.getString(key + ".name"),
                    material,
                    configurationSection.getInt(key + ".amount"),
                    configurationSection.getStringList(key + ".quest-pages"),
                    configurationSection.getStringList(key + ".done-pages")));
        }
        MINE_QUEST_NAME = config.getString("mining.name", MINE_QUEST_NAME);
    }
}
