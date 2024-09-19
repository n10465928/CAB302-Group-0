import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import vpm.gui_prototype.controllers.RegisterController;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.UserStuff.User;

public class registerInputsTest {
    RegisterController registerController;
    IUserDAO userDAO;

    @BeforeEach
    void setup(){
        registerController = new RegisterController();
        userDAO = new SqliteUserDAO();
    }

    @Test
    void testAllFieldsFilled(){
        assertTrue(registerController.fieldsFilled("user", "pass", "pass"));
    }

    @Test
    void testExistingAccount() {
        // Add new users and test
        User user1 = new User("user1", "qwerty1", "", "");
        userDAO.addUser(user1);
        assertTrue(registerController.checkExistingUser("kien1"));

        User user2 = new User("user2", "qwerty2", "", "");
        userDAO.addUser(user2);
        assertTrue(registerController.checkExistingUser("kien2"));

        User user3 = new User("user3", "qwerty3", "", "");
        userDAO.addUser(user3);
        assertTrue(registerController.checkExistingUser("kien3"));

        // Removes after testing
        userDAO.deleteUser(user1);
        userDAO.deleteUser(user2);
        userDAO.deleteUser(user3);

        // Test non-existing usernames
        assertFalse(registerController.checkExistingUser("empty1"));
        assertFalse(registerController.checkExistingUser("empty2"));
        assertFalse(registerController.checkExistingUser("empty3"));
    }

    @Test
    void testEachFieldEmpty(){
        assertFalse(registerController.fieldsFilled("", "pass", "pass"));
        assertFalse(registerController.fieldsFilled("user", "", "pass"));
        assertFalse(registerController.fieldsFilled("user", "pass", ""));

        assertFalse(registerController.fieldsFilled("", "", "pass"));
        assertFalse(registerController.fieldsFilled("user", "", ""));
        assertFalse(registerController.fieldsFilled("", "", "pass"));
        assertFalse(registerController.fieldsFilled("", "", ""));
    }

    @Test
    void testMatchingPasswords(){
        assertTrue(registerController.matchingPassword("pass", "pass"));
    }

    @Test
    void testMismatchedPassword(){
        assertFalse(registerController.matchingPassword("pass", "pas"));
        assertFalse(registerController.matchingPassword("pass", ""));
    }

    @Test
    void testReadyInputs(){
        assertTrue(registerController.inputsReady("user", "pass", "pass"));
    }

    @Test
    void testUnreadyInputs(){
        // assertTrue(registerController.inputsReady("user", "pass", ""));
        // assertFalse(registerController.inputsReady("user", "pass", "pass1"));
    }
}
