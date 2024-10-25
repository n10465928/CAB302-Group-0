

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.PetStuff.Bird;
import vpm.gui_prototype.models.PetStuff.Fish;

import static org.junit.jupiter.api.Assertions.*;

class FishTest {
    private Fish fish;

    @BeforeEach
    void setUp() {
        // Set up a default Bird object for testing
        fish = new Fish("Bubbles", 2);
    }

    @Test
    void testFishConstructorWithNameAndAge() {
        assertEquals("Bubbles", fish.getName());
        assertEquals("Fish", fish.getType());
        assertEquals(2, fish.getAge());
    }

    @Test
    void testFishConstructorWithAllAttributes() {
        Fish fishWithAttributes = new Fish("Nemo", 3, "Green", 7.5f, 5.0f, false, "Friendly");

        assertEquals("Nemo", fishWithAttributes.getName());
        assertEquals(3, fishWithAttributes.getAge());
        assertEquals("Green", fishWithAttributes.getColour());
        assertEquals(7.5f, fishWithAttributes.getHappiness());
        assertEquals(5.0f, fishWithAttributes.getFoodSatisfaction());
        assertFalse(fishWithAttributes.getIsDirty());
        assertEquals("Friendly", fishWithAttributes.getPersonality());
    }

    @Test
    void testGetHappinessDecrementInterval() {
        assertEquals(10, fish.getHappinessDecrementInterval());
    }

    @Test
    void testGetHungerDecrementInterval() {
        assertEquals(7, fish.getHungerDecrementInterval());
    }

    @Test
    void testHappinessRange() {
        fish.setHappiness(10.0f);
        assertTrue(fish.getHappiness() <= 10 && fish.getHappiness() >= 0);

        fish.setHappiness(0.0f);
        assertTrue(fish.getHappiness() <= 10 && fish.getHappiness() >= 0);
    }

    @Test
    void testFoodSatisfactionRange() {
        fish.setFoodSatisfaction(10.0f);
        assertTrue(fish.getFoodSatisfaction() <= 10 && fish.getFoodSatisfaction() >= 0);

        fish.setFoodSatisfaction(0.0f);
        assertTrue(fish.getFoodSatisfaction() <= 10 && fish.getFoodSatisfaction() >= 0);
    }
}
