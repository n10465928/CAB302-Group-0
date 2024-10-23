package vpm.gui_prototype.models.DatabaseStuff.UserData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class to manage the SQLite database connection for user data.
 */
public class UserSqliteConnection {
    // Holds the single instance of the database connection
    private static Connection instance = null;

    /**
     * Private constructor to initialize the database connection.
     * This is called only once when the instance is created.
     */
    private UserSqliteConnection() {
        String url = "jdbc:sqlite:users.db"; // Database URL
        try {
            instance = DriverManager.getConnection(url); // Establish the connection
        } catch (SQLException sqlEX) {
            System.out.println(url); // Print the URL for debugging
            System.err.println(sqlEX); // Print any SQL exceptions
        }
    }

    /**
     * Provides access to the singleton instance of the database connection.
     * Creates a new instance if one does not already exist.
     *
     * @return The singleton database connection instance.
     */
    public static Connection getInstance() {
        if (instance == null) {
            new UserSqliteConnection(); // Create a new instance if none exists
        }
        return instance; // Return the database connection instance
    }
}
