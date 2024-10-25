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
}
