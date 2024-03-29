package com.alttd.altitudequests.commands.subcommands;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.commands.SubCommand;
import com.alttd.altitudequests.config.*;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandReload extends SubCommand {

    private final AQuest plugin;

    public CommandReload(AQuest plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        plugin.reloadConfigs();
        commandSender.sendMiniMessage("<green>Reloaded AltitudeQuests config.</green>", null);
        return true;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getHelpMessage() {
        return MessagesConfig.RELOAD_HELP_MESSAGE;
    }

    @Override
    public boolean shouldTabComplete() {
        return true;
    }
}
