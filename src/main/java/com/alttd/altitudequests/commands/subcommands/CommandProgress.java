package com.alttd.altitudequests.commands.subcommands;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.commands.SubCommand;
import com.alttd.altitudequests.config.MessagesConfig;
import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.util.ProgressBookOpener;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class CommandProgress extends SubCommand {
    //TODO show player current quest progress

    private static final Set<UUID> inProcess = new HashSet<>();
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMiniMessage(MessagesConfig.NO_CONSOLE, null);
            return true;
        }
        if (player == null || !player.hasPlayedBefore()) {
            commandSender.sendMiniMessage(getHelpMessage(), null);
            return true;
        }
        Quest dailyQuest = Quest.getDailyQuest(player.getUniqueId());
        if (dailyQuest == null) {
            player.sendMiniMessage("<red>You have no active quests.</red>", null);
            return true;
        }
        if (dailyQuest.isDone()) {
            player.sendMiniMessage("<green>You already completed the daily quest.</green>", null);
            return true;
        }
        final UUID uniqueId = player.getUniqueId();
        if (inProcess.contains(uniqueId)) {
            return true;
        }
        inProcess.add(uniqueId);
        new BukkitRunnable() {
            @Override
            public void run() {
                ProgressBookOpener.openProgressBook(player);
                inProcess.remove(uniqueId);
            }
        }.runTaskAsynchronously(AQuest.getInstance());
        return true;
    }

    @Override
    public String getName() {
        return "progress";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        return null;
    }

    @Override
    public String getHelpMessage() {
        return null;
    }

    @Override
    public boolean shouldTabComplete() {
        return true;
    }
}
