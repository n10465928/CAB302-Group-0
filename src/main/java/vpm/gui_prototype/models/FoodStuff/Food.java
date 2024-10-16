package vpm.gui_prototype.models.FoodStuff;

public class Food implements IFood
{
    Float nutritionalValue;
    String foodType;

    public Food()
    {
        nutritionalValue = 0f;
    }

    public Food(Float value, String Type){
        nutritionalValue = value;
        foodType = Type;
    }

    public Food(Float nutritionalValue)
    {
        this.nutritionalValue = nutritionalValue;
    }

    public Float GetNutritionalValue()
    {
        return nutritionalValue;
    }

    public String GetFoodType(){return foodType;}
}