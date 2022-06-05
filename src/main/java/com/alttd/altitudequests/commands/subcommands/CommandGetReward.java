package com.alttd.altitudequests.commands.subcommands;

import com.alttd.altitudequests.commands.SubCommand;
import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.config.LocalConfig;
import com.alttd.altitudequests.config.MessagesConfig;
import com.alttd.altitudequests.objects.Quest;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandGetReward extends SubCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length != 1)
            return true;
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMiniMessage(MessagesConfig.NO_CONSOLE, null);
            return true;
        }
        Quest dailyQuest = Quest.getDailyQuest(player.getUniqueId());
        if (dailyQuest == null) {
            player.sendMiniMessage("<red>You have no active quests?</red>", null);
            return true;
        }
        if (player.getNearbyEntities(5, 5, 5).stream()
                .noneMatch(entity -> entity.getUniqueId().equals(LocalConfig.activeNPC))) {
            player.sendMiniMessage(MessagesConfig.TOO_FAR_FROM_NPC, Placeholder.component("npc", MiniMessage.miniMessage().deserialize(Config.NPC_NAME)));
            return true;
        }

        if (dailyQuest.isRewardReceived()) {
            player.sendMiniMessage(MessagesConfig.REWARD_ALREADY_RECEIVED, null);
            return true;
        }
        for (String command : dailyQuest.getRewardCommand())
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replaceAll("<player>", commandSender.getName()));
        dailyQuest.setRewardReceived(true);
        player.sendMiniMessage(MessagesConfig.REWARD_SENT, null);
        return true;
    }

    @Override
    public String getName() {
        return "getreward";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getHelpMessage() {
        return null;
    }

    @Override
    public boolean shouldTabComplete() {
        return false;
    }
}
