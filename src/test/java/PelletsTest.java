import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.FoodStuff.Pellets;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class PelletsTest {

    private Pellets defaultPellets;
    private Pellets pelletsWithNutritionalValues;
    private Pellets pelletsWithFluctuation;
    private static final List<String> DEFAULT_COMPATIBLE_PETS = Arrays.asList("Cat", "Dog", "Bird", "Fish");

    @BeforeEach
    public void setUp() {
        // Initialize Pellets objects before each test
        defaultPellets = new Pellets();
        pelletsWithNutritionalValues = new Pellets(5f, 10f);
        pelletsWithFluctuation = new Pellets(5f, 10f, 0.5f);
    }

    @Test
    public void testDefaultConstructor() {
        // Verify default nutritional values
        assertEquals(0f, defaultPellets.GetNutritionalValueL());
        assertEquals(0f, defaultPellets.GetNutritionalValueR());

        // Verify default fluctuation
        assertEquals(1f, defaultPellets.GetFluctuation());

        // Verify default food type
        assertEquals("Pallets", defaultPellets.GetFoodType());

        // Verify default compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, defaultPellets.GetCompatiblePet());
    }

    @Test
    public void testConstructorWithNutritionalValues() {
        // Verify nutritional values are set correctly
        assertEquals(5f, pelletsWithNutritionalValues.GetNutritionalValueL());
        assertEquals(10f, pelletsWithNutritionalValues.GetNutritionalValueR());

        // Verify default fluctuation
        assertEquals(1f, pelletsWithNutritionalValues.GetFluctuation());

        // Verify food type
        assertEquals("Pallets", pelletsWithNutritionalValues.GetFoodType());

        // Verify compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, pelletsWithNutritionalValues.GetCompatiblePet());
    }

    @Test
    public void testConstructorWithNutritionalValuesAndFluctuation() {
        // Verify nutritional values are set correctly
        assertEquals(5f, pelletsWithFluctuation.GetNutritionalValueL());
        assertEquals(10f, pelletsWithFluctuation.GetNutritionalValueR());

        // Verify fluctuation is set correctly
        assertEquals(0.5f, pelletsWithFluctuation.GetFluctuation());

        // Verify food type
        assertEquals("Pallets", pelletsWithFluctuation.GetFoodType());

        // Verify compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, pelletsWithFluctuation.GetCompatiblePet());
    }

    @Test
    public void testGetNutritionalValue() {
        // Generate a random nutritional value and check if it's within the expected range
        Float nutritionalValue = pelletsWithNutritionalValues.GetNutritionalValue();
        assertTrue(nutritionalValue >= 5f && nutritionalValue <= 10f);
    }
}
