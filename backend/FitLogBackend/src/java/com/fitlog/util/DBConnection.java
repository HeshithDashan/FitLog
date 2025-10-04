package com.fitlog.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;

    static {
        try {
            Properties prop = new Properties();
            InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("config.properties");
            
            if(input == null){
                System.out.println("FATAL ERROR: Cannot find config.properties in the default package!");
            } else {
                 prop.load(input);
                 URL = prop.getProperty("db.url");
                 USERNAME = prop.getProperty("db.user");
                 PASSWORD = prop.getProperty("db.password");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}