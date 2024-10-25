import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.FoodStuff.Milk;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class MilkTest {

    private Milk defaultMilk;
    private Milk milkWithNutritionalValues;
    private Milk milkWithFluctuation;
    private static final List<String> DEFAULT_COMPATIBLE_PETS = Arrays.asList("Cat", "Dog");

    @BeforeEach
    public void setUp() {
        // Initialize Milk objects before each test
        defaultMilk = new Milk();
        milkWithNutritionalValues = new Milk(5f, 10f);
        milkWithFluctuation = new Milk(5f, 10f, 1.5f);
    }

    @Test
    public void testDefaultConstructor() {
        // Verify default nutritional values
        assertEquals(0f, defaultMilk.GetNutritionalValueL());
        assertEquals(0f, defaultMilk.GetNutritionalValueR());

        // Verify default fluctuation
        assertEquals(2f, defaultMilk.GetFluctuation());

        // Verify default food type
        assertEquals("Milk", defaultMilk.GetFoodType());

        // Verify default compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, defaultMilk.GetCompatiblePet());
    }

    @Test
    public void testConstructorWithNutritionalValues() {
        // Verify nutritional values are set correctly
        assertEquals(5f, milkWithNutritionalValues.GetNutritionalValueL());
        assertEquals(10f, milkWithNutritionalValues.GetNutritionalValueR());

        // Verify default fluctuation
        assertEquals(2f, milkWithNutritionalValues.GetFluctuation());

        // Verify food type
        assertEquals("Milk", milkWithNutritionalValues.GetFoodType());

        // Verify compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, milkWithNutritionalValues.GetCompatiblePet());
    }

    @Test
    public void testConstructorWithNutritionalValuesAndFluctuation() {
        // Verify nutritional values are set correctly
        assertEquals(5f, milkWithFluctuation.GetNutritionalValueL());
        assertEquals(10f, milkWithFluctuation.GetNutritionalValueR());

        // Verify fluctuation is set correctly
        assertEquals(1.5f, milkWithFluctuation.GetFluctuation());

        // Verify food type
        assertEquals("Milk", milkWithFluctuation.GetFoodType());

        // Verify compatible pets
        assertEquals(DEFAULT_COMPATIBLE_PETS, milkWithFluctuation.GetCompatiblePet());
    }

    @Test
    public void testGetNutritionalValue() {
        // Generate a random nutritional value and check if it's within the expected range
        Float nutritionalValue = milkWithNutritionalValues.GetNutritionalValue();
        assertTrue(nutritionalValue >= 5f && nutritionalValue <= 10f);
    }
}
