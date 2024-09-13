package vpm.gui_prototype.models.PetStuff;

import Constants.Constants;
import FoodStuff.Food;

public class Pet implements IPet
{
    String name;
    Integer age;
    Float happiness;
    Float foodSatisfaction;
    //^^ the above is meant to mean 'opposite of hunger'
    Boolean isDirty;
    String type;

    //three constructors for different ways in which a pet may be initialised
    public Pet()
    {
        name = "";
        age = 0;
        happiness = Constants.MAXHAPPINESS;
        type = "";
    }
    public Pet(String name, Integer age, String type)
    {
        this.name = name;
        this.age = age;
        this.type = type;
        this.happiness = Constants.MAXHAPPINESS;
        this.foodSatisfaction = Constants.MAXFOODSATISFACTION;
    }
    public Pet(String name, Integer age, String type, Float happiness, Float foodSatisfaction)
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

    public String GetType() {
        return type;
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
        if (happiness > Constants.MAXHAPPINESS)
        {
            happiness = Constants.MAXHAPPINESS;
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
        if (foodSatisfaction > Constants.MAXFOODSATISFACTION)
        {
            foodSatisfaction = Constants.MAXFOODSATISFACTION;
            return "Too fed already, cannot be anymore";
        }
        if (foodSatisfaction < 0f)
        {
            foodSatisfaction = 0f;
            return "Too unfed already, cannot be anymore";
        }
        return "";
    }

    public String Clean()
    {
        if (isDirty)
        {
            isDirty = Boolean.FALSE;
            return "";
        }
        else
            return "already clean";
    }
}
