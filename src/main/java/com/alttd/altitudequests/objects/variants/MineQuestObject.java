package com.alttd.altitudequests.objects.variants;

import com.alttd.altitudequests.objects.Variant;
import org.bukkit.Material;

import java.util.List;

public class MineQuestObject extends Variant {

    private final Material material;
    private final Material turnInMaterial;

    public MineQuestObject(String internalName, String name, Material material, Material turnInMaterial,
                           List<String> questPages, List<String> donePages, int min, int max) {
        super(internalName, name, questPages, donePages, min, max);
        this.material = material;
        this.turnInMaterial = turnInMaterial;
    }

    public Material getMaterial() {
        return material;
    }
    public Material getTurnInMaterial() {
        return turnInMaterial;
    }
}
