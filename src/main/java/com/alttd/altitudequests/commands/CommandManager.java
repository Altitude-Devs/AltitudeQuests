package com.alttd.altitudequests.commands;

import com.alttd.altitudequests.AQuest;
import com.alttd.altitudequests.commands.subcommands.*;
import com.alttd.altitudequests.config.MessagesConfig;
import com.alttd.altitudequests.util.Logger;
import org.bukkit.command.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandManager implements CommandExecutor, TabExecutor {
    private final List<SubCommand> subCommands;

    public CommandManager() {
        AQuest aQuest = AQuest.getInstance();

        PluginCommand command = aQuest.getCommand("aquest");
        if (command == null) {
            subCommands = null;
            Logger.severe("Unable to find AltitudeQuests command.");
            return;
        }
        command.setExecutor(this);
        command.setTabCompleter(this);

        subCommands = Arrays.asList(
                new CommandHelp(this),
                new CommandReload(AQuest.getInstance()),
                new CommandCreateScruff(),
                new CommandChangeQuest(),
                new CommandTurnIn(),
                new CommandSetQuest());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        SubCommand subCommand;
        if (args.length == 0)
            subCommand = getSubCommand("help");
        else
            subCommand = getSubCommand(args[0]);

        if (!commandSender.hasPermission(subCommand.getPermission())) {
            commandSender.sendMiniMessage(MessagesConfig.NO_PERMISSION, null);
            return true;
        }

        return subCommand.onCommand(commandSender, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String cmd, @NotNull String[] args) {
        List<String> res = new ArrayList<>();

        if (args.length <= 1) {
            res.addAll(subCommands.stream()
                    .filter(SubCommand::shouldTabComplete)
                    .filter(subCommand -> commandSender.hasPermission(subCommand.getPermission()))
                    .map(SubCommand::getName)
                    .filter(name -> args.length == 0 || name.startsWith(args[0]))
                    .collect(Collectors.toList())
            );
        } else {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null && subCommand.shouldTabComplete() && commandSender.hasPermission(subCommand.getPermission()))
                res.addAll(subCommand.getTabComplete(commandSender, args));
        }
        return res;
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    private SubCommand getSubCommand(String cmdName) {
        return subCommands.stream()
                .filter(subCommand -> subCommand.getName().equals(cmdName))
                .findFirst()
                .orElse(null);
    }
}