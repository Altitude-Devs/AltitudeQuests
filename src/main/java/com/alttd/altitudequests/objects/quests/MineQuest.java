package com.alttd.altitudequests.objects.quests;

import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.config.QuestsConfig;
import com.alttd.altitudequests.database.Database;
import com.alttd.altitudequests.objects.variants.MineQuestObject;
import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.util.Logger;
import com.alttd.altitudequests.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class MineQuest extends Quest {

    private final MineQuestObject mineQuestObject;

    public MineQuest(UUID uuid) {
        super(uuid, 0, 0,
            QuestsConfig.MINE_QUESTS.get(Utilities.randomOr0(QuestsConfig.MINE_QUESTS.size() - 1)));
        if (getVariant() instanceof MineQuestObject mineQuestObject)
            this.mineQuestObject = mineQuestObject;
        else
            this.mineQuestObject = null;
        if (mineQuestObject == null) {
            Logger.warning("Tried to create MineQuest but unable to find variant: %.", "unknown");
            return; //TODO error
        }
    }

    public MineQuest(String variantInternalName, int mined, int turnedIn, UUID uuid) {
        super(uuid, mined, turnedIn, QuestsConfig.MINE_QUESTS.stream()
                .filter(object -> variantInternalName.equals(object.getInternalName()))
                .findAny().orElse(null));
        if (getVariant() instanceof MineQuestObject mineQuestObject)
            this.mineQuestObject = mineQuestObject;
        else
            this.mineQuestObject = null;
        if (mineQuestObject == null) {
            Logger.warning("Tried to create MineQuest but unable to find variant: %.", variantInternalName);
            return; //TODO error
        }
        checkDone();
    }

    @Override
    public void save() {
        String sql = "INSERT INTO generic_quest_progress " +
                "(year_day, uuid, quest, quest_variant, step_1_progress, step_2_progress) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                    "quest = ?, quest_variant = ?, step_1_progress = ?, step_2_progress = ?, year_day = ?";
        try {
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(sql);
            int yearDay = Utilities.getYearDay();
            if (Config.DEBUG)
                Logger.info("Saving user for year day %.", String.valueOf(yearDay));
            statement.setInt(1, yearDay);
            statement.setString(2, getUuid().toString());
            statement.setString(3, MineQuest.class.getSimpleName());
            statement.setString(4, mineQuestObject.getInternalName());
            statement.setInt(5, getStep1());
            statement.setInt(6, getStep2());
            statement.setString(7, MineQuest.class.getSimpleName());
            statement.setString(8, mineQuestObject.getInternalName());
            statement.setInt(9, getStep1());
            statement.setInt(10, getStep2());
            statement.setInt(11, yearDay);
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public TagResolver getTagResolvers() {
        return TagResolver.resolver(
                Placeholder.unparsed("block", mineQuestObject.getMaterial().name()),
                Placeholder.parsed("step_1_progress", getStep1() == mineQuestObject.getAmount() ?
                        "<green>" + getStep1() + "</green>" : "<red>" + getStep1() + "</red>"),
                Placeholder.parsed("step_1_total", String.valueOf(mineQuestObject.getAmount())),
                Placeholder.parsed("step_2_progress", getStep2() == mineQuestObject.getAmount() ?
                        "<green>" + getStep2() + "</green>" : "<red>" + getStep2() + "</red>"),
                Placeholder.parsed("step_2_total", String.valueOf(mineQuestObject.getAmount()))
                );
    }

    @Override
    public int turnIn(Player player) {
        PlayerInventory inventory = player.getInventory();
        int maxToTurnIn = getMaxToTurnIn();

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
        addStep2(totalTurnedIn);
        checkDone(player);
        return totalTurnedIn;
    }

    @Override
    public Component getDisplayName() {
        return MiniMessage.miniMessage().deserialize(QuestsConfig.MINE_QUEST_NAME);
    }

    public void mine(Block block) {
        if (isDone() || !block.getType().equals(mineQuestObject.getMaterial()))
            return;
        addStep1(1);
        checkDone();
    }
}
