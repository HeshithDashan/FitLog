package com.fitlog.controller;

import com.fitlog.dao.WorkoutDAO;
import com.fitlog.model.User;
import com.fitlog.model.Workout;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
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

    @Override
    public void init() {
        workoutDAO = new WorkoutDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login.html");
            return;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        int userId = loggedInUser.getId();

        List<Workout> workoutList = workoutDAO.getWorkoutsByUserId(userId);

        request.setAttribute("workoutList", workoutList);

        RequestDispatcher dispatcher = request.getRequestDispatcher("view-workouts.jsp");
        dispatcher.forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write("Authentication Error: You must be logged in to add a workout.");
            return;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        int userId = loggedInUser.getId();
        
        String workoutType = request.getParameter("workoutType");
        String durationStr = request.getParameter("durationMinutes");
        String caloriesStr = request.getParameter("caloriesBurned");
        String logDateStr = request.getParameter("logDate");
        
        try {
            int durationMinutes = Integer.parseInt(durationStr);
            int caloriesBurned = (caloriesStr == null || caloriesStr.isEmpty()) ? 0 : Integer.parseInt(caloriesStr);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date utilDate = sdf.parse(logDateStr);
            Date logDate = new Date(utilDate.getTime());

            Workout newWorkout = new Workout();
            newWorkout.setUserId(userId);
            newWorkout.setWorkoutType(workoutType);
            newWorkout.setDurationMinutes(durationMinutes);
            newWorkout.setCaloriesBurned(caloriesBurned);
            newWorkout.setLogDate(logDate);

            boolean success = workoutDAO.addWorkout(newWorkout);

            if (success) {
                response.sendRedirect("workouts");
            } else {
                response.getWriter().write("Failed to add workout.");
            }
            
        } catch (ParseException | NumberFormatException e) {
            e.printStackTrace();
            response.getWriter().write("Error: Invalid data format submitted.");
        }
    }
}