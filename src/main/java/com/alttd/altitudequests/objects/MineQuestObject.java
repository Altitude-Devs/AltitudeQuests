package com.alttd.altitudequests.objects;

import org.bukkit.Material;

import java.util.List;

public class MineQuestObject {

    String internalName;
    String name;
    Material material;
    int amount;
    List<String> pages;

    public MineQuestObject(String internalName, String name, Material material, int amount, List<String> pages) {
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

    public List<String> getPages() {
        return pages;
    }
}
