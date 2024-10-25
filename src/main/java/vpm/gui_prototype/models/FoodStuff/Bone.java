package vpm.gui_prototype.models.FoodStuff;

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

    // Nutritional values
    private Float nutritionalValueL;
    private Float nutritionalValueR;
    private Float fluctuation;

    /**
     * Default constructor that initializes a Bone object with default values.
     */
    public Bone() {
        // Call the superclass constructor with default values
        super(0f, 0f, defaultFluctuation, foodType, defaultCompatiblePets);
        this.nutritionalValueL = 0f;
        this.nutritionalValueR = 0f;
        this.fluctuation = defaultFluctuation;
    }

    /**
     * Constructor for creating a Bone food item with specified nutritional values.
     *
     * @param nutritionalValueL The lower bound of the nutritional value.
     * @param nutritionalValueR The upper bound of the nutritional value.
     */
    public Bone(Float nutritionalValueL, Float nutritionalValueR) {
        super(nutritionalValueL, nutritionalValueR, defaultFluctuation, foodType, defaultCompatiblePets);
        this.nutritionalValueL = nutritionalValueL;
        this.nutritionalValueR = nutritionalValueR;
        this.fluctuation = defaultFluctuation;
    }

    /**
     * Constructor for creating a Bone food item with specified nutritional values and fluctuation.
     *
     * @param nutritionalValueL The lower bound of the nutritional value.
     * @param nutritionalValueR The upper bound of the nutritional value.
     * @param fluctuation The fluctuation range of the nutritional value.
     */
    public Bone(Float nutritionalValueL, Float nutritionalValueR, Float fluctuation) {
        super(nutritionalValueL, nutritionalValueR, fluctuation, foodType, defaultCompatiblePets);
        this.nutritionalValueL = nutritionalValueL;
        this.nutritionalValueR = nutritionalValueR;
        this.fluctuation = fluctuation;
    }

    // Getter methods
    public Float getNutritionalValueL() {
        return nutritionalValueL;
    }

    public Float getNutritionalValueR() {
        return nutritionalValueR;
    }

    public Float getFluctuation() {
        return fluctuation;
    }

    public String getFoodType() {
        return foodType;
    }

    public List<String> getCompatiblePets() {
        return defaultCompatiblePets;
    }
}
