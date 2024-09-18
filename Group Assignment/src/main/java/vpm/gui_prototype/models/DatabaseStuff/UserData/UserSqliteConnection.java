package vpm.gui_prototype.models.DatabaseStuff.UserData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserSqliteConnection {
    private static Connection instance = null;

    private UserSqliteConnection() {
        String url = "jdbc:sqlite:users.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEX) {
            System.out.println(url);
            System.err.println(sqlEX);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new UserSqliteConnection();
        }
        return instance;
    }
}
