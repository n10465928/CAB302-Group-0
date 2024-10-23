package vpm.gui_prototype.models.FoodStuff;

import java.util.Arrays;
import java.util.List;

public class Pellets extends Food {
    private static final String foodType = "Pallets";
    private static final Float defaultFluctuation = 1f;
    private static final List<String> defaultCompatiblePets = Arrays.asList("Cat", "Dog", "Bird", "Fish");

    /**
     * Default constructor
     */
    public Pellets() {
        super(0f, 0f, defaultFluctuation, foodType, defaultCompatiblePets);
    }
    /**
     * Constructor for Pallets class food
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     */
    public Pellets(Float nutritionalValueL, Float nutritionalValueR) {
        super(nutritionalValueL, nutritionalValueR, defaultFluctuation, foodType, defaultCompatiblePets);
    }
    /**
     * Constructor for Pallets class food
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     * @param fluctuation the fluctuation range of nutritionalValue
     */
    public Pellets(Float nutritionalValueL, Float nutritionalValueR, Float fluctuation) {
        super(nutritionalValueL, nutritionalValueR, fluctuation, foodType, defaultCompatiblePets);
    }
}
