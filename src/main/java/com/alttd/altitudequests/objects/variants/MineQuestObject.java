package com.alttd.altitudequests.objects.variants;

import com.alttd.altitudequests.objects.Variant;
import org.bukkit.Material;

import java.util.List;

public class MineQuestObject extends Variant {

    private final Material material;

    public MineQuestObject(String internalName, String name, Material material, int amount,
                           List<String> questPages, List<String> donePages) {
        super(internalName, name, amount, questPages, donePages);
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
