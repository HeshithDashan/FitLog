package com.fitlog.dao;

import com.fitlog.model.User;
import com.fitlog.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    private static final String SQL_INSERT_USER = "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";

     public boolean registerUser(User user) {
        
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
             conn = DBConnection.getConnection();
            
             ps = conn.prepareStatement(SQL_INSERT_USER);
            
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            
            int rowsAffected = ps.executeUpdate();
            
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            return false; 
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}