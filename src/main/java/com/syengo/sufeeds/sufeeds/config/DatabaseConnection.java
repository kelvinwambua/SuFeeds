package com.syengo.sufeeds.sufeeds.config;


import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/sufeed";
    private static final String USER = "postgres";
    private static final String PASS = "password"; // Make sure to use your actual database password

    public static Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
