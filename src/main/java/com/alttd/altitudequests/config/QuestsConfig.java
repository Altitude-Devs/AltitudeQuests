package com.alttd.altitudequests.config;;

import com.alttd.altitudequests.objects.MineQuestObject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.kyori.adventure.text.minimessage.template.TemplateResolver;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QuestsConfig extends AbstractConfig {
    static QuestsConfig config;
    static int version;

    public QuestsConfig() {
        super(new File(System.getProperty("user.home") + File.separator + "share" + File.separator + "configs" + File.separator + "AltitudeQuests"), "quests.yml");
    }

    public static void reload() {
        config = new QuestsConfig();

        version = config.getInt("config-version", 1);
        config.set("config-version", 1);

        config.readConfig(QuestsConfig.class, null);
    }

    String getString(String path, String def) {
        yaml.addDefault(path, def);
        return yaml.getString(path, yaml.getString(path));
    }

    public static List<MineQuestObject> MINE_QUESTS = new ArrayList<>();
    private static void loadMineQuests() {
        MINE_QUESTS.clear();
        ConfigurationSection configurationSection = config.getConfigurationSection("mining.possible_tasks");
        Set<String> keys = configurationSection.getKeys(false);
        MiniMessage miniMessage = MiniMessage.miniMessage();
        for (String key : keys) {
            Material material = Material.valueOf(configurationSection.getString(key + "material"));

            TemplateResolver resolver = TemplateResolver.resolving(Template.template("block", material.name().toLowerCase()));
            List<Component> collect = configurationSection.getStringList(key + "pages").stream()
                    .map(page -> miniMessage.deserialize(page, resolver))
                    .collect(Collectors.toList());
            MINE_QUESTS.add(new MineQuestObject(key,
                    configurationSection.getString(key + "name"),
                    material,
                    configurationSection.getInt(key + "amount"),
                    collect));
        }
    }
}
