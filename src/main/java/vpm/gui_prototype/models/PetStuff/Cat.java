package vpm.gui_prototype.models.PetStuff;

/**
 * Represents a Cat, extending the abstract Pet class.
 * This class includes constructors for creating a new cat or fetching one from the database,
 * as well as methods to determine happiness and hunger decrement intervals.
 */
public class Cat extends Pet {

    /**
     * Constructor for creating a new Cat with a specified name and age.
     *
     * @param name the name of the cat
     * @param petAge the age of the cat
     */
    public Cat(String name, Integer petAge) {
        super(name, "Cat", petAge);
    }

    /**
     * Constructor for fetching a Cat from the database, including all attributes.
     *
     * @param name the name of the cat
     * @param age the age of the cat
     * @param colour the colour of the cat
     * @param happiness the current happiness level of the cat
     * @param foodSatisfaction the current food satisfaction level of the cat
     * @param isDirty indicates if the cat is dirty
     * @param personality the personality trait of the cat
     * @param customTrait a custom trait for the cat
     */
    public Cat(String name, Integer age, String colour,
               Float happiness, Float foodSatisfaction,
               Boolean isDirty, String personality,
               String customTrait) {
        super(name, age, "Cat", colour, happiness, foodSatisfaction, isDirty, personality, customTrait);
    }

    /**
     * Returns the happiness decrement interval for the cat.
     *
     * @return the happiness decrement interval in seconds
     */
    @Override
    public int getHappinessDecrementInterval() {
        return 8;  // Cat's happiness decreases every 8 seconds
    }

    /**
     * Returns the hunger decrement interval for the cat.
     *
     * @return the hunger decrement interval in seconds
     */
    @Override
    public int getHungerDecrementInterval() {
        return 6;  // Cat's hunger decreases every 6 seconds
    }
}
