package com.alttd.altitudequests.commands.subcommands;

import com.alttd.altitudequests.commands.SubCommand;
import com.alttd.altitudequests.config.LocalConfig;
import com.alttd.altitudequests.config.MessagesConfig;
import com.alttd.altitudequests.util.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandCreateScruff extends SubCommand {

    @Override
    public boolean onCommand(CommandSender commandSender, String[] args) {
        if (args.length != 8) {
            commandSender.sendMiniMessage(getHelpMessage(), null);
            return true;
        }

        World world = Bukkit.getServer().getWorld(args[7]);
        if (world == null) {
            commandSender.sendMiniMessage(getHelpMessage(), null);
            return true;
        }
        Location location;
        try {
            location = new Location(world, Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]),
                    Float.parseFloat(args[5]), Float.parseFloat(args[6]));
        } catch (NumberFormatException exception) {
            commandSender.sendMiniMessage("<red>Invalid arguments.</red>", null);
            return true;
        }
        Wolf wolf = (Wolf) world.spawnEntity(location, EntityType.WOLF, CreatureSpawnEvent.SpawnReason.CUSTOM);
        wolf.setPersistent(true);
        wolf.setInvulnerable(true);
        wolf.setGravity(false);
        wolf.setSilent(true);
        wolf.setAI(false);
        wolf.setCollarColor(DyeColor.MAGENTA);
        wolf.setCustomNameVisible(true);
        wolf.customName(getMiniMessage().deserialize("Scruff"));

        UUID uuid = wolf.getUniqueId();

        LocalConfig.setActiveNPC(uuid);
        return true;
    }

    @Override
    public String getName() {
        return "createscruff";
    }

    @Override
    public List<String> getTabComplete(CommandSender commandSender, String[] args) {
        List<String> res = new ArrayList<>();
        switch (args.length) {
            case 2 -> {
                if (commandSender instanceof Player player) {
                    res.add(String.valueOf(Utilities.round(player.getLocation().getX(), 2)));
                }
            }
            case 3 -> {
                if (commandSender instanceof Player player) {
                    res.add(String.valueOf(Utilities.round(player.getLocation().getY(), 1)));
                }
            }
            case 4 -> {
                if (commandSender instanceof Player player) {
                    res.add(String.valueOf(Utilities.round(player.getLocation().getZ(), 2)));
                }
            }
            case 5 -> {
                if (commandSender instanceof Player player) {
                    res.add(String.valueOf(Utilities.round(player.getLocation().getYaw(), 2)));
                }
            }
            case 6 -> {
                if (commandSender instanceof Player player) {
                    res.add(String.valueOf(Utilities.round(player.getLocation().getPitch(), 2)));
                }
            }
            case 7 -> {
                if (commandSender instanceof Player player) {
                    res.add(player.getLocation().getWorld().getName());
                }
            }
        }
        return res;
    }

    @Override
    public String getHelpMessage() {
        return MessagesConfig.CREATE_SCRUFF_MESSAGE;
    }
}
