package com.fitlog.controller;

import com.fitlog.dao.UserDAO;
import com.fitlog.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("--- LOGIN SERVLET: doPost() method started ---");
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = userDAO.loginUser(email, password);

            if (user != null) {

                System.out.println("User found in DB! User ID: " + user.getId() + ", Name: " + user.getFirstName()); 
                
                HttpSession session = request.getSession();
                session.setAttribute("loggedInUser", user);
                
                System.out.println("Session created successfully. Session ID: " + session.getId()); 
                
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Login Successful! Welcome " + user.getFirstName());
            } else {
                System.out.println("User NOT found in DB for email: " + email); 
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid email or password.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred on the server.");
        }
        System.out.println("--- LOGIN SERVLET: doPost() method finished ---");
    }
}