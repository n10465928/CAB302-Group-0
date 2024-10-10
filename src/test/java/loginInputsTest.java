import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import vpm.gui_prototype.controllers.LoginController;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.UserStuff.User;
import vpm.gui_prototype.services.PasswordHashingService;

import java.util.List;

public class loginInputsTest {
    LoginController loginController;
    IUserDAO userDAO;
    PasswordHashingService hashService;

    @BeforeEach
    void setup(){
        loginController = new LoginController();
        userDAO = new SqliteUserDAO();
        hashService = new PasswordHashingService();

        // Clean up any existing users
        List<User> allUsers = userDAO.getAllUsers();
        for (User user : allUsers) {
            userDAO.deleteUser(user);
        }
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

    /*
    @Test
    void testMatchingCredentials() {
        String password = "qwerty1";
        String storedPassword = hashService.getHash(password);
        User user1 = new User("user1", storedPassword, "", "");

        // Add user and confirm addition
        userDAO.addUser(user1);
        System.out.println("Trying to retrieve user1 after addition...");
        User retrievedUser1 = userDAO.getUserByUsername("user1");

        // Assert user was added
        assertNotNull(retrievedUser1, "User1 should exist after being added.");
        assertEquals(user1.getUsername(), retrievedUser1.getUsername(), "Retrieved username should match.");

        // Verify password
        assertTrue(loginController.verifyUser("user1", password), "User1 should match with correct password.");
        assertFalse(loginController.verifyUser("user1", "wrongpass"), "User1 should not match with wrong password.");

        // Cleanup
        userDAO.deleteUser(user1);
    }

     */

}
