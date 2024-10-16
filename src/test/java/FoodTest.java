import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.FoodStuff.Food;

import static org.junit.jupiter.api.Assertions.*;

public class FoodTest {

    @Test
    public void testDefaultConstructor() {
        Food food = new Food();
        assertEquals(0f, food.GetNutritionalValue());
    }

    @Test
    public void testParameterizedConstructor() {
        Float nutritionalValue = 10f;
        Food food = new Food(nutritionalValue);
        assertEquals(nutritionalValue, food.GetNutritionalValue());
    }

    @Test
    public void testGetNutritionalValue() {
        Float nutritionalValue = 20f;
        Food food = new Food(nutritionalValue);
        assertEquals(nutritionalValue, food.GetNutritionalValue());
    }

    @Test
    public void testFullParameterizedConstructor() {
        Float nutritionalValue = 10f;
        Food food = new Food(nutritionalValue);
        assertEquals(nutritionalValue, food.GetNutritionalValue());
    }

    @Test
    public void testGetFoodType() {
        Float nutritionalValue = 20f;
        String type = "Dog";
        Food food = new Food(nutritionalValue, type);
        assertEquals(type, food.GetFoodType());
    }
}
