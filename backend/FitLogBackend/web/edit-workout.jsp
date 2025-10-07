<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.fitlog.model.Workout" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Workout - FitLog</title>
    
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Poppins', sans-serif; background-color: #f0f2f5; display: flex; justify-content: center; align-items: center; min-height: 100vh; padding: 20px 0; }
        .form-container { background-color: #ffffff; padding: 40px; border-radius: 10px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); width: 100%; max-width: 500px; }
        .form-container h1 { text-align: center; margin-bottom: 24px; color: #1c1e21; }
        .input-group { margin-bottom: 20px; }
        .input-group label { display: block; margin-bottom: 8px; font-weight: 500; color: #606770; }
        .input-group input, .input-group select { width: 100%; padding: 12px; border: 1px solid #dddfe2; border-radius: 6px; font-size: 16px; font-family: 'Poppins', sans-serif; }
        .input-group input:focus, .input-group select:focus { outline: none; border-color: #007bff; }
        .submit-btn { width: 100%; padding: 12px; background-color: #28a745; border: none; border-radius: 6px; color: #ffffff; font-size: 18px; font-weight: 600; cursor: pointer; transition: background-color 0.3s; }
        .submit-btn:hover { background-color: #218838; }
    </style>
</head>
<body>

    <% Workout workoutToEdit = (Workout) request.getAttribute("workout"); %>

    <div class="form-container">
        <h1>Edit Your Workout</h1>
        
        <% if (workoutToEdit != null) { %>
            <form action="workouts" method="post">
            
                <input type="hidden" name="id" value="<%= workoutToEdit.getId() %>">

                <div class="input-group">
                    <label for="workoutType">Workout Type</label>
                    <% String currentType = workoutToEdit.getWorkoutType(); %>
                    <select id="workoutType" name="workoutType" required>
                        <option value="Running" <%= "Running".equals(currentType) ? "selected" : "" %>>Running</option>
                        <option value="Weightlifting" <%= "Weightlifting".equals(currentType) ? "selected" : "" %>>Weightlifting</option>
                        <option value="Cycling" <%= "Cycling".equals(currentType) ? "selected" : "" %>>Cycling</option>
                        <option value="Swimming" <%= "Swimming".equals(currentType) ? "selected" : "" %>>Swimming</option>
                        <option value="Yoga" <%= "Yoga".equals(currentType) ? "selected" : "" %>>Yoga</option>
                        <option value="Other" <%= "Other".equals(currentType) ? "selected" : "" %>>Other</option>
                    </select>
                </div>

                <div class="input-group">
                    <label for="durationMinutes">Duration (in minutes)</label>
                    <input type="number" id="durationMinutes" name="durationMinutes" value="<%= workoutToEdit.getDurationMinutes() %>" required>
                </div>
                
                <div class="input-group">
                    <label for="caloriesBurned">Calories Burned</label>
                    <input type="number" id="caloriesBurned" name="caloriesBurned" value="<%= workoutToEdit.getCaloriesBurned() %>">
                </div>

                <div class="input-group">
                    <label for="logDate">Date of Workout</label>
                    <input type="date" id="logDate" name="logDate" value="<%= workoutToEdit.getLogDate() %>" required>
                </div>
                
                <button type="submit" class="submit-btn">Save Changes</button>
            </form>
        <% } else { %>
            <p>Error: Workout not found. Please go back to the list and try again.</p>
        <% } %>
    </div>

</body>
</html>