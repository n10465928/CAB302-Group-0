import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import vpm.gui_prototype.controllers.RegisterController;

public class registerInputsTest {
    RegisterController registerController;

    @BeforeEach
    void setup(){
        registerController = new RegisterController();
    }

    @Test
    void testAllFieldsFilled(){
        assertTrue(registerController.fieldsFilled("user", "pass", "pass"));
    }

    @Test
    void testEachFieldEmpty(){
        assertFalse(registerController.fieldsFilled("", "pass", "pass"));
        assertFalse(registerController.fieldsFilled("user", "", "pass"));
        assertFalse(registerController.fieldsFilled("user", "pass", ""));
    }

    @Test
    void testMatchingPasswords(){
        assertTrue(registerController.matchingPassword("pass", "pass"));
    }

    @Test
    void testMismatchedPassword(){
        assertFalse(registerController.matchingPassword("pass", "pas"));
    }

    @Test
    void testReadyInputs(){
        assertTrue(registerController.inputsReady("user", "pass", "pass"));
    }

    @Test
    void testUnreadyInputs(){
        assertFalse(registerController.inputsReady("user", "pass", ""));
        assertFalse(registerController.inputsReady("user", "pass", "pass1"));
    }
}
