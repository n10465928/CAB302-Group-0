import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import vpm.gui_prototype.controllers.LoginController;

public class loginInputsTest {
    LoginController loginController;

    @BeforeEach
    void setup(){
        loginController = new LoginController();
    }

    @Test
    void testAllFieldsFilled(){
        assertTrue(loginController.validInputs("user", "pass"));
    }

    @Test
    void testEachFieldEmpty(){
        assertFalse(loginController.validInputs("user", ""));
        assertFalse(loginController.validInputs("", "pass"));
    }
}
