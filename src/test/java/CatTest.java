

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.PetStuff.Cat;

import static org.junit.jupiter.api.Assertions.*;

class CatTest {
    private Cat cat;

    @BeforeEach
    void setUp() {
        // Set up a default Cat object for testing
        cat = new Cat("Whiskers", 3);
    }

    @Test
    void testCatConstructorWithNameAndAge() {
        assertEquals("Whiskers", cat.getName());
        assertEquals("Cat", cat.getType());
        assertEquals(3, cat.getAge());
    }

    @Test
    void testCatConstructorWithAllAttributes() {
        Cat catWithAttributes = new Cat("Luna", 4, "Black", 8.0f, 6.0f, true, "Playful");

        assertEquals("Luna", catWithAttributes.getName());
        assertEquals(4, catWithAttributes.getAge());
        assertEquals("Black", catWithAttributes.getColour());
        assertEquals(8.0f, catWithAttributes.getHappiness());
        assertEquals(6.0f, catWithAttributes.getFoodSatisfaction());
        assertTrue(catWithAttributes.getIsDirty());
        assertEquals("Playful", catWithAttributes.getPersonality());
    }

    @Test
    void testGetHappinessDecrementInterval() {
        assertEquals(8, cat.getHappinessDecrementInterval());
    }

    @Test
    void testGetHungerDecrementInterval() {
        assertEquals(6, cat.getHungerDecrementInterval());
    }

    @Test
    void testHappinessRange() {
        cat.setHappiness(10.0f);
        assertTrue(cat.getHappiness() <= 10 && cat.getHappiness() >= 0);

        cat.setHappiness(0.0f);
        assertTrue(cat.getHappiness() <= 10 && cat.getHappiness() >= 0);
    }

    @Test
    void testFoodSatisfactionRange() {
        cat.setFoodSatisfaction(10.0f);
        assertTrue(cat.getFoodSatisfaction() <= 10 && cat.getFoodSatisfaction() >= 0);

        cat.setFoodSatisfaction(0.0f);
        assertTrue(cat.getFoodSatisfaction() <= 10 && cat.getFoodSatisfaction() >= 0);
    }
}
