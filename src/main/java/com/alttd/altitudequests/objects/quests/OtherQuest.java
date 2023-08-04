package com.alttd.altitudequests.objects.quests;

import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.config.QuestsConfig;
import com.alttd.altitudequests.database.Database;
import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.objects.Variant;
import com.alttd.altitudequests.objects.variants.OtherQuestObject;
import com.alttd.altitudequests.util.Logger;
import com.alttd.altitudequests.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class OtherQuest extends Quest {

    private final OtherQuestObject otherQuestObject;

    public OtherQuest(UUID uuid) {
        super(uuid, 0, 0,
                QuestsConfig.OTHER_QUEST.get(Utilities.randomOr0(QuestsConfig.OTHER_QUEST.size() - 1)), -1, false);
        if (getVariant() instanceof OtherQuestObject otherQuestObject)
            this.otherQuestObject = otherQuestObject;
        else
            this.otherQuestObject = null;
        if (otherQuestObject == null) {
            Logger.warning("Tried to create OtherQuest but unable to find variant: %.", "unknown");
            return;
        }
    }

    public OtherQuest(UUID uuid, int step1, int step2, String variant, int amount, boolean rewardReceived) {
        super(uuid, step1, step2, QuestsConfig.OTHER_QUEST.stream()
                .filter(object -> variant.equals(object.getInternalName()))
                .findAny().orElse(null), amount, rewardReceived);
        if (getVariant() instanceof OtherQuestObject otherQuestObject)
            this.otherQuestObject = otherQuestObject;
        else
            this.otherQuestObject = null;
        if (otherQuestObject == null) {
            Logger.warning("Tried to create OtherQuest but unable to find variant: %.", variant);
            Logger.warning("Possible variants: %", QuestsConfig.OTHER_QUEST.stream().map(Variant::getInternalName).collect(Collectors.joining(", ")));
            return;
        }
        checkDone();
    }

    @Override
    public void save() {
        String sql = "INSERT INTO generic_quest_progress " +
                "(year_day, uuid, quest, quest_variant, step_1_progress, step_2_progress, amount, reward_received) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "quest = ?, quest_variant = ?, step_1_progress = ?, step_2_progress = ?, year_day = ?, amount = ?, reward_received = ?";
        try {
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(sql);
            int yearDay = Utilities.getYearDay();
            if (Config.DEBUG)
                Logger.info("Saving user for year day %.", String.valueOf(yearDay));
            statement.setInt(1, yearDay);
            statement.setString(2, getUuid().toString());
            statement.setString(3, this.getClass().getSimpleName());
            statement.setString(4, otherQuestObject.getInternalName());
            statement.setInt(5, getStep1());
            statement.setInt(6, getStep2());
            statement.setInt(7, getAmount());
            statement.setInt(8, isRewardReceived() ? 1 : 0);
            statement.setString(9, this.getClass().getSimpleName());
            statement.setString(10, otherQuestObject.getInternalName());
            statement.setInt(11, getStep1());
            statement.setInt(12, getStep2());
            statement.setInt(13, yearDay);
            statement.setInt(14, getAmount());
            statement.setInt(15, isRewardReceived() ? 1 : 0);
            statement.execute();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public TagResolver getTagResolvers() {
        TagResolver resolver = TagResolver.resolver(
                Placeholder.unparsed("item", Utilities.formatName(otherQuestObject.getMaterial().name())),
                Placeholder.parsed("step_1_progress", getStep1() == getAmount() ?
                        "<green>" + getStep1() + "</green>" : "<red>" + getStep1() + "</red>"),
                Placeholder.parsed("step_1_total", String.valueOf(getAmount())),
                Placeholder.parsed("step_2_progress", getStep2() == getAmount() ?
                        "<green>" + getStep2() + "</green>" : "<red>" + getStep2() + "</red>"),
                Placeholder.parsed("step_2_total", String.valueOf(getAmount())),
                Placeholder.unparsed("step_1", otherQuestObject.getStep1()),
                Placeholder.unparsed("step_2", otherQuestObject.getStep2()),
                Placeholder.unparsed("turn_in_text", QuestsConfig.OTHER_TURN_IN)
        );
        Component turnInText = MiniMessage.miniMessage().deserialize(QuestsConfig.OTHER_TURN_IN, resolver);
        return TagResolver.resolver(
                resolver,
                Placeholder.component("turn_in_text", turnInText)
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
                .filter(itemStack -> itemStack.getType().equals(otherQuestObject.getMaterial()))
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
        return MiniMessage.miniMessage().deserialize("<green>%s</green>".formatted( otherQuestObject.getCategory()));
        //return MiniMessage.miniMessage().deserialize("%s<green>: </green>%s".formatted(QuestsConfig.OTHER_QUEST_NAME, otherQuestObject.getCategory()));
    }

    @Override
    public List<String> getRewardCommand() {
        return QuestsConfig.COLLECT_DROPS_COMMANDS;
    }

    public void fish(ItemStack caughtItem){
        if (isDone() || !caughtItem.getType().equals(otherQuestObject.getMaterial()) || getAmount() == getStep1()) {
            return;
        }
        addStep1(1);
        checkDone();
    }

    public void shear(Entity entity) {
        if (isDone() || !entity.getType().equals(otherQuestObject.getEntity()) || getAmount() == getStep1()) {
            return;
        }
        DyeColor color = getDyeColorFromItemStack(otherQuestObject.getMaterial());
        if (entity instanceof Sheep) {
            Sheep sheep = (Sheep) entity;
            if (sheep.getColor() != color) {
                return;
            }
        }
        addStep1(1);
        checkDone();
    }

    public void bucket(ItemStack bucket, Entity entity) {
        if (isDone() || !entity.getType().equals(otherQuestObject.getEntity()) || getAmount() == getStep1()) {
            return;
        }
        addStep1(1);
        checkDone();
    }

    public void raid(){
        if (isDone() || getAmount() == getStep1() || otherQuestObject.getCategory() != "Raid") {
            return;
        }
        addStep1(1);
        checkDone();
    }

    public void brewingStarted(ItemStack ingredient, Location brewingStandLocation){
        Logger.warning("Brewing Started");
    }

    public void brewingFinished(List <ItemStack> results, Location brewingStandLocation) {
        Logger.warning("Brewing Finished");
    }

    public static List<String> getSubTypes() {
        return QuestsConfig.OTHER_QUEST.stream().map(Variant::getInternalName).collect(Collectors.toList());
    }

    public static DyeColor getDyeColorFromItemStack(Material material) {
        if (material != null && material.name().contains("_WOOL")) {
            String colorName = material.name().replace("_WOOL", "");
            try {
                return DyeColor.valueOf(colorName);
            } catch (IllegalArgumentException ignored) {
                // This will be thrown if the color name doesn't match the enum
            }
        }
        return null;
    }
}

