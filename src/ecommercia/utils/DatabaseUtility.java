package ecommercia.utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtility {

    public static Connection getConnection() throws SQLException {
        // Define the path to the database file in the 'data' folder
        String databasePath = new File("data/ecommercia.db").getAbsolutePath();
        String databaseUrl = "jdbc:sqlite:" + databasePath;

        return DriverManager.getConnection(databaseUrl);
    }
}
