package vpm.gui_prototype.models.FoodStuff;

import java.util.List;

/**
 * Interface representing the essential methods for food types in the pet food system.
 * Any food class must implement this interface to provide details about its nutritional values and compatibility.
 */
public interface IFood {
    /**
     * Gets the lower bound of the nutritional value.
     *
     * @return The lower limit of the nutritional value.
     */
    Float GetNutritionalValueL();

    /**
     * Gets the upper bound of the nutritional value.
     *
     * @return The upper limit of the nutritional value.
     */
    Float GetNutritionalValueR();

    /**
     * Gets the fluctuation range of the nutritional value.
     *
     * @return The fluctuation range.
     */
    Float GetFluctuation();

    /**
     * Calculates and returns a random nutritional value within the specified range.
     *
     * @return A random nutritional value.
     */
    Float GetNutritionalValue();

    /**
     * Gets the type of food.
     *
     * @return A string representing the type of food.
     */
    String GetFoodType();

    /**
     * Gets a list of pet types that are compatible with this food.
     *
     * @return A list of compatible pet types.
     */
    List<String> GetCompatiblePet();
}
