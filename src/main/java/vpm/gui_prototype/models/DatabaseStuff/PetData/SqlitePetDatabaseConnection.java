package vpm.gui_prototype.models.DatabaseStuff.PetData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton class that manages the connection to a SQLite database
 * for storing pet information.
 */
public class SqlitePetDatabaseConnection {
    private static Connection instance = null;

    /**
     * Private constructor to prevent instantiation from outside the class.
     * Initializes the database connection.
     */
    private SqlitePetDatabaseConnection() {
        String url = "jdbc:sqlite:pets_database.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println("Failed to establish a database connection: " + sqlEx.getMessage());
        }
    }

    /**
     * Provides access to the singleton instance of the database connection.
     * If the connection has not been created yet, it initializes a new one.
     *
     * @return The instance of the SQL connection.
     */
    public static Connection getInstance() {
        if (instance == null) {
            new SqlitePetDatabaseConnection();
        }
        return instance;
    }
}
