

import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.PetStuff.*;

import static org.junit.jupiter.api.Assertions.*;

class PetFactoryTest {

    @Test
    void testCreateDog() {
        Pet dog = PetFactory.createPet("dog", "Buddy", 3);
        assertNotNull(dog, "Dog object should not be null");
        assertTrue(dog instanceof Dog, "Expected instance of Dog");
        assertEquals("Buddy", dog.getName(), "Dog name should be Buddy");
        assertEquals(3, dog.getAge(), "Dog age should be 3");
    }

    @Test
    void testCreateCat() {
        Pet cat = PetFactory.createPet("cat", "Whiskers", 2);
        assertNotNull(cat, "Cat object should not be null");
        assertTrue(cat instanceof Cat, "Expected instance of Cat");
        assertEquals("Whiskers", cat.getName(), "Cat name should be Whiskers");
        assertEquals(2, cat.getAge(), "Cat age should be 2");
    }

    @Test
    void testCreateBird() {
        Pet bird = PetFactory.createPet("bird", "Tweety", 1);
        assertNotNull(bird, "Bird object should not be null");
        assertTrue(bird instanceof Bird, "Expected instance of Bird");
        assertEquals("Tweety", bird.getName(), "Bird name should be Tweety");
        assertEquals(1, bird.getAge(), "Bird age should be 1");
    }

    @Test
    void testCreateFish() {
        Pet fish = PetFactory.createPet("fish", "Goldie", 1);
        assertNotNull(fish, "Fish object should not be null");
        assertTrue(fish instanceof Fish, "Expected instance of Fish");
        assertEquals("Goldie", fish.getName(), "Fish name should be Goldie");
        assertEquals(1, fish.getAge(), "Fish age should be 1");
    }

    @Test
    void testCreateUnknownType() {
        // Assert that an IllegalArgumentException is thrown with an unknown pet type
        assertThrows(IllegalArgumentException.class, () -> {
            PetFactory.createPet("unknown", "Mystery", 5);
        });
    }
}
