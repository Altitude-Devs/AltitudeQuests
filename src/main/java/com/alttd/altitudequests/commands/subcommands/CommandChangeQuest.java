package com.alttd.altitudequests.commands.subcommands;

import com.alttd.altitudequests.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandChangeQuest extends SubCommand {
    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        //TODO check if they have money
        //TODO error return if not, take money if they do
        //TODO give new quest
        return false;
    }

    @Override
    public String getName() {
        return "change";
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
