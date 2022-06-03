package com.alttd.altitudequests.objects.variants;

import com.alttd.altitudequests.objects.Variant;
import org.bukkit.Material;

import java.util.List;

public class CollectDropsQuestObject extends Variant {

    private final Material material;

    public CollectDropsQuestObject(String internalName, String name, Material item,
                                   List<String> questPages, List<String> donePages, int min, int max) {
        super(internalName, name, questPages, donePages, min, max);
        this.material = item;
    }

    public Material getMaterial() {
        return material;
    }
}
