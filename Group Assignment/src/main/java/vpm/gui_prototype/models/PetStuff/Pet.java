package vpm.gui_prototype.models.PetStuff;

import Constants.Constants;
import FoodStuff.Food;
import javafx.scene.paint.Color;

public class Pet implements IPet
{
    Integer petID;
    String name;
    Integer age;
    Float happiness;
    Float foodSatisfaction;
    //^^ the above is meant to mean 'opposite of hunger'
    Boolean isDirty;
    String type;
    String colour;

    //three constructors for different ways in which a pet may be initialised
    public Pet(Integer petID)
    {
        this.petID = petID;
        name = "";
        age = 0;
        happiness = Constants.MAXHAPPINESS;
        type = "";
    }
    public Pet(Integer petID, String name, Integer age, String type, String colour)
    {
        this.petID = petID;
        this.name = name;
        this.age = age;
        this.type = type;
        this.colour = colour;
        this.happiness = Constants.MAXHAPPINESS;
        this.foodSatisfaction = Constants.MAXFOODSATISFACTION;
    }
    public Pet(Integer petID, String name, Integer age, String type, String colour, Float happiness, Float foodSatisfaction)
    {
        this.petID = petID;
        this.name = name;
        this.age = age;
        this.type = type;
        this.colour = colour;
        this.happiness = happiness;
        this.foodSatisfaction = foodSatisfaction;
    }
    public Pet(String name, Integer age, String type)
    {
        this.name = name;
        this.age = age;
        this.type = type;
    }

    public Integer GetID() { return petID; }

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

    public String GetColour() { return colour; }

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
