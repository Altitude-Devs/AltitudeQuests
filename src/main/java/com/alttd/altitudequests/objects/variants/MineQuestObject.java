package com.alttd.altitudequests.objects.variants;

import com.alttd.altitudequests.objects.Variant;
import org.bukkit.Material;

import java.util.List;

public class MineQuestObject extends Variant {

    private final Material material;

    public MineQuestObject(String internalName, String name, Material material,
                           List<String> questPages, List<String> donePages, int min, int max) {
        super(internalName, name, questPages, donePages, min, max);
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
