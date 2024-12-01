package ecommercia.utils;

import java.sql.Connection;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try (Connection connection = DatabaseUtility.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to SQLite database successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to connect to the database.");
        }
    }
}
