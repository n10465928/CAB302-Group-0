package vpm.gui_prototype.models.PetStuff;

public class Fish extends Pet {

    // Constructor for creating a new Fish with name and age
    public Fish(String Name, Integer petAge) {
        super(Name, "Fish", petAge);
    }

    // Constructor for fetching a Fish from the database (includes all attributes)
    public Fish(String name, Integer age, String colour, Float happiness, Float foodSatisfaction, Boolean isDirty, String personality, String customTrait) {
        super(name, age, "Fish", colour, happiness, foodSatisfaction, isDirty, personality, customTrait);
    }

    @Override
    public long getHappinessDecrementInterval() {
        return 10;  // Fish's happiness decreases every 10 seconds
    }

    @Override
    public long getHungerDecrementInterval() {
        return 7;  // Fish's hunger decreases every 7 seconds
    }
}
