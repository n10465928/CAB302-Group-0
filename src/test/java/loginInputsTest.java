import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import vpm.gui_prototype.controllers.LoginController;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.UserStuff.User;
import vpm.gui_prototype.services.PasswordHashingService;

public class loginInputsTest {
    LoginController loginController;
    IUserDAO userDAO;
    PasswordHashingService hashService;

    @BeforeEach
    void setup(){
        loginController = new LoginController();
        userDAO = new SqliteUserDAO();
        hashService = new PasswordHashingService();
    }

    @Test
    void testAllFieldsFilled(){
        String satisfied = "Good";
        assertEquals(loginController.validInputs("user", "pass"), satisfied);
    }

    @Test
    void testEachFieldEmpty(){
        String condition1 = "Please input all credentials";
        assertEquals(loginController.validInputs("user", ""), condition1);
        assertEquals(loginController.validInputs("", "pass"), condition1);
    }

    @Test
    void testMatchingCredentials() {
        // Add new users and test
        String password = null;
        String storedPassword = null;

        password = "qwerty1";
        storedPassword = hashService.getHash(password);
        User user1 = new User("user1", storedPassword, "", "");
        userDAO.addUser(user1);
        System.out.println(storedPassword);
        assertTrue(loginController.verifyUser("user1", "qwerty1"));
        assertFalse(loginController.verifyUser("user1", "qwerty"));

        password = "qwerty2";
        storedPassword = hashService.getHash(password);
        User user2 = new User("user2", storedPassword, "", "");
        userDAO.addUser(user2);
        assertTrue(loginController.verifyUser("user2", "qwerty2"));
        assertFalse(loginController.verifyUser("user2", "qwerty"));

        password = "qwerty3";
        storedPassword = hashService.getHash(password);
        User user3 = new User("user3", storedPassword, "", "");
        userDAO.addUser(user3);
        assertTrue(loginController.verifyUser("user3", "qwerty3"));
        assertFalse(loginController.verifyUser("user3", "qwerty"));

        // Removes after testing
        userDAO.deleteUser(user1);
        userDAO.deleteUser(user2);
        userDAO.deleteUser(user3);

        // Test non-existing usernames
        assertFalse(loginController.verifyUser("empty1", "qwerty"));
        assertFalse(loginController.verifyUser("empty2", "qwerty"));
        assertFalse(loginController.verifyUser("empty3", "qwerty"));
    }
}
