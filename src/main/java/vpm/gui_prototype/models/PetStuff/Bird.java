package vpm.gui_prototype.models.PetStuff;

/**
 * Represents a Bird, extending the abstract Pet class.
 * This class includes constructors for creating a new bird or fetching one from the database,
 * as well as methods to determine happiness and hunger decrement intervals.
 */
public class Bird extends Pet {

    /**
     * Constructor for creating a new Bird with a specified name and age.
     *
     * @param name the name of the bird
     * @param petAge the age of the bird
     */
    public Bird(String name, Integer petAge) {
        super(name, "Bird", petAge);
    }

    /**
     * Constructor for fetching a Bird from the database, including all attributes.
     *
     * @param name the name of the bird
     * @param age the age of the bird
     * @param colour the colour of the bird
     * @param happiness the current happiness level of the bird
     * @param foodSatisfaction the current food satisfaction level of the bird
     * @param isDirty indicates if the bird is dirty
     * @param personality the personality trait of the bird
     * @param customTrait a custom trait for the bird
     */
    public Bird(String name, Integer age, String colour,
                Float happiness, Float foodSatisfaction,
                Boolean isDirty, String personality,
                String customTrait) {
        super(name, age, "Bird", colour, happiness, foodSatisfaction, isDirty, personality, customTrait);
    }

    /**
     * Returns the happiness decrement interval for the bird.
     *
     * @return the happiness decrement interval in seconds
     */
    @Override
    public int getHappinessDecrementInterval() {
        return 10;  // Bird's happiness decreases every 10 seconds
    }

    /**
     * Returns the hunger decrement interval for the bird.
     *
     * @return the hunger decrement interval in seconds
     */
    @Override
    public int getHungerDecrementInterval() {
        return 3;  // Bird's hunger decreases every 3 seconds
    }
}
