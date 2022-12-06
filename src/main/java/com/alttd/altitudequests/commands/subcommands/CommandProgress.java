package com.alttd.altitudequests.commands.subcommands;

import com.alttd.altitudequests.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandProgress extends SubCommand {
    //TODO show player current quest progress
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        return false;
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
