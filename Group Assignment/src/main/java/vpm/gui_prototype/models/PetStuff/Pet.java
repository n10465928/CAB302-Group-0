package vpm.gui_prototype.models.PetStuff;

import Constants.Constants;
import javafx.scene.paint.Color;
import vpm.gui_prototype.models.FoodStuff.Food;

public class Pet implements IPet
{
    Integer userID;
    Integer petID;
    String name;
    Integer age;
    Float happiness;
    Float foodSatisfaction;
    //^^ the above is meant to mean 'opposite of hunger'
    Boolean isDirty;
    String type;
    String colour;
    String personality;
    String customTrait;

    //three constructors for different ways in which a pet may be initialised
    public Pet()
    {
        name = "";
        age = 0;
        happiness = Constants.MAXHAPPINESS;
        type = "";
    }

    public Pet(String Name, String Type, Integer petAge)
    {
        this.name = Name;
        this.type = Type;
        this.age = petAge;
        this.colour = null;
        this.happiness = Constants.MAXHAPPINESS;
        this.foodSatisfaction = Constants.MAXFOODSATISFACTION;
        this.isDirty = false;
        this.personality= null;
        this.customTrait = null;
    }

    public Pet(String name, Integer age, String type, String colour)
    {
        this.name = name;
        this.age = age;
        this.type = type;
        this.colour = colour;
        this.happiness = Constants.MAXHAPPINESS/2;
        this.foodSatisfaction = Constants.MAXFOODSATISFACTION/2;
        this.isDirty = false;
    }

    public Pet(int id, String name, Integer age, String type, String colour)
    {
        this.petID = id;
        this.name = name;
        this.age = age;
        this.type = type;
        this.colour = colour;
        this.happiness = Constants.MAXHAPPINESS/2;
        this.foodSatisfaction = Constants.MAXFOODSATISFACTION/2;
        this.isDirty = false;
    }
    public Pet(String name, Integer age, String type, String colour, Float happiness, Float foodSatisfaction, Boolean isDirty)
    {
        this.name = name;
        this.age = age;
        this.type = type;
        this.colour = colour;
        this.happiness = happiness;
        this.foodSatisfaction = foodSatisfaction;
        this.isDirty = isDirty;
    }
    public Pet(String name, Integer age, String type)
    {
        this.name = name;
        this.age = age;
        this.type = type;
    }

    public Integer GetUserID()
    {
        return userID;
    }
    public void SetUserID(Integer userID)
    {
        this.userID = userID;
    }

    public Integer GetPetID()
    {
        return petID;
    }
    public void SetPetID(Integer petID)
    {
        this.petID = petID;
    }

    public String GetName()
    {
        return name;
    }
    public void SetName(String name){
        this.name = name;
    }

    public Integer GetAge()
    {
        return age;
    }
    public void SetAge(Integer age)
    {
        this.age = age;
    }

    public String GetType()
    {
        return type;
    }
    public void SetType(String type)
    {
        this.type = type;
    }

    public Float GetHappiness()
    {
        return happiness;
    }
    public void SetHappiness(Float happiness)
    {
        this.happiness = happiness;
    }

    public Float GetFoodSatisfaction()
    {
        return foodSatisfaction;
    }
    public void SetFoodSatisfaction(Float foodSatisfaction)
    {
        this.foodSatisfaction = foodSatisfaction;
    }

    public String GetColour()
    {
        return colour;
    }
    public void SetColour(String colour)
    {
        this.colour = colour;
    }

    public Boolean GetIsDirty()
    {
        return isDirty;
    }
    public void SetIsDirty(Boolean isDirty)
    {
        this.isDirty = isDirty;
    }

    public String GetPersonality()
    {
        return personality;
    }
    public void SetPersonality(String personality)
    {
        this.personality = personality;
    }

    public String GetCustomTrait()
    {
        return customTrait;
    }
    public void SetCustomTrait(String customTrait)
    {
        this.customTrait = customTrait;
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

    public String Feed(float food){
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