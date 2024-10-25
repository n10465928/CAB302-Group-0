package vpm.gui_prototype.models.PetStuff;

import java.util.Objects;

/**
 * Generic PetBuilder for constructing various types of Pet objects.
 */
public class PetBuilder {
    private String name;
    private String type;
    private Integer age = 1;
    private String colour = "Brown";
    private Float happiness = 5.0f;
    private Float foodSatisfaction = 5.0f;
    private Boolean isDirty = false;
    private String personality = "Unknown";

    /**
     * Sets the pet's name.
     *
     * @param name the name of the pet
     * @return the PetBuilder instance
     */
    public PetBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the type of pet (e.g., "Dog", "Cat").
     *
     * @param type the type of pet
     * @return the PetBuilder instance
     */
    public PetBuilder setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * Sets the pet's age.
     *
     * @param age the age of the pet
     * @return the PetBuilder instance
     */
    public PetBuilder setAge(Integer age) {
        this.age = age;
        return this;
    }

    /**
     * Sets the pet's color.
     *
     * @param colour the color of the pet
     * @return the PetBuilder instance
     */
    public PetBuilder setColour(String colour) {
        this.colour = colour;
        return this;
    }

    /**
     * Sets the pet's happiness level.
     *
     * @param happiness the happiness level of the pet
     * @return the PetBuilder instance
     */
    public PetBuilder setHappiness(Float happiness) {
        this.happiness = happiness;
        return this;
    }

    /**
     * Sets the pet's food satisfaction level.
     *
     * @param foodSatisfaction the food satisfaction level of the pet
     * @return the PetBuilder instance
     */
    public PetBuilder setFoodSatisfaction(Float foodSatisfaction) {
        this.foodSatisfaction = foodSatisfaction;
        return this;
    }

    /**
     * Sets whether the pet is dirty.
     *
     * @param isDirty true if the pet is dirty, false otherwise
     * @return the PetBuilder instance
     */
    public PetBuilder setIsDirty(Boolean isDirty) {
        this.isDirty = isDirty;
        return this;
    }

    /**
     * Sets the pet's personality trait.
     *
     * @param personality the personality trait of the pet
     * @return the PetBuilder instance
     */
    public PetBuilder setPersonality(String personality) {
        this.personality = personality;
        return this;
    }

    /**
     * Builds and returns a Pet instance based on the specified type.
     *
     * @return the constructed Pet instance
     * @throws IllegalStateException if required fields are missing
     */
    public Pet build() {
        if (name == null || name.isEmpty()) {
            throw new IllegalStateException("Name must be set before building a Pet.");
        }
        if (type == null || type.isEmpty()) {
            throw new IllegalStateException("Type must be set before building a Pet.");
        }

        switch (type.toLowerCase()) {
            case "dog":
                return new Dog(name, age, colour, happiness, foodSatisfaction, isDirty, personality);
            case "cat":
                return new Cat(name, age, colour, happiness, foodSatisfaction, isDirty, personality);
            case "bird":
                return new Bird(name, age, colour, happiness, foodSatisfaction, isDirty, personality);
            case "fish":
                return new Fish(name, age, colour, happiness, foodSatisfaction, isDirty, personality);
            //handle invalid types
            default:
                throw new IllegalArgumentException("Unsupported pet type: " + type);
        }
    }
}
