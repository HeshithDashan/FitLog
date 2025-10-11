package com.fitlog.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthCheckServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;


    public AuthCheckServlet() {
        super();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
     HttpSession session = request.getSession(false); 
        

     if (session != null && session.getAttribute("loggedInUser") != null) {

         response.setStatus(HttpServletResponse.SC_OK);
        } else {

         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST method is not supported.");
    }

}