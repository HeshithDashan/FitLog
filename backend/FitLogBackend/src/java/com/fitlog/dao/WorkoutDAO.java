package com.fitlog.dao;

import com.fitlog.model.Workout;
import com.fitlog.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WorkoutDAO {

    private static final String INSERT_WORKOUT_SQL = "INSERT INTO workouts (userId, workoutType, durationMinutes, caloriesBurned, logDate) VALUES (?, ?, ?, ?, ?);";

    public boolean addWorkout(Workout workout) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_WORKOUT_SQL)) {

            preparedStatement.setInt(1, workout.getUserId());
            preparedStatement.setString(2, workout.getWorkoutType());
            preparedStatement.setInt(3, workout.getDurationMinutes());
            preparedStatement.setInt(4, workout.getCaloriesBurned());
            preparedStatement.setDate(5, workout.getLogDate()); 

            int result = preparedStatement.executeUpdate();

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
