package vpm.gui_prototype.models.FoodStuff;

import vpm.gui_prototype.models.PetStuff.Cat;
import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.Arrays;
import java.util.List;

public class Milk extends Food {
    private static final String foodType = "Milk";
    private static final Float defaultFluctuation = 2f;
    private static final List<String> defaultCompatiblePets = Arrays.asList("Cat", "Dog");

    /**
     * Default constructor
     */
    public Milk() {
        super(0f, 0f, defaultFluctuation, foodType, defaultCompatiblePets);
    }
    /**
     * Constructor for Milk class food
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     */
    public Milk(Float nutritionalValueL, Float nutritionalValueR) {
        super(nutritionalValueL, nutritionalValueR, defaultFluctuation, foodType, defaultCompatiblePets);
    }
    /**
     * Constructor for Milk class food
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     * @param fluctuation the fluctuation range of nutritionalValue
     */
    public Milk(Float nutritionalValueL, Float nutritionalValueR, Float fluctuation) {
        super(nutritionalValueL, nutritionalValueR, fluctuation, foodType, defaultCompatiblePets);
    }
}
