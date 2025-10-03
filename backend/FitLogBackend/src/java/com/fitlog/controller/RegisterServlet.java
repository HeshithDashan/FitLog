package com.fitlog.controller;

import com.fitlog.dao.UserDAO; 
import com.fitlog.model.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setPassword(password);
        
        UserDAO userDAO = new UserDAO();
        
        boolean isRegistered = userDAO.registerUser(newUser);
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        if (isRegistered) {
            System.out.println("User registered successfully: " + email);
            out.print("{\"status\": \"success\", \"message\": \"User registered successfully!\"}");
        } else {
            System.out.println("User registration failed for: " + email);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); 
            out.print("{\"status\": \"error\", \"message\": \"User registration failed. Please try again.\"}");
        }
        
        out.flush(); 
    }

}