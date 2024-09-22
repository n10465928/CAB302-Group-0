import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import vpm.gui_prototype.controllers.LoginController;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.UserStuff.User;

public class loginInputsTest {
    LoginController loginController;
    IUserDAO userDAO;

    @BeforeEach
    void setup(){
        loginController = new LoginController();
        userDAO = new SqliteUserDAO();
    }

    @Test
    void testAllFieldsFilled(){
        String satisfied = "Good";
        assertTrue(loginController.validInputs("user", "pass").equals(satisfied));
    }

    @Test
    void testEachFieldEmpty(){
        String condition1 = "Please input all credentials";
        assertTrue(loginController.validInputs("user", "").equals(condition1));
        assertTrue(loginController.validInputs("", "pass").equals(condition1));
    }

    @Test
    void testMatchingCredentials() {
        // Add new users and test
        User user1 = new User("user1", "qwerty1", "", "");
        userDAO.addUser(user1);
        assertTrue(loginController.verifyUser("kien1", "qwerty1"));
        assertFalse(loginController.verifyUser("kien1", "qwerty"));

        User user2 = new User("user2", "qwerty2", "", "");
        userDAO.addUser(user2);
        assertTrue(loginController.verifyUser("kien2", "qwerty2"));
        assertFalse(loginController.verifyUser("kien2", "qwerty"));

        User user3 = new User("user3", "qwerty3", "", "");
        userDAO.addUser(user3);
        assertTrue(loginController.verifyUser("kien3", "qwerty3"));
        assertFalse(loginController.verifyUser("kien3", "qwerty"));

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
