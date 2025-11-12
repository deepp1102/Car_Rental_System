package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/car_rental_db";
    private static final String USER = "root";  // your MySQL username
    private static final String PASSWORD = "130322"; // your MySQL password

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();
        }
        return conn;
    }
}