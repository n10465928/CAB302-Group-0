package vpm.gui_prototype.models.FoodStuff;

public class Food implements IFood
{
    Float nutritionalValue;

    public Food()
    {
        nutritionalValue = 0f;
    }

    public Food(Float nutritionalValue)
    {
        this.nutritionalValue = nutritionalValue;
    }

    public Float GetNutritionalValue()
    {
        return nutritionalValue;
    }
}
