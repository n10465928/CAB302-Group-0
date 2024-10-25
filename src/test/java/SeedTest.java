import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.FoodStuff.Seed;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class SeedTest {

    private Seed defaultSeed;
    private Seed seedWithNutritionalValues;
    private Seed seedWithFluctuation;
    private static final List<String> DEFAULT_COMPATIBLE_PETS = Arrays.asList("Bird", "Fish");

    @BeforeEach
    public void setUp() {
        // Initialize Seed objects before each test
        defaultSeed = new Seed();
        seedWithNutritionalValues = new Seed(5f, 10f);
        seedWithFluctuation = new Seed(5f, 10f, 1.5f);
    }

    @Test
    public void testDefaultConstructor() {
        // Verify default nutritional values
        assertEquals(0f, defaultSeed.GetNutritionalValueL());
        assertEquals(0f, defaultSeed.GetNutritionalValueR());

        // Verify default fluctuation
        assertEquals(3f, defaultSeed.GetFluctuation());

        // Verify default food type
        assertEquals("Seed", defaultSeed.GetFoodType());

        // Verify default compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, defaultSeed.GetCompatiblePet());
    }

    @Test
    public void testConstructorWithNutritionalValues() {
        // Verify nutritional values are set correctly
        assertEquals(5f, seedWithNutritionalValues.GetNutritionalValueL());
        assertEquals(10f, seedWithNutritionalValues.GetNutritionalValueR());

        // Verify default fluctuation
        assertEquals(3f, seedWithNutritionalValues.GetFluctuation());

        // Verify food type
        assertEquals("Seed", seedWithNutritionalValues.GetFoodType());

        // Verify compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, seedWithNutritionalValues.GetCompatiblePet());
    }

    @Test
    public void testConstructorWithNutritionalValuesAndFluctuation() {
        // Verify nutritional values are set correctly
        assertEquals(5f, seedWithFluctuation.GetNutritionalValueL());
        assertEquals(10f, seedWithFluctuation.GetNutritionalValueR());

        // Verify fluctuation is set correctly
        assertEquals(1.5f, seedWithFluctuation.GetFluctuation());

        // Verify food type
        assertEquals("Seed", seedWithFluctuation.GetFoodType());

        // Verify compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, seedWithFluctuation.GetCompatiblePet());
    }

    @Test
    public void testGetNutritionalValue() {
        // Generate a random nutritional value and check if it's within the expected range
        Float nutritionalValue = seedWithNutritionalValues.GetNutritionalValue();
        assertTrue(nutritionalValue >= 5f && nutritionalValue <= 10f);
    }
}
