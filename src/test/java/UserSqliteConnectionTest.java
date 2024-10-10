import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.DatabaseStuff.UserData.UserSqliteConnection;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserSqliteConnectionTest {

    @BeforeEach
    public void setUp() {
        // Ensure the instance is null before each test
        UserSqliteConnection.getInstance();
    }

    @AfterEach
    public void tearDown() {
        // Reset the instance to null after each test
        try {
            Connection conn = UserSqliteConnection.getInstance();
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetInstanceNotNull() {
        Connection conn = UserSqliteConnection.getInstance();
        assertNotNull(conn, "Connection instance should not be null");
    }

    @Test
    public void testGetInstanceSingleton() {
        Connection conn1 = UserSqliteConnection.getInstance();
        Connection conn2 = UserSqliteConnection.getInstance();
        assertSame(conn1, conn2, "Both instances should be the same");
    }

    @Test
    public void testConnection() {
        Connection conn = UserSqliteConnection.getInstance();
        assertEquals(true, conn != null);
    }
}
