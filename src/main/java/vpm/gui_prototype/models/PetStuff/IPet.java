package vpm.gui_prototype.models.PetStuff;

/**
 * Interface representing the basic functionalities and properties of a Pet.
 * This interface defines methods for getting and setting pet attributes, as well as actions that can be performed on a pet.
 */
public interface IPet {

    /**
     * Gets the user ID associated with the pet.
     *
     * @return the user ID
     */
    Integer getUserID();

    /**
     * Sets the user ID associated with the pet.
     *
     * @param UserID the user ID to set
     */
    void setUserID(Integer UserID);

    /**
     * Gets the pet ID.
     *
     * @return the pet ID
     */
    Integer getPetID();

    /**
     * Sets the pet ID.
     *
     * @param PetID the pet ID to set
     */
    void setPetID(Integer PetID);

    /**
     * Gets the name of the pet.
     *
     * @return the name of the pet
     */
    String getName();

    /**
     * Sets the name of the pet.
     *
     * @param Name the name to set
     */
    void setName(String Name);

    /**
     * Gets the age of the pet.
     *
     * @return the age of the pet
     */
    Integer getAge();

    /**
     * Sets the age of the pet.
     *
     * @param Age the age to set
     */
    void setAge(Integer Age);

    /**
     * Gets the type of the pet (e.g., dog, cat).
     *
     * @return the type of the pet
     */
    String getType();

    /**
     * Sets the type of the pet.
     *
     * @param Type the type to set
     */
    void setType(String Type);

    /**
     * Gets the current happiness level of the pet.
     *
     * @return the happiness level of the pet
     */
    Float getHappiness();

    /**
     * Sets the happiness level of the pet.
     *
     * @param Happiness the happiness level to set
     */
    void setHappiness(Float Happiness);

    /**
     * Gets the current food satisfaction level of the pet.
     *
     * @return the food satisfaction level of the pet
     */
    Float getFoodSatisfaction();

    /**
     * Sets the food satisfaction level of the pet.
     *
     * @param FoodSatisfaction the food satisfaction level to set
     */
    void setFoodSatisfaction(Float FoodSatisfaction);

    /**
     * Gets the colour of the pet.
     *
     * @return the colour of the pet
     */
    String getColour();

    /**
     * Sets the colour of the pet.
     *
     * @param Colour the colour to set
     */
    void setColour(String Colour);

    /**
     * Gets the dirty status of the pet.
     *
     * @return true if the pet is dirty; false otherwise
     */
    Boolean getIsDirty();

    /**
     * Sets the dirty status of the pet.
     *
     * @param IsDirty the dirty status to set
     */
    void setIsDirty(Boolean IsDirty);

    /**
     * Gets the personality trait of the pet.
     *
     * @return the personality of the pet
     */
    String getPersonality();

    /**
     * Sets the personality trait of the pet.
     *
     * @param personality the personality to set
     */
    void setPersonality(String personality);

    /**
     * Gets the custom trait of the pet.
     *
     * @return the custom trait of the pet
     */
    String getCustomTrait();

    /**
     * Sets the custom trait of the pet.
     *
     * @param customTrait the custom trait to set
     */
    void setCustomTrait(String customTrait);

    /**
     * Rounds a float value to two decimal places.
     *
     * @param value the float value to round
     * @return the rounded float value
     */
    float roundToTwoDecimalPlaces(float value);

    /**
     * Increases the happiness of the pet by a specified amount.
     *
     * @param amount the amount to increase happiness by
     * @return a message indicating the result of the operation
     */
    String increaseHappiness(Float amount);

    /**
     * Decreases the happiness of the pet by a specified amount.
     *
     * @param amount the amount to decrease happiness by
     * @return a message indicating the result of the operation
     */
    String decreaseHappiness(Float amount);

    /**
     * Feeds the pet with a specified amount of food.
     *
     * @param food the amount of food to give
     * @return a message indicating the result of the operation
     */
    String feed(float food);

    /**
     * Cleans the pet if it is dirty.
     *
     * @return a message indicating the result of the cleaning operation
     */
    String clean();
}
