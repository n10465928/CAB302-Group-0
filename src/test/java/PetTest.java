import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.PetStuff.Dog;
import vpm.gui_prototype.models.PetStuff.Pet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetTest {
    private Pet pet;

    @BeforeEach
    public void setUp() {
        pet = new Dog("Max", 4, "Black",
                10f, 10f, true,
                "Friendly", "A Good Boy");
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
}
