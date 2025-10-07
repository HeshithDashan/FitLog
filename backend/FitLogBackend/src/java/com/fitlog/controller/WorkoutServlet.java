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
        
        String action = request.getParameter("action");
        
        if ("edit".equals(action)) {
            showEditForm(request, response);
        } else {
            listWorkouts(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String idStr = request.getParameter("id");

        if (idStr != null && !idStr.isEmpty()) {
            updateWorkout(request, response);
        } else {
            addWorkout(request, response);
        }
    }


    private void listWorkouts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login.html");
            return;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Workout> workoutList = workoutDAO.getWorkoutsByUserId(loggedInUser.getId());
        request.setAttribute("workoutList", workoutList);
        RequestDispatcher dispatcher = request.getRequestDispatcher("view-workouts.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login.html");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));
        Workout existingWorkout = workoutDAO.getWorkoutById(id);
        request.setAttribute("workout", existingWorkout); 
        RequestDispatcher dispatcher = request.getRequestDispatcher("edit-workout.jsp"); 
        dispatcher.forward(request, response);
    }

    private void addWorkout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication Error: You must be logged in.");
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
                response.sendRedirect("workouts");
            } else {
                response.getWriter().write("Failed to add workout.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateWorkout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
             response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
             response.getWriter().write("Authentication Error: You must be logged in.");
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
                response.sendRedirect("workouts");
            } else {
                response.getWriter().write("Failed to update workout.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}