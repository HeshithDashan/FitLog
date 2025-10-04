package com.fitlog.dao;

import com.fitlog.model.User;
import com.fitlog.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    private static final String INSERT_USER_SQL = "INSERT INTO users (firstName, lastName, email, password) VALUES (?, ?, ?, ?);";

  
    public boolean registerUser(User user) {

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            

            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());



            int result = preparedStatement.executeUpdate();


            return result > 0;

        } catch (SQLException e) {


            e.printStackTrace();
            return false;
        }
    }
}