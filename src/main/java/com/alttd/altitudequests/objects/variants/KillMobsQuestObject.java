package com.alttd.altitudequests.objects.variants;

import com.alttd.altitudequests.objects.Variant;
import org.bukkit.entity.EntityType;

import java.util.List;

public class KillMobsQuestObject extends Variant {

    private final EntityType entityType;

    public KillMobsQuestObject(String internalName, String name, EntityType entityType,
                               List<String> questPages, List<String> donePages, int min, int max) {
        super(internalName, name, questPages, donePages, min, max);
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
