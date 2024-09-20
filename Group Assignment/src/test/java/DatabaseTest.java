import org.junit.jupiter.api.*;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDatabaseConnection;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseTest {
    @Test
    public void testConnection() {
        Connection conn = SqlitePetDatabaseConnection.getInstance();
        assertEquals(true, conn != null);
    }
}
