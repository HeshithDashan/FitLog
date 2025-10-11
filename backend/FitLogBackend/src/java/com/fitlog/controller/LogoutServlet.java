package com.fitlog.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("--- LOGOUT SERVLET: doPost() method started ---");
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            session.invalidate();
            System.out.println("Session invalidated successfully.");
        } else {
            System.out.println("No active session to invalidate.");
        }
        
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"message\":\"Logged out successfully\"}");
    }
}