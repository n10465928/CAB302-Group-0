package vpm.gui_prototype.models.PetStuff;

import javafx.scene.paint.Color;

public interface IPet
{
    public Integer GetUserID();
    public void SetUserID(Integer UserID);

    public Integer GetPetID();
    public void SetPetID(Integer PetID);

    public String GetName();
    public void SetName(String Name);

    public Integer GetAge();
    public void SetAge(Integer Age);

    public String GetType();
    public void SetType(String Type);

    public Float GetHappiness();
    public void SetHappiness(Float Happiness);

    public Float GetFoodSatisfaction();
    public void SetFoodSatisfaction(Float FoodSatisfaction);

    public String GetColour();
    public void SetColour(String Colour);

    public Boolean GetIsDirty();
    public void SetIsDirty(Boolean IsDirty);
}