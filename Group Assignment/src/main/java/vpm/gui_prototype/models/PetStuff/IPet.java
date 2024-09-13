package vpm.gui_prototype.models.PetStuff;

import javafx.scene.paint.Color;

public interface IPet
{
    public Integer GetID();
    public String GetName();
    public Integer GetAge();
    public String GetType();
    public Float GetHappiness();
    public Float GetFoodSatisfaction();
    public String GetColour();
}

