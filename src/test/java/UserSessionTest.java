import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.UserStuff.UserSession;

public class UserSessionTest {

    @BeforeEach
    public void setUp() {
        // Reset the instance before each test
        UserSession instance = UserSession.getInstance();
        instance.setUserId(0);
    }

    @Test
    public void testGetInstance() {
        UserSession instance1 = UserSession.getInstance();
        UserSession instance2 = UserSession.getInstance();
        assertSame(instance1, instance2, "Instances should be the same");
    }

    @Test
    public void testSetAndGetUserId() {
        UserSession instance = UserSession.getInstance();
        instance.setUserId(123);
        assertEquals(123, instance.getUserId(), "User ID should be 123");
    }
}
