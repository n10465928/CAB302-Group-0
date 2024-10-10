package vpm.gui_prototype.models.PetStuff;

import vpm.gui_prototype.models.Constants.Constants;

public abstract class Pet implements IPet {

    private Integer userID;
    private Integer petID;
    private String name;
    private Integer age;
    private Float happiness;
    private Float foodSatisfaction;
    private Boolean isDirty;
    private String type;
    private String colour;
    private String personality;
    private String customTrait;

    //Constructors for the pet class
    public Pet() {
        this("","",0);
    }

    public Pet(String name, String type, Integer age) {
        this.name = name;
        this.type = type;
        this.age = age;
        this.colour = null;
        this.happiness = Constants.MAXHAPPINESS / 2;
        this.foodSatisfaction = Constants.MAXFOODSATISFACTION / 2;
        this.isDirty = true;
        this.personality = null;
        this.customTrait = null;
    }

    public Pet(String name, Integer age, String type, String colour,
               Float happiness, Float foodSatisfaction, Boolean isDirty,
               String personality, String customTrait) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.colour = colour;
        this.happiness = happiness;
        this.foodSatisfaction = foodSatisfaction;
        this.isDirty = isDirty;
        this.personality = personality;
        this.customTrait = customTrait;
    }

    // Abstract methods for custom decrement intervals
    public abstract long getHappinessDecrementInterval();  // Abstract method for custom decrement interval
    public abstract long getHungerDecrementInterval();  // Abstract method for custom decrement interval

    // Getter/Setter methods for Pet properties
    public Integer getUserID() { return userID; }
    public void setUserID(Integer userID) { this.userID = userID; }

    public Integer getPetID() { return petID; }
    public void setPetID(Integer petID) { this.petID = petID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Float getHappiness() { return happiness; }
    public void setHappiness(Float happiness) { this.happiness = happiness; }

    public Float getFoodSatisfaction() { return foodSatisfaction; }
    public void setFoodSatisfaction(Float foodSatisfaction) { this.foodSatisfaction = foodSatisfaction; }

    public String getColour() { return colour; }
    public void setColour(String colour) { this.colour = colour; }

    public Boolean getIsDirty() { return isDirty; }
    public void setIsDirty(Boolean isDirty) { this.isDirty = isDirty; }

    public String getPersonality() { return personality; }
    public void setPersonality(String personality) { this.personality = personality; }

    public String getCustomTrait() { return customTrait; }
    public void setCustomTrait(String customTrait) { this.customTrait = customTrait; }

    public float roundToTwoDecimalPlaces(float value) {
        return Math.round(value * 100.0f) / 100.0f;
    }

    public String increaseHappiness(Float amount) {
        happiness += amount;
        happiness = roundToTwoDecimalPlaces(happiness);

        if (happiness > Constants.MAXHAPPINESS) {
            happiness = Constants.MAXHAPPINESS;
            return "Too happy already, cannot be anymore";
        }
        if (happiness < 0f) {
            happiness = 0f;
            return "Too unhappy already, cannot be anymore";
        }
        return "";
    }

    public String decreaseHappiness(Float amount) {
        return increaseHappiness(-amount);
    }

    public String feed(float food) {
        foodSatisfaction += food;
        foodSatisfaction = roundToTwoDecimalPlaces(foodSatisfaction);

        if (foodSatisfaction > Constants.MAXFOODSATISFACTION) {
            foodSatisfaction = Constants.MAXFOODSATISFACTION;
            return "Too fed already, cannot be anymore";
        }
        if (foodSatisfaction < 0f) {
            foodSatisfaction = 0f;
            return "Too unfed already, cannot be anymore";
        }
        return "";
    }

    public String clean() {
        if (isDirty) {
            isDirty = false;
            return "Pet Cleaned";
        } else
            return "Already clean";
    }
}
