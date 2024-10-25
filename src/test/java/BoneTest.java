import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.FoodStuff.Bone;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class BoneTest {

    private static final List<String> DEFAULT_COMPATIBLE_PETS = Arrays.asList("Cat", "Dog");

    @BeforeEach
    public void setUp() {
        // This method is executed before each test
    }

    @Test
    public void testDefaultConstructor() {
        // Create a Bone object using the default constructor
        Bone bone = new Bone();

        // Verify default nutritional values
        assertEquals(0f, bone.getNutritionalValueL());
        assertEquals(0f, bone.getNutritionalValueR());

        // Verify default fluctuation
        assertEquals(2f, bone.getFluctuation());

        // Verify default food type
        assertEquals("Bone", bone.getFoodType());

        // Verify default compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, bone.getCompatiblePets());
    }

    @Test
    public void testParameterizedConstructor_NutritionalValues() {
        // Create a Bone object with specific nutritional values
        Bone bone = new Bone(5f, 10f);

        // Verify that the nutritional values were set correctly
        assertEquals(5f, bone.getNutritionalValueL());
        assertEquals(10f, bone.getNutritionalValueR());

        // Verify default fluctuation
        assertEquals(2f, bone.getFluctuation());

        // Verify food type
        assertEquals("Bone", bone.getFoodType());

        // Verify compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, bone.getCompatiblePets());
    }

    @Test
    public void testParameterizedConstructor_NutritionalValuesAndFluctuation() {
        // Create a Bone object with specific nutritional values and fluctuation
        Bone bone = new Bone(5f, 10f, 1.5f);

        // Verify that the nutritional values were set correctly
        assertEquals(5f, bone.getNutritionalValueL());
        assertEquals(10f, bone.getNutritionalValueR());

        // Verify that the fluctuation was set correctly
        assertEquals(1.5f, bone.getFluctuation());

        // Verify food type
        assertEquals("Bone", bone.getFoodType());

        // Verify compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, bone.getCompatiblePets());
    }
}
