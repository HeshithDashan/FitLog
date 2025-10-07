package com.fitlog.controller;

import com.fitlog.dao.WorkoutDAO;
import com.fitlog.model.User;
import com.fitlog.model.Workout;
import com.google.gson.Gson;
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
    private Gson gson;

    @Override
    public void init() {
        workoutDAO = new WorkoutDAO();
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            showEditForm(request, response);
        } else if ("delete".equals(action)) {
            deleteWorkout(request, response);
        } else {

            listWorkoutsAsJson(request, response);
        }
    }

    private void listWorkoutsAsJson(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Authentication required\"}"); // JSON error message
            return;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        List<Workout> workoutList = workoutDAO.getWorkoutsByUserId(loggedInUser.getId());

        String jsonResponse = this.gson.toJson(workoutList);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            updateWorkout(request, response);
        } else {
            addWorkout(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    private void addWorkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    private void updateWorkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    private void deleteWorkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication Error: You must be logged in.");
            return;
        }
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            workoutDAO.deleteWorkout(id);
            response.sendRedirect("workouts");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
