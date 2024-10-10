package vpm.gui_prototype.models.PetStuff;

public class Bird extends Pet {

    // Constructor for creating a new Bird with name and age
    public Bird(String Name, Integer petAge) {
        super(Name, "Bird", petAge);
    }

    // Constructor for fetching a Bird from the database (includes all attributes)
    public Bird(String name, Integer age, String colour, Float happiness, Float foodSatisfaction, Boolean isDirty, String personality, String customTrait) {
        super(name, age, "Bird", colour, happiness, foodSatisfaction, isDirty, personality, customTrait);
    }

    @Override
    public long getHappinessDecrementInterval() {
        return 10;  // Bird's happiness decreases every 10 seconds
    }

    @Override
    public long getHungerDecrementInterval() {
        return 3;  // Bird's hunger decreases every 3 seconds
    }
}
