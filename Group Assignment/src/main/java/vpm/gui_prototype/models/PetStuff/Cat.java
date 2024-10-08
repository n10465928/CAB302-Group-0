package vpm.gui_prototype.models.PetStuff;

public class Cat extends Pet {

    // Constructor for creating a new Cat with name and age
    public Cat(String Name, Integer petAge) {
        super(Name, "Cat", petAge);
    }

    // Constructor for fetching a Cat from the database (includes all attributes)
    public Cat(String name, Integer age, String colour, Float happiness, Float foodSatisfaction, Boolean isDirty, String personality, String customTrait) {
        super(name, age, "Cat", colour, happiness, foodSatisfaction, isDirty, personality, customTrait);
    }

    @Override
    public long getHappinessDecrementInterval() {
        return 8;  // Cat's happiness decreases every 8 seconds
    }

    @Override
    public long getHungerDecrementInterval() {
        return 6;  // Cat's hunger decreases every 6 seconds
    }
}
