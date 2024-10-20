package vpm.gui_prototype.models.FoodStuff;

import vpm.gui_prototype.models.PetStuff.Cat;
import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.Arrays;
import java.util.List;

public class Bone extends Food {
    private static final String foodType = "Bone";
    private static final Float defaultFluctuation = 2f;
    private static final List<String> defaultCompatiblePets = Arrays.asList("Cat", "Dog");

    /**
     * Default constructor
     */
    public Bone() {
        super(0f, 0f, defaultFluctuation, foodType, defaultCompatiblePets);
    }
    /**
     * Constructor for Bone class food
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     */
    public Bone(Float nutritionalValueL, Float nutritionalValueR) {
        super(nutritionalValueL, nutritionalValueR, defaultFluctuation, foodType, defaultCompatiblePets);
    }

    /**
     * Constructor for Bone class food
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     * @param fluctuation the fluctuation range of nutritionalValue
     */
    public Bone(Float nutritionalValueL, Float nutritionalValueR, Float fluctuation) {
        super(nutritionalValueL, nutritionalValueR, fluctuation, foodType, defaultCompatiblePets);
    }
}
