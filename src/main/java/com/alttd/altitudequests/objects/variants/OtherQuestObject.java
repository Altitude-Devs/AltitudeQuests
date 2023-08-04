package com.alttd.altitudequests.objects.variants;

import com.alttd.altitudequests.objects.Variant;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.List;

public class OtherQuestObject extends Variant {

    private final Material material;
    private final EntityType entity;
    private final String step1;
    private final String step2;
    private final String category;

    public OtherQuestObject(String internalName, String name, String category, Material item, EntityType entity,
                                   List<String> questPages, List<String> donePages, int min, int max, String step1, String step2) {
        super(internalName, name, questPages, donePages, min, max);
        this.material = item;
        this.entity = entity;
        this.step1 = step1;
        this.step2 = step2;
        this.category = category;
    }

    public Material getMaterial() {
        return material;
    }
    public EntityType getEntity() {return entity;}
    public String getStep1() {return step1;}
    public String getStep2() {return step2;}
    public String getCategory() {return category;}
}
