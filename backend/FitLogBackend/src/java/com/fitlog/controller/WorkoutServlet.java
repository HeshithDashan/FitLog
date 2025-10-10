package com.fitlog.controller;

import com.fitlog.dao.WorkoutDAO;
import com.fitlog.model.User;
import com.fitlog.model.Workout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
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

        gson = new GsonBuilder().setDateFormat("MMM d, yyyy").create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String reportType = request.getParameter("report");

        if ("weeklyCalories".equals(reportType)) {

            generateWeeklyCaloriesReport(request, response);
        } else {

            listWorkoutsAsJson(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String idStr = request.getParameter("id");

        if ("delete".equals(action)) {
            deleteWorkout(request, response);
        } else if (idStr != null && !idStr.isEmpty()) {
            updateWorkout(request, response);
        } else {
            addWorkout(request, response);
        }
    }

    private void generateWeeklyCaloriesReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Authentication required\"}");
            return;
        }
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        List<java.util.Map<String, Object>> summaryData = workoutDAO.getWeeklyCaloriesSummary(loggedInUser.getId());

        String jsonResponse = this.gson.toJson(summaryData);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

    private void listWorkoutsAsJson(HttpServletRequest request, HttpServletResponse response) throws IOException {

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
    }

    private void updateWorkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }

    private void deleteWorkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
    }
}
