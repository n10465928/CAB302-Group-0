package vpm.gui_prototype.models.DatabaseStuff.PetData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class that connects and interacts with a SQL database.
 */
public class SqlitePetDatabaseConnection {
    private static Connection instance = null;

    private SqlitePetDatabaseConnection() {
        String url = "jdbc:sqlite:pets_database.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    /**
     * Method that gets an instance of the SQL connection
     * @return The instance of the connection
     */
    public static Connection getInstance() {
        if (instance == null) {
            new SqlitePetDatabaseConnection();
        }
        return instance;
    }
}