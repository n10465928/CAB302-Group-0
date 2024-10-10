import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import vpm.gui_prototype.controllers.RegisterController;
import vpm.gui_prototype.models.Constants.Constants;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.UserStuff.User;

import java.io.Console;

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
        assertTrue(registerController.checkExistingUser("user1"));

        User user2 = new User("user2", "qwerty2", "", "");
        userDAO.addUser(user2);
        assertTrue(registerController.checkExistingUser("user2"));

        User user3 = new User("user3", "qwerty3", "", "");
        userDAO.addUser(user3);
        assertTrue(registerController.checkExistingUser("user3"));

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
    void testEvaluatePassword() {
        String condition1Reply = "Password must be at least 8 characters long";
        String condition2Reply = "Password must have an uppercase letter";
        String condition3Reply = "Password must have a special character";
        String satisfied = "Good";

        // Condition 1:
        assert(registerController.evaluatePassword("User01*").equals(condition1Reply));
        assert(registerController.evaluatePassword("sSer02*").equals(condition1Reply));
        assert(registerController.evaluatePassword("*usEr").equals(condition1Reply));

        // Condition 2:
        assert(registerController.evaluatePassword("asdasdser01*").equals(condition2Reply));
        assert(registerController.evaluatePassword("ssweq*_er02*").equals(condition2Reply));
        assert(registerController.evaluatePassword("asdljaldk***(").equals(condition2Reply));

        // Condition 3:
        assert(registerController.evaluatePassword("Asdasdser01").equals(condition3Reply));
        assert(registerController.evaluatePassword("sswQESer02").equals(condition3Reply));
        assert(registerController.evaluatePassword("asdljKSHW02").equals(condition3Reply));

        // Satisfied:
        assert(registerController.evaluatePassword("User0123*").equals(satisfied));
        assert(registerController.evaluatePassword("userE01(_").equals(satisfied));
        assert(registerController.evaluatePassword("!WASd239s0").equals(satisfied));
    }

    @Test
    void testEvaluateUsername() {
        String condition1Reply = "Username must be from 6 to 20 characters long";
        String condition2Reply = "Username must not contain special characters";
        String satisfied = "Good";

        // Condition 1:
        assert(registerController.evaluateUsername("Use02").equals(condition1Reply));
        assert(registerController.evaluateUsername("asdkjakshflkasjdhfklasdfkjlkhaskjfdhsd").equals(condition1Reply));
        assert(registerController.evaluateUsername("I_really_want_a_7_please_give_me_one").equals(condition1Reply));

        // Condition 2:
        assert(registerController.evaluateUsername("Use02asd*").equals(condition2Reply));
        assert(registerController.evaluateUsername("I_want_a_7").equals(condition2Reply));
        assert(registerController.evaluateUsername("**hehehehe**").equals(condition2Reply));

        // Satisfied:
        assert(registerController.evaluateUsername("KienIsHandsome").equals(satisfied));
        assert(registerController.evaluateUsername("ILoveGroup0TheMost").equals(satisfied));
        assert(registerController.evaluateUsername("iwanttopassthisunit").equals(satisfied));
    }

    @Test
    void testEachFieldEmpty(){
        assertFalse(registerController.fieldsFilled("", "pass", "pass"));
        assertFalse(registerController.fieldsFilled("user", "", "pass"));
        assertFalse(registerController.fieldsFilled("user", "pass", ""));

        assertFalse(registerController.fieldsFilled("", "", "pass"));
        assertFalse(registerController.fieldsFilled("user", "", ""));
        assertFalse(registerController.fieldsFilled("", "pass", ""));
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
    void testInputs(){
         String condition1Reply = "Please ensure you fill in all fields";
         String condition2Reply = "Passwords do not match";
         String satisfied = "Good";

         // Condition1:
         assertTrue(registerController.inputsReady("user", "pass", "").equals(condition1Reply));
         assertTrue(registerController.inputsReady("user", "", "pass").equals(condition1Reply));
         assertTrue(registerController.inputsReady("", "pass", "pass").equals(condition1Reply));

         // Condition2:
         assertTrue(registerController.inputsReady("user", "pass", "pass2").equals(condition2Reply));
         assertTrue(registerController.inputsReady("user", "pass", "pass2").equals(condition2Reply));
         assertTrue(registerController.inputsReady("user", "pass1", "pass").equals(condition2Reply));

         // Satisfied:
         assertTrue(registerController.inputsReady("user", "pass", "pass").equals(satisfied));
    }
}
