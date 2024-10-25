

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.PetStuff.Dog;

import static org.junit.jupiter.api.Assertions.*;

class DogTest {
    private Dog dog;

    @BeforeEach
    void setUp() {
        // Set up a default Dog object for testing
        dog = new Dog("Buddy", 4);
    }

    @Test
    void testDogConstructorWithNameAndAge() {
        assertEquals("Buddy", dog.getName());
        assertEquals("Dog", dog.getType());
        assertEquals(4, dog.getAge());
    }

    @Test
    void testDogConstructorWithAllAttributes() {
        Dog dogWithAttributes = new Dog("Max", 5, "Brown", 9.0f, 7.0f, false, "Friendly");

        assertEquals("Max", dogWithAttributes.getName());
        assertEquals(5, dogWithAttributes.getAge());
        assertEquals("Brown", dogWithAttributes.getColour());
        assertEquals(9.0f, dogWithAttributes.getHappiness());
        assertEquals(7.0f, dogWithAttributes.getFoodSatisfaction());
        assertFalse(dogWithAttributes.getIsDirty());
        assertEquals("Friendly", dogWithAttributes.getPersonality());
    }

    @Test
    void testGetHappinessDecrementInterval() {
        assertEquals(5, dog.getHappinessDecrementInterval());
    }

    @Test
    void testGetHungerDecrementInterval() {
        assertEquals(5, dog.getHungerDecrementInterval());
    }

    @Test
    void testHappinessRange() {
        dog.setHappiness(10.0f);
        assertTrue(dog.getHappiness() <= 10 && dog.getHappiness() >= 0);

        dog.setHappiness(0.0f);
        assertTrue(dog.getHappiness() <= 10 && dog.getHappiness() >= 0);
    }

    @Test
    void testFoodSatisfactionRange() {
        dog.setFoodSatisfaction(10.0f);
        assertTrue(dog.getFoodSatisfaction() <= 10 && dog.getFoodSatisfaction() >= 0);

        dog.setFoodSatisfaction(0.0f);
        assertTrue(dog.getFoodSatisfaction() <= 10 && dog.getFoodSatisfaction() >= 0);
    }
}
