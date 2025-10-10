package com.fitlog.dao;

import com.fitlog.model.Workout;
import com.fitlog.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 

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

    public Workout getWorkoutById(int workoutId) { 
        Workout workout = null;
        String SELECT_WORKOUT_BY_ID_SQL = "SELECT * FROM workouts WHERE id = ?;";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_WORKOUT_BY_ID_SQL)) {
            preparedStatement.setInt(1, workoutId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                workout = new Workout();
                workout.setId(rs.getInt("id"));
                workout.setUserId(rs.getInt("userId"));
                workout.setWorkoutType(rs.getString("workoutType"));
                workout.setDurationMinutes(rs.getInt("durationMinutes"));
                workout.setCaloriesBurned(rs.getInt("caloriesBurned"));
                workout.setLogDate(rs.getDate("logDate"));
                workout.setCreatedAt(rs.getTimestamp("createdAt"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workout;
    }

    public boolean updateWorkout(Workout workout) {
        String UPDATE_WORKOUT_SQL = "UPDATE workouts SET workoutType = ?, durationMinutes = ?, caloriesBurned = ?, logDate = ? WHERE id = ?;";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_WORKOUT_SQL)) {
            preparedStatement.setString(1, workout.getWorkoutType());
            preparedStatement.setInt(2, workout.getDurationMinutes());
            preparedStatement.setInt(3, workout.getCaloriesBurned());
            preparedStatement.setDate(4, workout.getLogDate());
            preparedStatement.setInt(5, workout.getId());
            int result = preparedStatement.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteWorkout(int workoutId) {
        String DELETE_WORKOUT_SQL = "DELETE FROM workouts WHERE id = ?;";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_WORKOUT_SQL)) {

            preparedStatement.setInt(1, workoutId);
            int result = preparedStatement.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> getWeeklyCaloriesSummary(int userId) {
        List<Map<String, Object>> summary = new ArrayList<>();
        

        String sql = "SELECT DATE(logDate) as report_date, SUM(caloriesBurned) as total_calories " +
                     "FROM workouts " +
                     "WHERE userId = ? AND logDate >= CURDATE() - INTERVAL 6 DAY " +
                     "GROUP BY DATE(logDate) " +
                     "ORDER BY report_date ASC;";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            
            preparedStatement.setInt(1, userId);
            
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Map<String, Object> dayData = new HashMap<>();
                dayData.put("report_date", rs.getDate("report_date"));
                dayData.put("total_calories", rs.getInt("total_calories"));
                summary.add(dayData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return summary;
    }
}