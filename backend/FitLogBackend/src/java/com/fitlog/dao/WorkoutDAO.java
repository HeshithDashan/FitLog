package com.fitlog.dao;

import com.fitlog.model.Workout;
import com.fitlog.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List; 

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

    public List<Workout> getWorkoutsByUserId(int userId) {
        List<Workout> workouts = new ArrayList<>();
        String SELECT_WORKOUTS_SQL = "SELECT * FROM workouts WHERE userId = ? ORDER BY logDate DESC;";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_WORKOUTS_SQL)) {
            
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Workout workout = new Workout();
                workout.setId(rs.getInt("id"));
                workout.setUserId(rs.getInt("userId"));
                workout.setWorkoutType(rs.getString("workoutType"));
                workout.setDurationMinutes(rs.getInt("durationMinutes"));
                workout.setCaloriesBurned(rs.getInt("caloriesBurned"));
                workout.setLogDate(rs.getDate("logDate"));
                workout.setCreatedAt(rs.getTimestamp("createdAt"));
                
                workouts.add(workout);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return workouts;
    }
}