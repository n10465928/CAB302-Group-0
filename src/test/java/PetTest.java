import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.Constants.Constants;
import vpm.gui_prototype.models.PetStuff.Dog;
import vpm.gui_prototype.models.PetStuff.Pet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PetTest {
    private Pet pet;

    @BeforeEach
    public void setUp() {
        pet = new Dog("Max", 4, "Black",
                10f, 10f, true,
                "Playful");
    }
    @Test
    public void testSetUserId() {
        pet.setUserID(2);
        assertEquals(2, pet.getUserID());
    }

    @Test
    public void testSetPetId() {
        pet.setPetID(2);
        assertEquals(2, pet.getPetID());
    }

    @Test
    public void testGetName() {
        assertEquals("Max", pet.getName());
    }

    @Test
    public void testSetName() {
        pet.setName("Felix");
        assertEquals("Felix", pet.getName());
    }

    @Test
    public void testGetType() {
        assertEquals("Dog", pet.getType());
    }

    @Test
    public void testSetType() {
        pet.setType("Cat");
        assertEquals("Cat", pet.getType());
    }

    @Test
    public void testGetAge() {
        assertEquals(4, pet.getAge());
    }

    @Test
    public void testSetAge() {
        pet.setAge(6);
        assertEquals(6, pet.getAge());
    }

    @Test
    public void testGetColour() {
        assertEquals("Black", pet.getColour());
    }

    @Test
    public void testSetColour() {
        pet.setColour("Brown");
        assertEquals("Brown", pet.getColour());
    }

    @Test
    public void testGetHappiness(){
        assertEquals(10f, pet.getHappiness());
    }

    @Test
    public void testSetHappiness(){
        pet.setHappiness(5f);
        assertEquals(5f, pet.getHappiness());
    }

    @Test
    public void testGetFoodSatisfaction(){
        assertEquals(10f, pet.getFoodSatisfaction());
    }

    @Test
    public void testSetFoodSatisfaction(){
        pet.setFoodSatisfaction(5f);
        assertEquals(5f, pet.getFoodSatisfaction());
    }


    @Test
    public void testGetIsDirty(){
        assertEquals(true, pet.getIsDirty());
    }

    @Test
    public void testSetIsDirty(){
        pet.setIsDirty(true);
        assertEquals(true, pet.getIsDirty());
    }

    @Test
    public void testGetPersonality(){
        assertEquals("Playful", pet.getPersonality());
    }

    @Test
    public void testSetPersonality(){
        pet.setPersonality("Loyal");
        assertEquals("Loyal", pet.getPersonality());
    }

    @Test
    void testRoundToTwoDecimalPlaces() {
        assertEquals(1.23f, pet.roundToTwoDecimalPlaces(1.2345f), 0.01f);
        assertEquals(1.24f, pet.roundToTwoDecimalPlaces(1.2399f), 0.01f);
        assertEquals(0.0f, pet.roundToTwoDecimalPlaces(0.004f), 0.01f);
        assertEquals(-1.25f, pet.roundToTwoDecimalPlaces(-1.255f), 0.01f);
        assertEquals(-1.25f, pet.roundToTwoDecimalPlaces(-1.254f), 0.01f);
        assertEquals(2.0f, pet.roundToTwoDecimalPlaces(2.0001f), 0.01f);
        assertEquals(0.0f, pet.roundToTwoDecimalPlaces(0.0f), 0.01f);
    }
    @Test
    void testIncreaseHappiness(){
        pet = new Dog("Max", 4, "Black",
                5.0f, 10f, true,
                "Playful");
        pet.increaseHappiness(1.0f);
        assertEquals(6.0f, pet.getHappiness());
        pet.increaseHappiness(20.0f);
        assertEquals(10.0f, pet.getHappiness());
        pet.increaseHappiness(-20.0f);
        assertEquals(0.0f, pet.getHappiness());
    }
    @Test
    public void testPlayWithSameTypeIncreasesHappiness() {
        Float initialHappiness = pet.getHappiness();
        String result = pet.playWtihPet("Dog", 5.0f); // Assuming the pet's type is "Dog"

        assertEquals("Too happy already, cannot be anymore", result);}

    @Test
    public void testPlayWithDifferentTypeDecreasesHappiness() {
        Float initialHappiness = pet.getHappiness();
        String result = pet.playWtihPet("Cat", 5.0f); // Assuming the pet's type is "Dog"

        assertEquals("", result);
        assertTrue(pet.getHappiness() < initialHappiness, "Happiness should decrease when playing with a different type.");
    }

    @Test
    public void testPlayWithDifferentTypeMinHappiness() {
        pet.setHappiness(0f); // Set pet happiness to minimum
        String result = pet.playWtihPet("Cat", 5.0f); // Play with a different type

        assertEquals("Too unhappy already, cannot be anymore", result);
        assertEquals(0f, pet.getHappiness(), "Happiness should remain at minimum.");
    }


}
