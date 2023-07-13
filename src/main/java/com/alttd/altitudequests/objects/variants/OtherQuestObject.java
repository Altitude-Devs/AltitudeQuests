package com.alttd.altitudequests.objects.variants;

import com.alttd.altitudequests.objects.Variant;
import org.bukkit.Material;

import java.util.List;

public class OtherQuestObject extends Variant {

    private final Material material;
    private static String step1 = null;
    private static String step2 = null;
    private static String category = null;

    public OtherQuestObject(String internalName, String name, String category, Material item,
                                   List<String> questPages, List<String> donePages, int min, int max, String step1, String step2) {
        super(internalName, name, questPages, donePages, min, max);
        this.material = item;
        this.step1 = step1;
        this.step2 = step2;
        this.category = category;
    }

    public Material getMaterial() {
        return material;
    }
    public static String getStep1() {return step1;}
    public static String getStep2() {return step2;}
    public static String getCategory() {return category;}
}
