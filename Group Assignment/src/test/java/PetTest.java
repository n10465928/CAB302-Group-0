import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.PetStuff.Pet;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetTest {
    private Pet pet;

    @BeforeEach
    public void setUp() {
        pet = new Pet("Max", 4, "Dog", "Black",
                10f, 10f, true,
                "Friendly", "A Good Boy");
    }
    @Test
    public void testSetUserId() {
        pet.SetUserID(2);
        assertEquals(2, pet.GetUserID());
    }

    @Test
    public void testSetPetId() {
        pet.SetPetID(2);
        assertEquals(2, pet.GetPetID());
    }

    @Test
    public void testGetName() {
        assertEquals("Max", pet.GetName());
    }

    @Test
    public void testSetName() {
        pet.SetName("Felix");
        assertEquals("Felix", pet.GetName());
    }

    @Test
    public void testGetType() {
        assertEquals("Dog", pet.GetType());
    }

    @Test
    public void testSetType() {
        pet.SetType("Cat");
        assertEquals("Cat", pet.GetType());
    }

    @Test
    public void testGetAge() {
        assertEquals(4, pet.GetAge());
    }

    @Test
    public void testSetAge() {
        pet.SetAge(6);
        assertEquals(6, pet.GetAge());
    }

    @Test
    public void testGetColour() {
        assertEquals("Black", pet.GetColour());
    }

    @Test
    public void testSetColour() {
        pet.SetColour("Brown");
        assertEquals("Brown", pet.GetColour());
    }

    @Test
    public void testGetHappiness(){
        assertEquals(10f, pet.GetHappiness());
    }

    @Test
    public void testSetHappiness(){
        pet.SetHappiness(5f);
        assertEquals(5f, pet.GetHappiness());
    }

    @Test
    public void testGetFoodSatisfaction(){
        assertEquals(10f, pet.GetFoodSatisfaction());
    }

    @Test
    public void testSetFoodSatisfaction(){
        pet.SetHappiness(5f);
        assertEquals(5f, pet.GetHappiness());
    }

    @Test
    public void testGetIsDirty(){
        assertEquals(false, pet.GetIsDirty());
    }

    @Test
    public void testSetIsDirty(){
        pet.SetIsDirty(true);
        assertEquals(true, pet.GetIsDirty());
    }
}
