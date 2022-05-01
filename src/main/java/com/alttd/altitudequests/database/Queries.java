package com.alttd.altitudequests.database;

import com.alttd.altitudequests.objects.GoalType;
import com.alttd.altitudequests.util.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Queries {

    public static int setUserProgress(UUID uuid, GoalType goalType, int progress) {
        String sql = "INSERT VALUES (?, ?, ?) INTO user_seen " +
                "WHERE uuid = ? AND goal_type = ? " +
                "ON DUPLICATE KEY UPDATE progress = ?";
        long time;

        try {
            PreparedStatement preparedStatement = Database.connection.prepareStatement(sql);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, goalType.name());
            preparedStatement.setInt(3, progress);
            preparedStatement.setString(4, uuid.toString());
            preparedStatement.setString(5, goalType.name());
            preparedStatement.setInt(6, progress);

            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.warning("Unable to set progress for %.", uuid.toString());
        }

        return (0);
    }
}
