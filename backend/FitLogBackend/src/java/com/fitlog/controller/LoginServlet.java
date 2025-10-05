package com.fitlog.controller;

import com.fitlog.dao.UserDAO;
import com.fitlog.model.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {

            User user = userDAO.loginUser(email, password);


            if (user != null) {

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Login Successful! Welcome " + user.getFirstName());
            } else {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid email or password. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("An error occurred on the server.");
        }
    }
}