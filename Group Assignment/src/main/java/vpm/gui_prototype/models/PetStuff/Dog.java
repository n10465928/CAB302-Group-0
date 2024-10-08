package vpm.gui_prototype.models.PetStuff;

public class Dog extends Pet {

    // Constructor for creating a new Dog with name and age
    public Dog(String Name, Integer petAge) {
        super(Name, "Dog", petAge);
    }

    // Constructor for fetching a Dog from the database (includes all attributes)
    public Dog(String name, Integer age, String colour, Float happiness, Float foodSatisfaction, Boolean isDirty, String personality, String customTrait) {
        super(name, age, "Dog", colour, happiness, foodSatisfaction, isDirty, personality, customTrait);
    }

    @Override
    public long getHappinessDecrementInterval() {
        return 1;  // Dog's happiness decreases every 5 seconds
    }

    @Override
    public long getHungerDecrementInterval() {
        return 1;  // Dog's hunger decreases every 5 seconds
    }
}
