

import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.PetStuff.PetBuilder;

import static org.junit.jupiter.api.Assertions.*;

public class PetBuilderTest {

    @Test
    public void testBuildDog() {
        PetBuilder builder = new PetBuilder();
        Pet dog = builder.setName("Buddy")
                .setType("Dog")
                .setAge(3)
                .setColour("Brown")
                .setHappiness(7.0f)
                .setFoodSatisfaction(8.0f)
                .setIsDirty(false)
                .setPersonality("Playful")
                .build();

        assertEquals("Buddy", dog.getName());
        assertEquals("Dog", dog.getType());
        assertEquals(3, dog.getAge());
        assertEquals("Brown", dog.getColour());
        assertEquals(7.0f, dog.getHappiness());
        assertEquals(8.0f, dog.getFoodSatisfaction());
        assertFalse(dog.getIsDirty());
        assertEquals("Playful", dog.getPersonality());
    }

    @Test
    public void testBuildCat() {
        PetBuilder builder = new PetBuilder();
        Pet cat = builder.setName("Whiskers")
                .setType("Cat")
                .build();

        assertEquals("Whiskers", cat.getName());
        assertEquals("Cat", cat.getType());
        assertEquals(1, cat.getAge()); // Default age
        assertEquals("Brown", cat.getColour()); // Default colour
        assertEquals(5.0f, cat.getHappiness()); // Default happiness
        assertEquals(5.0f, cat.getFoodSatisfaction()); // Default food satisfaction
        assertFalse(cat.getIsDirty()); // Default dirty status
        assertEquals("Unknown", cat.getPersonality()); // Default personality
    }

    @Test
    public void testBuildWithMissingName() {
        PetBuilder builder = new PetBuilder();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            builder.setType("Dog").build();
        });
        assertEquals("Name must be set before building a Pet.", exception.getMessage());
    }

    @Test
    public void testBuildWithMissingType() {
        PetBuilder builder = new PetBuilder();
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            builder.setName("Buddy").build();
        });
        assertEquals("Type must be set before building a Pet.", exception.getMessage());
    }

    @Test
    public void testBuildWithUnsupportedPetType() {
        PetBuilder builder = new PetBuilder();
        builder.setName("UnknownPet").setType("Dragon");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            builder.build();
        });
        assertEquals("Unsupported pet type: Dragon", exception.getMessage());
    }
}
