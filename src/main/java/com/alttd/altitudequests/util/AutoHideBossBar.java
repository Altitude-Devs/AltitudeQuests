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
import org.bukkit.scheduler.BukkitScheduler;

public class AutoHideBossBar extends BukkitRunnable {

    private final BossBar bossBar;

    public AutoHideBossBar(Player player, Variant variant, String suffix) throws Exception {
        NamespacedKey namespacedKeyOne = NamespacedKey.fromString(player.getUniqueId() + variant.getInternalName() + suffix, AQuest.getInstance());
        if (namespacedKeyOne == null) {
            Logger.warning("Unable to create nameSpacedKey with suffix % for quest for %", suffix, player.getName());
            throw new Exception("Failed to create namespace key");
        }
        this.bossBar = Bukkit.createBossBar(
                namespacedKeyOne,
                "Step One Progress",
                BarColor.GREEN,
                BarStyle.SOLID);
        bossBar.setVisible(false);
        bossBar.addPlayer(player);
    }

    public void show(double progress) {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        if (scheduler.isQueued(this.getTaskId()))
            scheduler.cancelTask(this.getTaskId());
        bossBar.setVisible(true);
        bossBar.setProgress(progress);
        this.runTaskLater(AQuest.getInstance(), Config.BOSS_BAR_AUTO_HIDE.getSeconds() * 20);
    }

    @Override
    public void run() {
        bossBar.setVisible(false);
    }
}
