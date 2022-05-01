package com.alttd.altitudequests.objects.quests;

import com.alttd.altitudequests.objects.GoalType;
import com.alttd.altitudequests.objects.Quest;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class MineQuest extends Quest {

    public MineQuest(String name, GoalType goalType) {
        super(name, goalType);
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Quest initQuest() {
        return new MineQuest("Mine", GoalType.MINE);
    }

    public void mine(Block block, Player player) {

    }
}
