<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.fitlog.model.Workout" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <%-- (Head section is the same, no changes here) --%>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Workouts - FitLog</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Poppins', sans-serif; background-color: #f0f2f5; padding: 40px 0; }
        .container { background-color: #ffffff; padding: 40px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); width: 90%; max-width: 900px; margin: 0 auto; }
        .container h1 { text-align: center; margin-bottom: 24px; color: #1c1e21; }
        .btn { display: inline-block; padding: 10px 20px; background-color: #007bff; border: none; border-radius: 6px; color: #ffffff; font-size: 16px; font-weight: 600; cursor: pointer; text-decoration: none; transition: background-color 0.3s; margin-bottom: 20px; }
        .btn:hover { background-color: #0056b3; }
        .workout-table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        .workout-table th, .workout-table td { border: 1px solid #ddd; padding: 12px; text-align: left; }
        .workout-table th { background-color: #f2f2f2; font-weight: 600; }
        .workout-table tbody tr:nth-child(even) { background-color: #f9f9f9; }
        .workout-table tbody tr:hover { background-color: #f1f1f1; }
        .no-workouts { text-align: center; margin-top: 20px; color: #606770; }
        /* --- අලුත් style එක --- */
        .action-link { color: #007bff; text-decoration: none; font-weight: 500; }
        .action-link:hover { text-decoration: underline; }
    </style>
</head>
<body>

    <div class="container">
        <h1>My Workouts</h1>
        <a href="add-workout.html" class="btn">Add New Workout</a>
        
        <%
            List<Workout> workoutList = (List<Workout>) request.getAttribute("workoutList");
        %>

        <% if (workoutList != null && !workoutList.isEmpty()) { %>
        
            <table class="workout-table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Workout Type</th>
                        <th>Duration (mins)</th>
                        <th>Calories Burned</th>
                        <th>Actions</th> 
                    </tr>
                </thead>
                <tbody>
                    <% for (Workout workout : workoutList) { %>
                        <tr>
                            <td><%= workout.getLogDate() %></td>
                            <td><%= workout.getWorkoutType() %></td>
                            <td><%= workout.getDurationMinutes() %></td>
                            <td><%= workout.getCaloriesBurned() %></td>

                            <td>
                                <a href="workouts?action=edit&id=<%= workout.getId() %>" class="action-link">Edit</a>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>

        <% } else { %>
            <p class="no-workouts">You haven't logged any workouts yet. Add one now!</p>
        <% } %>
        
    </div>

</body>
</html>