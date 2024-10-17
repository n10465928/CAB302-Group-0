package vpm.gui_prototype.models.PetStuff;

import vpm.gui_prototype.models.Constants.Constants;
import vpm.gui_prototype.models.FoodStuff.Food;

import java.util.Objects;

/**
 * Abstract class representing a Pet in the system.
 * This class contains common properties and behaviors for all pets.
 */
public abstract class Pet implements IPet {

    private Integer userID;
    private Integer petID;
    private String name;
    private Integer age;
    private Float happiness;
    private Float foodSatisfaction;
    private Boolean isDirty;
    private String type;
    private String colour;
    private String personality;

    /**
     * Default constructor for the Pet class.
     * Initializes a pet with default values.
     */
    public Pet() {
        this("", "", 0);
    }

    /**
     * Constructor for the Pet class with specified name, type, and age.
     *
     * @param name the name of the pet
     * @param type the type of the pet (e.g., dog, cat)
     * @param age the age of the pet
     */
    public Pet(String name, String type, Integer age) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.colour = null;
        this.happiness = Constants.MAXHAPPINESS / 2;
        this.foodSatisfaction = Constants.MAXFOODSATISFACTION / 2;
        this.isDirty = true;
        this.personality = null;
    }

    /**
     * Constructor for the Pet class with all properties.
     *
     * @param name the name of the pet
     * @param age the age of the pet
     * @param type the type of the pet
     * @param colour the colour of the pet
     * @param happiness the current happiness level of the pet
     * @param foodSatisfaction the current food satisfaction level of the pet
     * @param isDirty indicates if the pet is dirty
     * @param personality the personality trait of the pet
     */
    public Pet(String name, Integer age, String type, String colour,
               Float happiness, Float foodSatisfaction, Boolean isDirty,
               String personality) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.colour = colour;
        this.happiness = happiness;
        this.foodSatisfaction = foodSatisfaction;
        this.isDirty = isDirty;
        this.personality = personality;
    }

    /**
     * Gets the happiness decrement interval specific to the pet.
     *
     * @return the interval in milliseconds for happiness decrement
     */
    public abstract int getHappinessDecrementInterval();

    /**
     * Gets the hunger decrement interval specific to the pet.
     *
     * @return the interval in milliseconds for hunger decrement
     */
    public abstract int getHungerDecrementInterval();

    // Getter and Setter methods for Pet properties

    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }

    public Integer getPetID() { return petID; }
    public void setPetID(Integer petID) { this.petID = petID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Float getHappiness() { return happiness; }
    public void setHappiness(Float happiness) { this.happiness = happiness; }

    public Float getFoodSatisfaction() { return foodSatisfaction; }
    public void setFoodSatisfaction(Float foodSatisfaction) { this.foodSatisfaction = foodSatisfaction; }

    public String getColour() { return colour; }
    public void setColour(String colour) { this.colour = colour; }

    public Boolean getIsDirty() { return isDirty; }
    public void setIsDirty(Boolean isDirty) { this.isDirty = isDirty; }

    public String getPersonality() { return personality; }
    public void setPersonality(String personality) { this.personality = personality; }

    /**
     * Rounds a float value to two decimal places.
     *
     * @param value the float value to round
     * @return the rounded float value
     */
    public float roundToTwoDecimalPlaces(float value) {
        return Math.round(value * 100.0f) / 100.0f;
    }

    /**
     * Increases the happiness of the pet by a specified amount.
     *
     * @param amount the amount to increase happiness by
     * @return a message indicating the result of the operation
     */
    public String increaseHappiness(Float amount) {
        happiness += amount;
        happiness = roundToTwoDecimalPlaces(happiness);

        if (happiness > Constants.MAXHAPPINESS) {
            happiness = Constants.MAXHAPPINESS;
            return "Too happy already, cannot be anymore";
        }
        if (happiness < 0f) {
            happiness = 0f;
            return "Too unhappy already, cannot be anymore";
        }
        return "";
    }

    /**
     * Increases the happiness of the pet by a specified amount.
     *
     * @param amount the amount to increase happiness by
     * @return a message indicating the result of the operation
     */
    public String playWtihPet(String type, Float amount) {
        if(Objects.equals(type, this.type)){
            happiness += amount;
        }else{
            happiness -= amount;
        }
        happiness = roundToTwoDecimalPlaces(happiness);

        if (happiness > Constants.MAXHAPPINESS) {
            happiness = Constants.MAXHAPPINESS;
            return "Too happy already, cannot be anymore";
        }
        if (happiness < 0f) {
            happiness = 0f;
            return "Too unhappy already, cannot be anymore";
        }
        return "";
    }



    /**
     * Decreases the happiness of the pet by a specified amount.
     *
     * @param amount the amount to decrease happiness by
     * @return a message indicating the result of the operation
     */
    public String decreaseHappiness(Float amount) {
        return increaseHappiness(-amount);
    }

    /**
     * Feeds the pet with a specified amount of food.
     *
     * @param food the amount of food to give
     * @return a message indicating the result of the operation
     */
    public String feed(float food) {
        foodSatisfaction += food;
        foodSatisfaction = roundToTwoDecimalPlaces(foodSatisfaction);

        if (foodSatisfaction > Constants.MAXFOODSATISFACTION) {
            foodSatisfaction = Constants.MAXFOODSATISFACTION;
            return "Too fed already, cannot be anymore";
        }
        if (foodSatisfaction < 0f) {
            foodSatisfaction = 0f;
            return "Too unfed already, cannot be anymore";
        }
        return "";
    }

    public String feed(Food food){
        if(Objects.equals(food.GetFoodType(), this.type)){
            foodSatisfaction += food.GetNutritionalValue();
        }
        else{
            foodSatisfaction -= food.GetNutritionalValue();
        }
        foodSatisfaction = roundToTwoDecimalPlaces(foodSatisfaction);

        if (foodSatisfaction > Constants.MAXFOODSATISFACTION) {
            foodSatisfaction = Constants.MAXFOODSATISFACTION;
            return "Too fed already, cannot be anymore";
        }
        if (foodSatisfaction < 0f) {
            foodSatisfaction = 0f;
            return "Too unfed already, cannot be anymore";
        }
        return "";
    }



    /**
     * Cleans the pet if it is dirty.
     *
     * @return a message indicating the result of the cleaning operation
     */
    public String clean() {
        if (isDirty) {
            isDirty = false;
            return "Pet Cleaned";
        } else {
            return "Already clean";
        }
    }
}
