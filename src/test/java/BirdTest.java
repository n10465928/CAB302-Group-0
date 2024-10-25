

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.PetStuff.Bird;

import static org.junit.jupiter.api.Assertions.*;

class BirdTest {
    private Bird bird;

    @BeforeEach
    void setUp() {
        // Set up a default Bird object for testing
        bird = new Bird("Tweety", 2);
    }

    @Test
    void testBirdConstructorWithNameAndAge() {
        assertEquals("Tweety", bird.getName());
        assertEquals("Bird", bird.getType());
        assertEquals(2, bird.getAge());
    }

    @Test
    void testBirdConstructorWithAllAttributes() {
        Bird birdWithAttributes = new Bird("Polly", 3, "Green", 7.5f, 5.0f, false, "Friendly");

        assertEquals("Polly", birdWithAttributes.getName());
        assertEquals(3, birdWithAttributes.getAge());
        assertEquals("Green", birdWithAttributes.getColour());
        assertEquals(7.5f, birdWithAttributes.getHappiness());
        assertEquals(5.0f, birdWithAttributes.getFoodSatisfaction());
        assertFalse(birdWithAttributes.getIsDirty());
        assertEquals("Friendly", birdWithAttributes.getPersonality());
    }

    @Test
    void testGetHappinessDecrementInterval() {
        assertEquals(2, bird.getHappinessDecrementInterval());
    }

    @Test
    void testGetHungerDecrementInterval() {
        assertEquals(1, bird.getHungerDecrementInterval());
    }

    @Test
    void testHappinessRange() {
        bird.setHappiness(10.0f);
        assertTrue(bird.getHappiness() <= 10 && bird.getHappiness() >= 0);

        bird.setHappiness(0.0f);
        assertTrue(bird.getHappiness() <= 10 && bird.getHappiness() >= 0);
    }

    @Test
    void testFoodSatisfactionRange() {
        bird.setFoodSatisfaction(10.0f);
        assertTrue(bird.getFoodSatisfaction() <= 10 && bird.getFoodSatisfaction() >= 0);

        bird.setFoodSatisfaction(0.0f);
        assertTrue(bird.getFoodSatisfaction() <= 10 && bird.getFoodSatisfaction() >= 0);
    }
}
