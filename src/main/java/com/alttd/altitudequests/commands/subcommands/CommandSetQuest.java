package com.alttd.altitudequests.commands.subcommands;

import com.alttd.altitudequests.commands.SubCommand;
import com.alttd.altitudequests.config.MessagesConfig;
import com.alttd.altitudequests.objects.Quest;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandSetQuest extends SubCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length != 4) {
            commandSender.sendMiniMessage(getHelpMessage(), null);
            return true;
        }
        Player player = Bukkit.getServer().getPlayer(args[1]);
        if (player == null || !player.hasPlayedBefore()) {
            commandSender.sendMiniMessage(getHelpMessage(), null);
            return true;
        }
        if (!Quest.loadDailyQuest(args[2], args[3], 0, 0, player.getUniqueId(), false))
            commandSender.sendMiniMessage("<red>Unable to create quest <quest> of variant <variant>.</red>",
                    TagResolver.resolver(Placeholder.parsed("quest", args[2]),
                            Placeholder.parsed("variant", args[3])));
        commandSender.sendMiniMessage("<green>Created quest <quest> of variant <variant>.</green>",
                TagResolver.resolver(Placeholder.parsed("quest", args[2]),
                        Placeholder.parsed("variant", args[3])));
        return true;
    }

    @Override
    public String getName() {
        return "setquest";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        List<String> res = new ArrayList<>();
        switch (args.length) {
            case 2 -> res.addAll(Bukkit.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList()));
            case 3 -> res.addAll(Quest.getTypes());
            case 4 -> res.add("enter quest type");
        }
        return res;
    }

    @Override
    public String getHelpMessage() {
        return MessagesConfig.SET_QUEST_HELP;
    }

    @Override
    public boolean shouldTabComplete() {
        return true;
    }
}
