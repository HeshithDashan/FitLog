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
        
        // JSP edit form එකට අදාළ පරණ logic එක
        if ("edit".equals(action)) {
            showEditForm(request, response);
        } else {
            // React app එකට JSON දෙන ප්‍රධාන logic එක
            listWorkoutsAsJson(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");

        // "action=delete" කියලා ආවොත්, ඒක delete request එකක්
        if ("delete".equals(action)) {
            deleteWorkout(request, response);
        } else {
            // නැත්නම්, id එකක් තියෙනවද බලලා add ද update ද කියලා තීරණය කරනවා
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                updateWorkout(request, response);
            } else {
                addWorkout(request, response);
            }
        }
    }
    
    // doDelete method එක දැන් අවශ්‍ය නෑ, මොකද අපි POST වලින් ඒ වැඩේ කරන්නේ

    // --- Private Helper Methods ---

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
                response.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
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

    // --- පහළ තියෙන මේ JSP වලට අදාළ methods දෙක දැනට React project එකට අවශ්‍ය නෑ, ඒත් තිබුණට කමක් නෑ ---
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

    private void updateWorkout(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                response.sendRedirect("workouts"); // This should also be changed to JSON later
            } else {
                response.getWriter().write("Failed to update workout.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

