import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.FoodStuff.Bone;
import vpm.gui_prototype.models.FoodStuff.Food;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodTest {

    private TestFood testFood;
    private static final List<String> DEFAULT_COMPATIBLE_PETS = Arrays.asList("Cat", "Dog");

    // Create a simple subclass of Food for testing purposes
    private class TestFood extends Food {
        public TestFood(Float nutritionalValueL, Float nutritionalValueR, Float fluctuation, String foodType, List<String> compatiblePets) {
            super(nutritionalValueL, nutritionalValueR, fluctuation, foodType, compatiblePets);
        }
    }

    @BeforeEach
    public void setUp() {
        // Initialize a TestFood object before each test
        testFood = new TestFood(5f, 10f, 2f, "TestFood", DEFAULT_COMPATIBLE_PETS);
    }

    @Test
    public void testDefaultConstructor() {
        // Create a TestFood object with the default constructor
        TestFood defaultFood = new TestFood(0f, 0f, 0f, "", new ArrayList<>());

        // Check that the default values are set correctly
        assertEquals(0f, defaultFood.GetNutritionalValueL());
        assertEquals(0f, defaultFood.GetNutritionalValueR());
        assertEquals(0f, defaultFood.GetFluctuation());
        assertEquals("", defaultFood.GetFoodType());
        assertTrue(defaultFood.GetCompatiblePet().isEmpty());
    }

    @Test
    public void testParameterizedConstructor_NutritionalValuesAndFoodType() {
        // Check that the constructor with values sets the properties correctly
        assertEquals(5f, testFood.GetNutritionalValueL());
        assertEquals(10f, testFood.GetNutritionalValueR());
        assertEquals(2f, testFood.GetFluctuation());
        assertEquals("TestFood", testFood.GetFoodType());
        assertEquals(DEFAULT_COMPATIBLE_PETS, testFood.GetCompatiblePet());
    }

    @Test
    public void testGetNutritionalValue() {
        // Generate a nutritional value and verify it falls within the expected range
        Float nutritionalValue = testFood.GetNutritionalValue();
        assertTrue(nutritionalValue >= 5f && nutritionalValue <= 10f);
    }

    @Test
    public void testGetCompatiblePets() {
        // Ensure the compatible pets list is returned correctly
        assertEquals(DEFAULT_COMPATIBLE_PETS, testFood.GetCompatiblePet());
    }

    @Test
    public void testGetFluctuation() {
        // Verify the fluctuation value
        assertEquals(2f, testFood.GetFluctuation());
    }

    @Test
    public void testGetNutritionalValueL() {
        // Verify the lower bound of the nutritional value
        assertEquals(5f, testFood.GetNutritionalValueL());
    }

    @Test
    public void testGetNutritionalValueR() {
        // Verify the upper bound of the nutritional value
        assertEquals(10f, testFood.GetNutritionalValueR());
    }

    @Test
    public void testGetFoodType() {
        // Verify the food type
        assertEquals("TestFood", testFood.GetFoodType());
    }
}
