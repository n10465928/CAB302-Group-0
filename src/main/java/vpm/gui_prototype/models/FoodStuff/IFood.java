package vpm.gui_prototype.models.FoodStuff;

import java.util.List;

public interface IFood
{
    public Float GetNutritionalValueL();
    public Float GetNutritionalValueR();
    public Float GetFluctuation();
    public Float GetNutritionalValue();
    public String GetFoodType();
    public List<String> GetCompatiblePet();
}