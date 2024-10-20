package vpm.gui_prototype.models.FoodStuff;

import vpm.gui_prototype.models.PetStuff.Cat;
import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.Arrays;
import java.util.List;

public class Pate extends Food {
    private static final String foodType = "Pate";
    private static final Float defaultFluctuation = 3f;
    private static final List<String> defaultCompatiblePets = Arrays.asList("Cat", "Dog");

    /**
     * Default constructor
     */
    public Pate() {
        super(0f, 0f, defaultFluctuation, foodType, defaultCompatiblePets);
    }
    /**
     * Constructor for Pate class food
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     */
    public Pate(Float nutritionalValueL, Float nutritionalValueR) {
        super(nutritionalValueL, nutritionalValueR, defaultFluctuation, foodType, defaultCompatiblePets);
    }
    /**
     * Constructor for Pate class food
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     * @param fluctuation the fluctuation range of nutritionalValue
     */
    public Pate(Float nutritionalValueL, Float nutritionalValueR, Float fluctuation) {
        super(nutritionalValueL, nutritionalValueR, fluctuation, foodType, defaultCompatiblePets);
    }
}
