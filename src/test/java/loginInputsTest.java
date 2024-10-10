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
        // User 1
        String password = "qwerty1";
        String storedPassword = hashService.getHash(password);
        User user1 = new User("user1", storedPassword, "", "");
        userDAO.addUser(user1);

        // Confirm the user was added
        User retrievedUser1 = userDAO.getUserByUsername("user1");
        assertNotNull(retrievedUser1, "User1 should exist after being added.");
        assertEquals(storedPassword, retrievedUser1.getPassword(), "Stored password should match.");

        assertTrue(loginController.verifyUser("user1", password), "User1 should match with correct password.");
        assertFalse(loginController.verifyUser("user1", "wrongpass"), "User1 should not match with wrong password.");

        // User 2
        password = "qwerty2";
        storedPassword = hashService.getHash(password);
        User user2 = new User("user2", storedPassword, "", "");
        userDAO.addUser(user2);

        User retrievedUser2 = userDAO.getUserByUsername("user2");
        assertNotNull(retrievedUser2, "User2 should exist after being added.");

        assertTrue(loginController.verifyUser("user2", password), "User2 should match with correct password.");
        assertFalse(loginController.verifyUser("user2", "wrongpass"), "User2 should not match with wrong password.");

        // Cleanup
        userDAO.deleteUser(user1);
        userDAO.deleteUser(user2);

        // Test non-existing usernames
        assertFalse(loginController.verifyUser("empty1", "qwerty"), "Non-existing user should not match.");
    }
}
