package com.alttd.altitudequests.commands.subcommands;

import com.alttd.altitudequests.commands.SubCommand;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.config.LocalConfig;
import com.alttd.altitudequests.objects.Quest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandTurnIn extends SubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length != 1)
            return true;
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMiniMessage(Config.NO_CONSOLE, null);
            return true;
        }
        Quest dailyQuest = Quest.getDailyQuest(player.getUniqueId());
        if (dailyQuest == null) {
            player.sendMiniMessage(Config.DAILY_ALREADY_DONE, null);
            return true;
        }
        if (player.getNearbyEntities(5, 5, 5).stream()
                .noneMatch(entity -> entity.getUniqueId().equals(LocalConfig.activeNPC))) {
            player.sendMiniMessage(Config.TOO_FAR_FROM_NPC, null);
            return true;
        }
        dailyQuest.turnIn(player);
        return true;
    }

    @Override
    public String getName() {
        return "turnin";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getHelpMessage() {
        return "\b";
    }

    @Override
    public boolean shouldTabComplete() {
        return false;
    }
}
