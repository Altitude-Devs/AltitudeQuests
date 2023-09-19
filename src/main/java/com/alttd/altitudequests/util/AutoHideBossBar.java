package com.alttd.altitudequests.util;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.objects.Variant;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.util.HashMap;

public class AutoHideBossBar {

    private static final HashMap<BossBar, Instant> bossBars = new HashMap<>();

    private final BossBar bossBar;

    public AutoHideBossBar(Player player, Variant variant, String part, String title, BarColor barColor) throws Exception {
        NamespacedKey namespacedKeyOne = NamespacedKey.fromString(player.getUniqueId() + variant.getInternalName() + part, AQuest.getInstance());
        if (namespacedKeyOne == null) {
            Logger.warning("Unable to create nameSpacedKey with suffix % for quest for %", part, player.getName());
            throw new Exception("Failed to create namespace key"); //quest names containing upper case letters can cause this
        }
        this.bossBar = Bukkit.createBossBar(
                namespacedKeyOne,
                title,
                barColor,
                BarStyle.SOLID);
        bossBar.setVisible(false);
        bossBar.addPlayer(player);
    }

    public void show(double progress) {
        bossBar.setVisible(true);
        bossBar.setProgress(progress);
        bossBars.put(bossBar, Instant.now().plusSeconds(Config.BOSS_BAR_AUTO_HIDE.toSeconds()));
    }

    public static void initiate() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (HashMap.Entry<BossBar, Instant> entry : bossBars.entrySet()) {
                    if (Instant.now().isAfter(entry.getValue())) {
                        BossBar bossBar = entry.getKey();
                        bossBar.setVisible(false);
                        bossBars.remove(entry.getKey());
                    }
                }
            }
        }.runTaskTimer(AQuest.getInstance(), 10, 10);
    }
}
