package vpm.gui_prototype.models.FoodStuff;

import vpm.gui_prototype.models.PetStuff.Cat;
import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a Bone food item that can be consumed by certain pets.
 * This class extends the Food class and specifies details relevant to Bone.
 */
public class Bone extends Food {
    // Constant defining the type of food
    private static final String foodType = "Bone";
    // Default fluctuation value for nutritional content
    private static final Float defaultFluctuation = 2f;
    // List of pets that can consume this food
    private static final List<String> defaultCompatiblePets = Arrays.asList("Cat", "Dog");

    /**
     * Default constructor that initializes a Bone object with default values.
     */
    public Bone() {
        // Call the superclass constructor with default values
        super(0f, 0f, defaultFluctuation, foodType, defaultCompatiblePets);
    }

    /**
     * Constructor for creating a Bone food item with specified nutritional values.
     *
     * @param nutritionalValueL The lower bound of the nutritional value.
     * @param nutritionalValueR The upper bound of the nutritional value.
     */
    public Bone(Float nutritionalValueL, Float nutritionalValueR) {
        // Call the superclass constructor with the provided nutritional values
        super(nutritionalValueL, nutritionalValueR, defaultFluctuation, foodType, defaultCompatiblePets);
    }

    /**
     * Constructor for creating a Bone food item with specified nutritional values and fluctuation.
     *
     * @param nutritionalValueL The lower bound of the nutritional value.
     * @param nutritionalValueR The upper bound of the nutritional value.
     * @param fluctuation The fluctuation range of the nutritional value.
     */
    public Bone(Float nutritionalValueL, Float nutritionalValueR, Float fluctuation) {
        // Call the superclass constructor with the provided parameters
        super(nutritionalValueL, nutritionalValueR, fluctuation, foodType, defaultCompatiblePets);
    }
}
