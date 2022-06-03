package com.alttd.altitudequests.commands.subcommands;

import com.alttd.altitudequests.commands.CommandManager;
import com.alttd.altitudequests.commands.SubCommand;
import com.alttd.altitudequests.config.MessagesConfig;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHelp extends SubCommand {

    private final CommandManager commandManager;

    public CommandHelp(CommandManager commandManager) {
        super();
        this.commandManager = commandManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        commandSender.sendMiniMessage(MessagesConfig.HELP_MESSAGE_WRAPPER.replaceAll("<commands>", commandManager
                        .getSubCommands().stream()
                        .filter(SubCommand::shouldTabComplete)
                        .filter(subCommand -> commandSender.hasPermission(subCommand.getPermission()))
                        .map(SubCommand::getHelpMessage)
                        .collect(Collectors.joining("\n"))), null);
        return true;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getHelpMessage() {
        return MessagesConfig.HELP_MESSAGE;
    }

    @Override
    public boolean shouldTabComplete() {
        return true;
    }
}
