

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.Constants.Constants;
import vpm.gui_prototype.models.PetStuff.Fish;

import static org.junit.jupiter.api.Assertions.*;

public class FishTest {

    private Fish testFish;

    @BeforeEach
    public void setUp() {
        testFish = new Fish("Nemo", 2); // Create a new Fish instance
    }

    @Test
    public void testFishConstructor_WithValidAge() {
        assertEquals("Nemo", testFish.getName());
        assertEquals("Fish", testFish.getType());
        assertEquals(2, testFish.getAge());
        assertNull(testFish.getColour());
        assertEquals(Constants.MAX_HAPPINESS / 2, testFish.getHappiness());
        assertEquals(Constants.MAX_FOOD_SATISFACTION / 2, testFish.getFoodSatisfaction());
        assertTrue(testFish.getIsDirty());
        assertNull(testFish.getPersonality());
    }



    @Test
    public void testFishConstructor_WithFullAttributes() {
        Fish fullFish = new Fish("Goldie", 1, "Gold", 8.0f, 9.0f, false, "Friendly");
        assertEquals("Goldie", fullFish.getName());
        assertEquals(1, fullFish.getAge());
        assertEquals("Gold", fullFish.getColour());
        assertEquals(8.0f, fullFish.getHappiness());
        assertEquals(9.0f, fullFish.getFoodSatisfaction());
        assertFalse(fullFish.getIsDirty());
        assertEquals("Friendly", fullFish.getPersonality());
    }

    @Test
    public void testGetHappinessDecrementInterval() {
        assertEquals(10, testFish.getHappinessDecrementInterval());
    }

    @Test
    public void testGetHungerDecrementInterval() {
        assertEquals(7, testFish.getHungerDecrementInterval());
    }
}
