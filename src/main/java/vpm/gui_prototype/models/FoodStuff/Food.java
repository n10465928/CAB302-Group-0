package vpm.gui_prototype.models.FoodStuff;

import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract class representing food items for pets.
 * This class implements the IFood interface and contains common properties and methods
 * for various types of food.
 */
public abstract class Food implements IFood {
    private Float nutritionalValueL; // Lower bound of nutritional value
    private Float nutritionalValueR; // Upper bound of nutritional value
    private Float fluctuation; // Fluctuation range for nutritional value
    private String foodType; // Type of food (e.g., Bone, Meat)
    private List<String> compatiblePets; // List of pets that can consume this food

    /**
     * Default constructor for Food class.
     * Initializes nutritional values and food type to default.
     */
    public Food() {
        nutritionalValueL = nutritionalValueR = fluctuation = 0f;
        foodType = "";
        compatiblePets = new ArrayList<>();
    }

    /**
     * Constructor for Food class with specified nutritional values and food type.
     *
     * @param nutritionalValueL The lower bound of the nutritional value.
     * @param nutritionalValueR The upper bound of the nutritional value.
     * @param foodType          The type of food.
     */
    public Food(Float nutritionalValueL, Float nutritionalValueR, String foodType) {
        this.nutritionalValueL = nutritionalValueL;
        this.nutritionalValueR = nutritionalValueR;
        this.foodType = foodType;
    }

    /**
     * Constructor for Food class with specified nutritional values, fluctuation, food type,
     * and compatible pets.
     *
     * @param nutritionalValueL  The lower bound of the nutritional value.
     * @param nutritionalValueR  The upper bound of the nutritional value.
     * @param fluctuation        The fluctuation range of the nutritional value.
     * @param foodType          The type of food.
     * @param compatiblePets     The list of pets compatible with this food.
     */
    public Food(Float nutritionalValueL, Float nutritionalValueR, Float fluctuation, String foodType, List<String> compatiblePets) {
        this.nutritionalValueL = nutritionalValueL;
        this.nutritionalValueR = nutritionalValueR;
        this.fluctuation = fluctuation;
        this.foodType = foodType;
        this.compatiblePets = compatiblePets;
    }

    /**
     * Returns a random nutritional value within the range [L, R].
     * The actual nutritional value may vary due to fluctuation.
     *
     * @return A randomly generated nutritional value.
     */
    @Override
    public Float GetNutritionalValue() {
        Random rand = new Random();
        float mid = (nutritionalValueL + nutritionalValueR) / 2;
        // Random fluctuation in range [-fluctuation, fluctuation]
        float randFluctuation = (rand.nextFloat() * 2 - 1) * fluctuation;
        float nutritionValue = randFluctuation + mid;
        nutritionValue = Math.max(nutritionValue, nutritionalValueL); // Ensure it does not go below L
        nutritionValue = Math.min(nutritionValue, nutritionalValueR); // Ensure it does not exceed R
        return nutritionValue;
    }

    /**
     * Returns the type of food.
     *
     * @return The type of food.
     */
    @Override
    public String GetFoodType() {
        return foodType;
    }

    /**
     * Returns the list of pets compatible with this food.
     *
     * @return A list of compatible pets.
     */
    @Override
    public List<String> GetCompatiblePet() {
        return compatiblePets;
    }

    /**
     * Returns the lower bound of the nutritional value.
     *
     * @return The lower bound of the nutritional value.
     */
    @Override
    public Float GetNutritionalValueL() {
        return nutritionalValueL;
    }

    /**
     * Returns the upper bound of the nutritional value.
     *
     * @return The upper bound of the nutritional value.
     */
    @Override
    public Float GetNutritionalValueR() {
        return nutritionalValueR;
    }

    /**
     * Returns the fluctuation range for the nutritional value.
     *
     * @return The fluctuation range.
     */
    @Override
    public Float GetFluctuation() {
        return fluctuation;
    }
}
