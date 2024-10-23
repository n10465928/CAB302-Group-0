import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.FoodStuff.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FoodTest {

    @Test
    public void testDefaultConstructor() {
        Food bone = new Bone();
        assertEquals(0f, bone.GetNutritionalValue());

        Food pate = new Pate();
        assertEquals(0f, pate.GetNutritionalValue());

        Food seed = new Seed();
        assertEquals(0f, seed.GetNutritionalValue());

        Food milk = new Milk();
        assertEquals(0f, milk.GetNutritionalValue());

        Food pallets = new Pellets();
        assertEquals(0f, pallets.GetNutritionalValue());
    }

    @Test
    public void testParameterizedConstructor() {
        Float nutritionalValueL = 1.0f;
        Float nutritionalValueR = 1.0f;
        Food bone = new Bone(nutritionalValueL, nutritionalValueR);
        assertEquals(nutritionalValueL, bone.GetNutritionalValueL());
        assertEquals(nutritionalValueR, bone.GetNutritionalValueR());
    }

    @Test
    public void testFullParameterizedConstructor() {
        Float nutritionalValueL = 1.0f;
        Float nutritionalValueR = 1.0f;
        Float fluctuation = 3.0f;
        Food bone = new Bone(nutritionalValueL, nutritionalValueR, fluctuation);
        assertEquals(nutritionalValueL, bone.GetNutritionalValueL());
        assertEquals(nutritionalValueR, bone.GetNutritionalValueR());
        assertEquals(fluctuation, bone.GetFluctuation());
    }

    @Test
    public void testGetCompatiblePets() {
        List<String> compatiblePets = Arrays.asList("Cat", "Dog", "Bird", "Fish");
        Food pallets = new Pellets();
        assertEquals(pallets.GetCompatiblePet(), compatiblePets);

        compatiblePets = Arrays.asList("Cat", "Dog");
        Food pate = new Pate();
        assertEquals(pate.GetCompatiblePet(), compatiblePets);

        compatiblePets = Arrays.asList("Bird", "Fish");
        Food seed = new Seed();
        assertEquals(seed.GetCompatiblePet(), compatiblePets);

        compatiblePets = Arrays.asList("Cat", "Dog");
        Food bone = new Bone();
        assertEquals(bone.GetCompatiblePet(), compatiblePets);

        compatiblePets = Arrays.asList("Cat", "Dog");
        Food milk = new Milk();
        assertEquals(milk.GetCompatiblePet(), compatiblePets);
    }
}
