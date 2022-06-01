package com.alttd.altitudequests.config;;

import com.alttd.altitudequests.objects.MineQuestObject;
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
//TODO maybe have pages for in progress and pages for done???
            List<String> collect = configurationSection.getStringList(key + ".pages");
            MINE_QUESTS.add(new MineQuestObject(key,
                    configurationSection.getString(key + ".name"),
                    material,
                    configurationSection.getInt(key + ".amount"),
                    collect));
        }
    }
}
