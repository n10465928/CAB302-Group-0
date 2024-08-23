package PetStuff;

import Constants.Constants;

public class Pet implements IPet
{
    String name;
    Integer age;
    Float happiness;
    Float hunger;

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
    }
    public Pet(String name, Integer age, Float happiness)
    {
        this.name = name;
        this.age = age;
        this.happiness = happiness;
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

    public Float GetHunger()
    {
        return hunger;
    }

    public void IncreaseHappiness(Float amount)
    {
        happiness += amount;
        if (happiness > 10f)
            happiness = 10f;
        if (happiness < 0f)
            happiness = 0f;
    }

    public void DecreaseHappiness(Float amount)
    {
        IncreaseHappiness(-amount);
    }

    public void Feed()//Food food)
    {
        //do something with that
    }
}
