package PetStuff;

import Constants.Constants;
import FoodStuff.Food;

public class Pet implements IPet
{
    String name;
    Integer age;
    Float happiness;
    Float foodSatisfaction;
    //^^ the above is meant to mean 'opposite of hunger'

    //three constructors for different ways in which a pet may be initialised
    public Pet()
    {
        name = "";
        age = 0;
        happiness = Constants.MAXHAPPINESS;
    }
    public Pet(String name, Integer age)
    {
        this.name = name;
        this.age = age;
        this.happiness = Constants.MAXHAPPINESS;
        this.foodSatisfaction = Constants.MAXFOODSATISFACTION;
    }
    public Pet(String name, Integer age, Float happiness, Float foodSatisfaction)
    {
        this.name = name;
        this.age = age;
        this.happiness = happiness;
        this.foodSatisfaction = foodSatisfaction;
    }

    public String GetName()
    {
        return name;
    }

    public Integer GetAge()
    {
        return age;
    }

    public Float GetHappiness()
    {
        return happiness;
    }

    public Float GetFoodSatisfaction()
    {
        return foodSatisfaction;
    }

    public String IncreaseHappiness(Float amount)
    {
        happiness += amount;
        if (happiness > 10f)
        {
            happiness = 10f;
            return "Too happy already, cannot be anymore";
        }
        if (happiness < 0f)
        {
            happiness = 0f;
            return "Too unhappy already, cannot be anymore";
        }
        return "";
    }

    public String DecreaseHappiness(Float amount)
    {
        return IncreaseHappiness(-amount);
    }

    public String Feed(Food food)
    {
        foodSatisfaction += food.GetNutritionalValue();
        if (foodSatisfaction > 10f)
        {
            foodSatisfaction = 10f;
            return "Too fed already, cannot be anymore";
        }
        if (foodSatisfaction < 0f)
        {
            foodSatisfaction = 0f;
            return "Too unfed already, cannot be anymore";
        }
        return "";
    }
}
