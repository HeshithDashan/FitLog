package com.fitlog.test;

import com.fitlog.dao.WorkoutDAO;
import com.fitlog.model.Workout;
import java.util.List;

public class TestDB {

    public static void main(String[] args) {
        System.out.println("--- Testing getWorkoutsByUserId() ---");

        WorkoutDAO workoutDAO = new WorkoutDAO();

        int testUserId = 1;
        System.out.println("Fetching workouts for user ID: " + testUserId);

        List<Workout> userWorkouts = workoutDAO.getWorkoutsByUserId(testUserId);

        if (userWorkouts != null && !userWorkouts.isEmpty()) {
            System.out.println("\nSUCCESS: Found " + userWorkouts.size() + " workouts for user ID " + testUserId);
            
            for (Workout workout : userWorkouts) {
                System.out.println("------------------------------------");
                System.out.println("  Workout ID: " + workout.getId());
                System.out.println("  Type: " + workout.getWorkoutType());
                System.out.println("  Duration: " + workout.getDurationMinutes() + " mins");
                System.out.println("  Date: " + workout.getLogDate());
                System.out.println("------------------------------------");
            }
        } else if (userWorkouts != null) {
            System.out.println("INFO: No workouts found for user ID " + testUserId);
        } else {
            System.out.println("FAILURE: The method returned null. Check for errors in the DAO.");
        }
    }
}
