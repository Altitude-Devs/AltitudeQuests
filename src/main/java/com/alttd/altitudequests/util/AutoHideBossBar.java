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

import java.time.Instant;

public class AutoHideBossBar implements Runnable {

    private final BossBar bossBar;
    private final Thread thread = new Thread(this);
    private Instant endTime;

    public AutoHideBossBar(Player player, Variant variant, String part, String title, BarColor barColor) throws Exception {
        NamespacedKey namespacedKeyOne = NamespacedKey.fromString(player.getUniqueId() + variant.getInternalName() + part, AQuest.getInstance());
        if (namespacedKeyOne == null) {
            Logger.warning("Unable to create nameSpacedKey with suffix % for quest for %", part, player.getName());
            throw new Exception("Failed to create namespace key");
        }
        this.bossBar = Bukkit.createBossBar(
                namespacedKeyOne,
                title,
                barColor,
                BarStyle.SOLID);
        bossBar.setVisible(false);
        bossBar.addPlayer(player);
    }

    private synchronized void schedule() {
        endTime = Instant.now().plusSeconds(Config.BOSS_BAR_AUTO_HIDE.toSeconds());
        if (!thread.isAlive())
            thread.start();
    }

    public void show(double progress) {
        bossBar.setVisible(true);
        bossBar.setProgress(progress);
        schedule();
    }

    private synchronized boolean waitOrRunTask() {
        if (Instant.now().isBefore(endTime))
            return true;
        bossBar.setVisible(false);
        return false;
    }

    @Override
    public void run() {
        while (waitOrRunTask()) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
