package com.alttd.altitudequests.objects.quests;

import com.alttd.altitudequests.config.QuestsConfig;
import com.alttd.altitudequests.objects.MineQuestObject;
import com.alttd.altitudequests.objects.Quest;
import org.bukkit.block.Block;

import java.util.Optional;

public class MineQuest extends Quest {

    int mined;
    int turnedIn;
    MineQuestObject mineQuestObject;
    boolean isDone = false;

    public MineQuest(MineQuestObject mineQuestObject) {
        mined = 0;
        turnedIn = 0;
        this.mineQuestObject = mineQuestObject;
    }

    public MineQuest(int mined, int turnedIn, String internalName) {
        this.mined = mined;
        this.turnedIn = turnedIn;
        Optional<MineQuestObject> any = QuestsConfig.MINE_QUESTS.stream().filter(object -> internalName.equals(object.getInternalName())).findAny();
        if (any.isEmpty())
            return; //TODO error
        this.mineQuestObject = any.get();
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    public void mine(Block block) {
        if (!isDone && !block.getType().equals(mineQuestObject.getMaterial()))
            return;
        mined += 1;
        if (mined == mineQuestObject.getAmount())
            isDone = true;
    }
}
