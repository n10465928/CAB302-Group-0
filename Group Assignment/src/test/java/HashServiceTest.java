import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.services.PasswordHashingService;

import static org.junit.jupiter.api.Assertions.*;

public class HashServiceTest {
    PasswordHashingService hashService;
    @BeforeEach
    void setup() {
        hashService = new PasswordHashingService();
    }

    @Test
    void testCorrectness() {
        assertEquals(hashService.getHash("qwerty"), hashService.getHash("qwerty"));
        assertEquals(hashService.getHash("qwerty1"), hashService.getHash("qwerty1"));
        assertNotEquals(hashService.getHash("qwerty"), hashService.getHash("qwerty1"));
    }
}
