package com.alttd.altitudequests.gui;

import net.kyori.adventure.inventory.Book;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.UUID;

public interface GUI {
    HashMap<UUID, GUI> GUIByUUID = new HashMap<>();

    void open(Player player);

    GUIAction getGuiAction(int slot);

    Inventory getInventory();

    Book getBook();
}
