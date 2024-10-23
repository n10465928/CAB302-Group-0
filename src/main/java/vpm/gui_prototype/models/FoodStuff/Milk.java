package vpm.gui_prototype.models.FoodStuff;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the Milk food type for pets, extending the Food class.
 * This class provides nutritional information and compatibility details for milk as pet food.
 */
public class Milk extends Food {
    private static final String foodType = "Milk";  // Type of food
    private static final Float defaultFluctuation = 2f;  // Default fluctuation value for nutritional content
    private static final List<String> defaultCompatiblePets = Arrays.asList("Cat", "Dog");  // Compatible pet types

    /**
     * Default constructor for Milk class.
     * Initializes with default nutritional values and settings.
     */
    public Milk() {
        super(0f, 0f, defaultFluctuation, foodType, defaultCompatiblePets);
    }

    /**
     * Constructor for Milk class specifying nutritional value bounds.
     *
     * @param nutritionalValueL The lower bound of the nutritional value.
     * @param nutritionalValueR The upper bound of the nutritional value.
     */
    public Milk(Float nutritionalValueL, Float nutritionalValueR) {
        super(nutritionalValueL, nutritionalValueR, defaultFluctuation, foodType, defaultCompatiblePets);
    }

    /**
     * Constructor for Milk class specifying nutritional value bounds and fluctuation.
     *
     * @param nutritionalValueL The lower bound of the nutritional value.
     * @param nutritionalValueR The upper bound of the nutritional value.
     * @param fluctuation The fluctuation range of the nutritional value.
     */
    public Milk(Float nutritionalValueL, Float nutritionalValueR, Float fluctuation) {
        super(nutritionalValueL, nutritionalValueR, fluctuation, foodType, defaultCompatiblePets);
    }
}
