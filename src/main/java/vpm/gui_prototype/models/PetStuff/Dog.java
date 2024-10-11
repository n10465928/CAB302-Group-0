package vpm.gui_prototype.models.PetStuff;

/**
 * Represents a Dog, extending the abstract Pet class.
 * This class includes constructors for creating a new dog or fetching one from the database,
 * as well as methods to determine happiness and hunger decrement intervals.
 */
public class Dog extends Pet {

    /**
     * Constructor for creating a new Dog with a specified name and age.
     *
     * @param name the name of the dog
     * @param petAge the age of the dog
     */
    public Dog(String name, Integer petAge) {
        super(name, "Dog", petAge);
    }

    /**
     * Constructor for fetching a Dog from the database, including all attributes.
     *
     * @param name the name of the dog
     * @param age the age of the dog
     * @param colour the colour of the dog
     * @param happiness the current happiness level of the dog
     * @param foodSatisfaction the current food satisfaction level of the dog
     * @param isDirty indicates if the dog is dirty
     * @param personality the personality trait of the dog
     * @param customTrait a custom trait for the dog
     */
    public Dog(String name, Integer age, String colour,
               Float happiness, Float foodSatisfaction,
               Boolean isDirty, String personality,
               String customTrait) {
        super(name, age, "Dog", colour, happiness, foodSatisfaction, isDirty, personality, customTrait);
    }

    /**
     * Returns the happiness decrement interval for the dog.
     *
     * @return the happiness decrement interval in seconds
     */
    @Override
    public int getHappinessDecrementInterval() {
        return 5;  // Dog's happiness decreases every 5 seconds
    }

    /**
     * Returns the hunger decrement interval for the dog.
     *
     * @return the hunger decrement interval in seconds
     */
    @Override
    public int getHungerDecrementInterval() {
        return 5;  // Dog's hunger decreases every 5 seconds
    }
}
