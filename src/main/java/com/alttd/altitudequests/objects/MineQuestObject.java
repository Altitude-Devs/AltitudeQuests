package com.alttd.altitudequests.objects;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;

public class MineQuestObject {

    String internalName;
    String name;
    Material material;
    int amount;
    List<Component> pages;

    public MineQuestObject(String internalName, String name, Material material, int amount, List<Component> pages) {
        this.internalName = internalName;
        this.name = name;
        this.material = material;
        this.amount = amount;
        this.pages = pages;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public List<Component> getPages() {
        return pages;
    }
}
