import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import vpm.gui_prototype.models.UserStuff.UserSession;
import vpm.gui_prototype.services.LoginService;

class LoginServiceTest {

    private LoginService loginService;
    private UserSession userSession;

    @BeforeEach
    void setUp() {
        loginService = new LoginService();
        userSession = UserSession.getInstance();
        userSession.reset(); // Reset user ID before each test
    }

    @Test
    void testLoginSetsUserId() {
        int userId = 123;

        loginService.login(userId);

        // Check that the user ID was set correctly
        assertEquals(userId, userSession.getUserId());
    }

    @Test
    void testLoginWithDifferentUserId() {
        int userId = 456;

        loginService.login(userId);

        // Check that the user ID was set correctly
        assertEquals(userId, userSession.getUserId());
    }

    @Test
    void testLoginCalledTwiceWithDifferentIds() {
        loginService.login(789);
        loginService.login(101);

        // Check that the user ID was set to the last called ID
        assertEquals(101, userSession.getUserId());
    }
}
