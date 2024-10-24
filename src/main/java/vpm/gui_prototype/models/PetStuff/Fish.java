package vpm.gui_prototype.models.PetStuff;

/**
 * Represents a Fish, extending the abstract Pet class.
 * This class includes constructors for creating a new fish or fetching one from the database,
 * as well as methods to determine happiness and hunger decrement intervals.
 */
public class Fish extends Pet {

    /**
     * Constructor for creating a new Fish with a specified name and age.
     *
     * @param name the name of the fish
     * @param petAge the age of the fish
     */
    public Fish(String name, Integer petAge) {
        super(name, "Fish", petAge);
    }

    /**
     * Constructor for fetching a Fish from the database, including all attributes.
     *
     * @param name the name of the fish
     * @param age the age of the fish
     * @param colour the colour of the fish
     * @param happiness the current happiness level of the fish
     * @param foodSatisfaction the current food satisfaction level of the fish
     * @param isDirty indicates if the fish is dirty
     * @param personality the personality trait of the fish
     */
    public Fish(String name, Integer age, String colour,
                Float happiness, Float foodSatisfaction,
                Boolean isDirty, String personality) {
        super(name, age, "Fish", colour, happiness, foodSatisfaction, isDirty, personality);
    }

    /**
     * Returns the happiness decrement interval for the fish.
     *
     * @return the happiness decrement interval in seconds
     */
    @Override
    public int getHappinessDecrementInterval() {
        return 10;  // Fish's happiness decreases every 10 seconds
    }

    /**
     * Returns the hunger decrement interval for the fish.
     *
     * @return the hunger decrement interval in seconds
     */
    @Override
    public int getHungerDecrementInterval() {
        return 7;  // Fish's hunger decreases every 7 seconds
    }
}
