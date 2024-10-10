import org.junit.jupiter.api.*;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SqlitePetDatabaseConnectionTest {

    @BeforeEach
    public void setUp() {
        // Ensure the instance is null before each test
        SqlitePetDatabaseConnection.getInstance();
    }

    @AfterEach
    public void tearDown() {
        // Reset the instance to null after each test
        try {
            Connection conn = SqlitePetDatabaseConnection.getInstance();
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetInstanceNotNull() {
        Connection conn = SqlitePetDatabaseConnection.getInstance();
        assertNotNull(conn, "Connection instance should not be null");
    }

    @Test
    public void testGetInstanceSingleton() {
        Connection conn1 = SqlitePetDatabaseConnection.getInstance();
        Connection conn2 = SqlitePetDatabaseConnection.getInstance();
        assertSame(conn1, conn2, "Both instances should be the same");
    }
}
