package com.alttd.altitudequests.objects.quests;

import com.alttd.altitudequests.config.Config;
import com.alttd.altitudequests.config.QuestsConfig;
import com.alttd.altitudequests.database.Database;
import com.alttd.altitudequests.objects.Quest;
import com.alttd.altitudequests.objects.variants.BreedMobsQuestObject;
import com.alttd.altitudequests.util.Logger;
import com.alttd.altitudequests.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class BreedMobsQuest extends Quest {

    private final BreedMobsQuestObject breedMobsQuestObject;

    public BreedMobsQuest(UUID uuid) {
        super(uuid, 0, 0,
                QuestsConfig.BREED_MOB_QUEST.get(Utilities.randomOr0(QuestsConfig.BREED_MOB_QUEST.size() - 1)));
        if (getVariant() instanceof BreedMobsQuestObject breedMobsQuestObject)
            this.breedMobsQuestObject = breedMobsQuestObject;
        else
            this.breedMobsQuestObject = null;
        if (breedMobsQuestObject == null) {
            Logger.warning("Tried to create breedMobQuest but unable to find variant: %.", "unknown");
            return;
        }
    }

    public BreedMobsQuest(UUID uuid, int step1, int step2, String variant) {
        super(uuid, step1, step2, QuestsConfig.BREED_MOB_QUEST.stream()
                .filter(object -> variant.equals(object.getInternalName()))
                .findAny().orElse(null));
        if (getVariant() instanceof BreedMobsQuestObject breedMobsQuestObject)
            this.breedMobsQuestObject = breedMobsQuestObject;
        else
            this.breedMobsQuestObject = null;
        if (breedMobsQuestObject == null) {
            Logger.warning("Tried to create breedMobQuest but unable to find variant: %.", variant);
            return;
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
            statement.setString(3, this.getClass().getSimpleName());
            statement.setString(4, breedMobsQuestObject.getInternalName());
            statement.setInt(5, getStep1());
            statement.setInt(6, getStep2());
            statement.setString(7, this.getClass().getSimpleName());
            statement.setString(8, breedMobsQuestObject.getInternalName());
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
                Placeholder.unparsed("mob", breedMobsQuestObject.getEntityType().name()),
                Placeholder.parsed("step_1_progress", getStep1() == breedMobsQuestObject.getAmount() ?
                        "<green>" + getStep1() + "</green>" : "<red>" + getStep1() + "</red>"),
                Placeholder.parsed("step_1_total", String.valueOf(breedMobsQuestObject.getAmount())),
                Placeholder.parsed("step_2_progress", getStep2() == breedMobsQuestObject.getAmount() ?
                        "<green>" + getStep2() + "</green>" : "<red>" + getStep2() + "</red>"),
                Placeholder.parsed("step_2_total", String.valueOf(breedMobsQuestObject.getAmount()))
        );
    }

    @Override
    public int turnIn(Player player) {
        int maxToTurnIn = getMaxToTurnIn();

        if (maxToTurnIn == 0)
            return 0;
        addStep2(maxToTurnIn);
        checkDone(player);
        return maxToTurnIn;
    }

    @Override
    public Component getDisplayName() {
        return MiniMessage.miniMessage().deserialize(QuestsConfig.BREED_MOB_QUEST_NAME);
    }

    public void breed(LivingEntity entity) {
        if (isDone() || !entity.getType().equals(breedMobsQuestObject.getEntityType()))
            return;
        addStep1(1);
        checkDone();
    }
}
