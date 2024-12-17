package com.example.noteapp.database;

import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String DATABASE_URL = "jdbc:mysql://YOUR_SERVER_IP:3306/YOUR_DATABASE_NAME";
    private static final String DATABASE_USER = "your_username";
    private static final String DATABASE_PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (ClassNotFoundException e) {
            Log.e("ConnectionManager", "MySQL Driver not found", e);
            throw new SQLException("MySQL Driver not found", e);
        }
    }
} 