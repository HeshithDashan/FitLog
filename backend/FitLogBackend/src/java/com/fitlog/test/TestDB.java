package com.fitlog.test;

import com.fitlog.util.DBConnection;
import java.sql.Connection;

public class TestDB {

    public static void main(String[] args) {

        System.out.println("--- Testing DBConnection ONLY ---");

        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("SUCCESS: Database connection established successfully!");
            try {
                conn.close();
                System.out.println("Connection closed.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("FAILURE: Failed to establish database connection. Check the logs for other errors.");
        }
    }
}
