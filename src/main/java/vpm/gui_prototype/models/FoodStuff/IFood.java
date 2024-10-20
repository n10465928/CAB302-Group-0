package vpm.gui_prototype.models.FoodStuff;

import java.util.List;

public interface IFood
{
    Float GetNutritionalValueL();
    Float GetNutritionalValueR();
    Float GetFluctuation();
    Float GetNutritionalValue();
    String GetFoodType();
    List<String> GetCompatiblePet();
}