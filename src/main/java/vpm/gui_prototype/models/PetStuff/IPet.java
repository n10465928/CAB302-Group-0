package vpm.gui_prototype.models.PetStuff;

public interface IPet
{

    Integer getUserID();
    void setUserID(Integer UserID);

    Integer getPetID();
    void setPetID(Integer PetID);

    String getName();
    void setName(String Name);

    Integer getAge();
    void setAge(Integer Age);

    String getType();
    void setType(String Type);

    Float getHappiness();
    void setHappiness(Float Happiness);

    Float getFoodSatisfaction();
    void setFoodSatisfaction(Float FoodSatisfaction);

    String getColour();
    void setColour(String Colour);

    Boolean getIsDirty();
    void setIsDirty(Boolean IsDirty);

    String getPersonality();
    void setPersonality(String personality);

    String getCustomTrait();
    void setCustomTrait(String customTrait);

    float roundToTwoDecimalPlaces(float value);

    String increaseHappiness(Float amount);

    String decreaseHappiness(Float amount);

    String feed(float food);

    String clean();
}