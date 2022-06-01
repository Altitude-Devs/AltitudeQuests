package com.alttd.altitudequests.objects.quests;

import com.alttd.altitudequests.config.QuestsConfig;
import com.alttd.altitudequests.database.Database;
import com.alttd.altitudequests.objects.MineQuestObject;
import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.objects.QuestCompleteEvent;
import com.alttd.altitudequests.util.Logger;
import com.alttd.altitudequests.util.Utilities;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class MineQuest extends Quest {

    private final UUID uuid;
    private int mined;
    private int turnedIn;
    private final MineQuestObject mineQuestObject;
    private boolean isDone = false;

    public MineQuest(UUID uuid) {
        this.uuid = uuid;
        mined = 0;
        turnedIn = 0;
        this.mineQuestObject = QuestsConfig.MINE_QUESTS.get(Utilities.randomOr0(QuestsConfig.MINE_QUESTS.size() - 1));
    }

    public MineQuest(String variantInternalName, int mined, int turnedIn,  UUID uuid) {
        this.mined = mined;
        this.turnedIn = turnedIn;
        this.uuid = uuid;
        Optional<MineQuestObject> any = QuestsConfig.MINE_QUESTS.stream().filter(object -> variantInternalName.equals(object.getInternalName())).findAny();
        if (any.isEmpty()) {
            this.mineQuestObject = null;
            Logger.warning("Tried to create MineQuest but unable to find variant: %.", variantInternalName);
            return; //TODO error
        }
        this.mineQuestObject = any.get();
    }

    @Override
    public void save() {
        String sql = "INSERT INTO generic_quest_progress " +
                "(uuid, quest, quest_variant, step_1_progress, step_2_progress) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                    "quest = ?, quest_variant = ?, step_1_progress = ?, step_2_progress = ?";
        try {
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(sql);
            statement.setString(1, uuid.toString());
            statement.setString(2, MineQuest.class.getSimpleName());
            statement.setString(3, mineQuestObject.getInternalName());
            statement.setInt(4, mined);
            statement.setInt(5, turnedIn);
            statement.setString(6, MineQuest.class.getSimpleName());
            statement.setString(7, mineQuestObject.getInternalName());
            statement.setInt(8, mined);
            statement.setInt(9, turnedIn);
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public List<String> getPages() {
        return mineQuestObject.getPages();
    }

    @Override
    public TagResolver getTagResolvers() {
        return TagResolver.resolver(
                Placeholder.unparsed("block", mineQuestObject.getMaterial().name()),
                Placeholder.parsed("mined", mined == mineQuestObject.getAmount() ?
                        "<green>" + mined + "</green>" : "<red>" + mined + "</red>"),
                Placeholder.parsed("total_to_mine", String.valueOf(mineQuestObject.getAmount())),
                Placeholder.parsed("turned_in", turnedIn == mineQuestObject.getAmount() ?
                        "<green>" + turnedIn + "</green>" : "<red>" + turnedIn + "</red>"),
                Placeholder.parsed("total_to_turn_in", String.valueOf(mineQuestObject.getAmount()))
                );
    }

    @Override
    public int turnIn(Player player) {
        PlayerInventory inventory = player.getInventory();
        int maxToTurnIn = Math.min(mineQuestObject.getAmount() - turnedIn, mined);

        if (maxToTurnIn == 0)
            return 0;
        var ref = new Object() {
            int tmpAmount = maxToTurnIn;
        };

        Arrays.stream(inventory.getContents())
                .filter(Objects::nonNull)
                .filter(itemStack -> itemStack.getType().equals(mineQuestObject.getMaterial()))
                .forEach(itemStack -> {
                    if (ref.tmpAmount == 0)
                        return;
                    if (itemStack.getAmount() > ref.tmpAmount) {
                        itemStack.setAmount(itemStack.getAmount() - ref.tmpAmount);
                        ref.tmpAmount = 0;
                    } else {
                        ref.tmpAmount -= itemStack.getAmount();
                        itemStack.setAmount(0);
                    }
                });
        int totalTurnedIn = maxToTurnIn - ref.tmpAmount;
        turnedIn += totalTurnedIn;
        checkDone(player);
        return totalTurnedIn;
    }

    public void mine(Block block) {
        if (isDone || mined == mineQuestObject.getAmount() || !block.getType().equals(mineQuestObject.getMaterial()))
            return;
        mined += 1;
    }

    public void checkDone(Player player) {
        if (turnedIn == mineQuestObject.getAmount() && mined == mineQuestObject.getAmount()) {
            isDone = true;
            QuestCompleteEvent event = new QuestCompleteEvent(player, this, true);
            event.callEvent();
        }
    }
}
