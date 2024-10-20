package vpm.gui_prototype.models.FoodStuff;

import vpm.gui_prototype.models.PetStuff.Cat;
import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.Arrays;
import java.util.List;

public class Pallets extends Food {
    private static final String foodType = "Pallets";
    private static final Float defaultFluctuation = 1f;
    private static final List<String> defaultCompatiblePets = Arrays.asList("Cat", "Dog", "Bird", "Fish");

    /**
     * Default constructor
     */
    public Pallets() {
        super(0f, 0f, defaultFluctuation, foodType, defaultCompatiblePets);
    }
    /**
     * Constructor for Pallets class food
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     */
    public Pallets(Float nutritionalValueL, Float nutritionalValueR) {
        super(nutritionalValueL, nutritionalValueR, defaultFluctuation, foodType, defaultCompatiblePets);
    }
    /**
     * Constructor for Pallets class food
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     * @param fluctuation the fluctuation range of nutritionalValue
     */
    public Pallets(Float nutritionalValueL, Float nutritionalValueR, Float fluctuation) {
        super(nutritionalValueL, nutritionalValueR, fluctuation, foodType, defaultCompatiblePets);
    }
}
