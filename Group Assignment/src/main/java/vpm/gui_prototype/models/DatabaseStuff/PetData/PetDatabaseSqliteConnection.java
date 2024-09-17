package vpm.gui_prototype.models.DatabaseStuff.PetData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PetDatabaseSqliteConnection {
    private static Connection instance = null;

    private PetDatabaseSqliteConnection() {
        String url = "jdbc:sqlite:pet_database.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new PetDatabaseSqliteConnection();
        }
        return instance;
    }
}