package com.alttd.altitudequests.objects.variants;

import com.alttd.altitudequests.objects.Variant;
import org.bukkit.entity.EntityType;

import java.util.List;

public class KillMobsQuestObject extends Variant {

    private final EntityType entityType;

    public KillMobsQuestObject(String internalName, String name, EntityType entityType, int amount,
                               List<String> questPages, List<String> donePages) {
        super(internalName, name, amount, questPages, donePages);
        this.entityType = entityType;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
