package vpm.gui_prototype.models.FoodStuff;

import vpm.gui_prototype.models.PetStuff.Pet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Food implements IFood
{
    Float nutritionalValueL, nutritionalValueR, fluctuation;
    String foodType;
    List<String> compatiblePets;

    /**
     * Default constructor for Food class
     */
    public Food() {
        nutritionalValueL = nutritionalValueR = fluctuation = 0f;
        foodType = "";
        compatiblePets = new ArrayList<>();
    }

    /**
     * Constructor for Food class
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     * @param foodType the type of food
     */
    public Food(Float nutritionalValueL, Float nutritionalValueR, String foodType) {
        this.nutritionalValueL = nutritionalValueL;
        this.nutritionalValueR = nutritionalValueR;
        this.foodType = foodType;
    }

    /**
     * Constructor for Food class
     * @param nutritionalValueL the lowerbound of the nutritionalValue
     * @param nutritionalValueR the upperbound of the nutritionalValue
     * @param fluctuation the fluctuation range of nutritionalValue
     * @param foodType the type of food
     * @param compatiblePets the list of pets that is compatible to the food
     */
    public Food(Float nutritionalValueL, Float nutritionalValueR, Float fluctuation, String foodType, List<String> compatiblePets) {
        this.nutritionalValueL = nutritionalValueL;
        this.nutritionalValueR = nutritionalValueR;
        this.fluctuation = fluctuation;
        this.foodType = foodType;
        this.compatiblePets = compatiblePets;
    }

    /**
     *  Return a random value in range [L, R] of nutritionalValue
     *  as food may vary in actual nutritionalValue
     */
    @Override
    public Float GetNutritionalValue() {
        Random rand = new Random();
        float mid = (nutritionalValueL + nutritionalValueR)/2;
        // random fluctuation value in range [-fluctuation, fluctuation]
        float randFluctuation = (rand.nextFloat() * 2 - 1) * fluctuation;
        float nutritionValue = randFluctuation + mid;
        nutritionValue = Math.max(nutritionValue, nutritionalValueL);
        nutritionValue = Math.min(nutritionValue, nutritionalValueR);
        return nutritionValue;
    }

    /**
     * Function to get the type of food
     * @return the type of food
     */
    @Override
    public String GetFoodType() {return foodType;}

    /**
     * Function to get the list of pets that is compatible to the food.
     * @return the list of pets that is compatible to the food
     */
    @Override
    public List<String> GetCompatiblePet() {
        return compatiblePets;
    }

    /**
     * Get function for nutritionalValueL
     * @return the nutritionalValueL
     */
    @Override
    public Float GetNutritionalValueL() {
        return nutritionalValueL;
    }

    /**
     * Get function for nutritionalValueR
     * @return the nutritionalValueR
     */
    @Override
    public Float GetNutritionalValueR() {
        return nutritionalValueR;
    }

    /**
     * Get function for fluctuation
     * @return the fluctuation
     */
    @Override
    public Float GetFluctuation() {
        return fluctuation;
    }

}