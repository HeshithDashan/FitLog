package com.fitlog.controller;

import com.fitlog.dao.WorkoutDAO;
import com.fitlog.model.User;
import com.fitlog.model.Workout;
import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/workouts")
public class WorkoutServlet extends HttpServlet {

    private WorkoutDAO workoutDAO;
    private Gson gson;

    @Override
    public void init() {
        workoutDAO = new WorkoutDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        listWorkoutsAsJson(request, response);
    }
    
    // --- doPost Method එක සරල කළා ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        String idStr = request.getParameter("id");

        if ("delete".equals(action)) {
            deleteWorkout(request, response);
        } else if (idStr != null && !idStr.isEmpty()) {
            // id එකක් තියෙනවා නම්, ඒක update request එකක්
            updateWorkout(request, response);
        } else {
            // id එකක් නැත්නම්, ඒක add request එකක්
            addWorkout(request, response);
        }
    }
    
    // doPut method එක දැන් අවශ්‍ය නෑ. සම්පූර්ණයෙන්ම අයින් කළා.

    // --- Private Helper Methods (unchanged from before) ---

    private void listWorkoutsAsJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ... (no changes here)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Authentication required\"}");
            return;
        }
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Workout> workoutList = workoutDAO.getWorkoutsByUserId(loggedInUser.getId());
        String jsonResponse = this.gson.toJson(workoutList);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

    private void addWorkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ... (no changes here)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Authentication required\"}");
            return;
        }
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        try {
            Workout newWorkout = new Workout();
            newWorkout.setUserId(loggedInUser.getId());
            newWorkout.setWorkoutType(request.getParameter("workoutType"));
            newWorkout.setDurationMinutes(Integer.parseInt(request.getParameter("durationMinutes")));
            String caloriesStr = request.getParameter("caloriesBurned");
            newWorkout.setCaloriesBurned((caloriesStr == null || caloriesStr.isEmpty()) ? 0 : Integer.parseInt(caloriesStr));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(request.getParameter("logDate"));
            newWorkout.setLogDate(new Date(utilDate.getTime()));
            
            if (workoutDAO.addWorkout(newWorkout)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write("{\"message\":\"Workout added successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Failed to add workout\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid data format\"}");
        }
    }

    private void updateWorkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ... (no changes here)
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Authentication required\"}");
            return;
        }
        try {
            Workout workout = new Workout();
            workout.setId(Integer.parseInt(request.getParameter("id"))); 
            workout.setWorkoutType(request.getParameter("workoutType"));
            workout.setDurationMinutes(Integer.parseInt(request.getParameter("durationMinutes")));
            String caloriesStr = request.getParameter("caloriesBurned");
            workout.setCaloriesBurned((caloriesStr == null || caloriesStr.isEmpty()) ? 0 : Integer.parseInt(caloriesStr));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(request.getParameter("logDate"));
            workout.setLogDate(new Date(utilDate.getTime()));
            
            if (workoutDAO.updateWorkout(workout)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\":\"Workout updated successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Failed to update workout\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid data for update\"}");
        }
    }

    private void deleteWorkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Authentication required\"}");
            return;
        }
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            if (workoutDAO.deleteWorkout(id)) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"message\":\"Workout deleted successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"Workout not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid ID\"}");
        }
    }
}