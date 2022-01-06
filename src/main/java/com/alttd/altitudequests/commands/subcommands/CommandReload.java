package com.alttd.altitudequests.commands.subcommands;

import com.alttd.altitudequests.commands.SubCommand;
import com.alttd.altitudequests.config.Config;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandReload extends SubCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        Config.reload();
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
        return Config.RELOAD_HELP_MESSAGE;
    }
}
