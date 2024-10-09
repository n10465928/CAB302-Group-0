import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;
import vpm.gui_prototype.models.PetStuff.Cat;
import vpm.gui_prototype.models.PetStuff.Dog;
import vpm.gui_prototype.models.PetStuff.Pet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SqlitePetDAOTest {

    private Connection connection;
    private SqlitePetDAO sqlitePetDAO;

    @BeforeEach
    public void setUp() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        sqlitePetDAO = new SqlitePetDAO();
        sqlitePetDAO.connection = connection;
        sqlitePetDAO.createPetTable();
    }

    @AfterEach
    public void tearDown() throws Exception {
        connection.close();
    }

    @Test
    public void testCreatePetTable() throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='pets'");
        assertTrue(resultSet.next());
    }

    @Test
    public void testAddPet() throws Exception {
        Pet pet = new Dog("Buddy", 3, "Brown", 8.5f, 7.5f, false, "Friendly", "Loyal");
        pet.SetPetID(1);
        sqlitePetDAO.addPet(pet, 1);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM pets WHERE userId = ? AND petName = ?");
        statement.setInt(1, 1);
        statement.setString(2, "Buddy");
        ResultSet resultSet = statement.executeQuery();

        assertTrue(resultSet.next());
        assertEquals("Buddy", resultSet.getString("petName"));
        assertEquals("Dog", resultSet.getString("petType"));
        assertEquals(3, resultSet.getInt("petAge"));
        assertEquals("Brown", resultSet.getString("petColour"));
        assertEquals(8.5f, resultSet.getFloat("petHappiness"));
        assertEquals(7.5f, resultSet.getFloat("petFoodSatisfaction"));
        assertFalse(resultSet.getBoolean("petIsDirty"));
        assertEquals("Friendly", resultSet.getString("petPersonality"));
        assertEquals("Loyal", resultSet.getString("petCustomTrait"));
    }

    @Test
    public void testUpdatePet() throws Exception {
        Pet pet = new Dog("Buddy", 3, "Brown", 8.5f, 7.5f, false, "Friendly", "Loyal");
        pet.SetPetID(1);
        sqlitePetDAO.addPet(pet, 1);

        pet.SetName("Max");
        pet.SetAge(4);
        sqlitePetDAO.updatePet(pet, 1);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM pets WHERE userId = ? AND petName = ?");
        statement.setInt(1, 1);
        statement.setString(2, "Max");
        ResultSet resultSet = statement.executeQuery();

        assertTrue(resultSet.next());
        assertEquals("Max", resultSet.getString("petName"));
        assertEquals(4, resultSet.getInt("petAge"));
    }

    @Test
    public void testDeletePet() throws Exception {
        Pet pet = new Dog("Buddy", 3, "Brown", 8.5f, 7.5f, false, "Friendly", "Loyal");
        pet.SetPetID(1);
        sqlitePetDAO.addPet(pet, 1);

        sqlitePetDAO.deletePet(pet, 1);

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM pets WHERE userId = ? AND petName = ?");
        statement.setInt(1, 1);
        statement.setString(2, "Buddy");
        ResultSet resultSet = statement.executeQuery();

        assertFalse(resultSet.next());
    }

    @Test
    public void testGetPet() throws Exception {
        Pet pet = new Dog("Buddy", 3, "Brown", 8.5f, 7.5f, false, "Friendly", "Loyal");
        pet.SetPetID(1);
        sqlitePetDAO.addPet(pet, 1);

        Pet retrievedPet = sqlitePetDAO.getPet(1, pet.GetPetID());

        assertNotNull(retrievedPet);
        assertEquals("Buddy", retrievedPet.GetName());
        assertEquals("Dog", retrievedPet.GetType());
        assertEquals(3, retrievedPet.GetAge());
        assertEquals("Brown", retrievedPet.GetColour());
        assertEquals(8.5f, retrievedPet.GetHappiness());
        assertEquals(7.5f, retrievedPet.GetFoodSatisfaction());
        assertFalse(retrievedPet.GetIsDirty());
        assertEquals("Friendly", retrievedPet.GetPersonality());
        assertEquals("Loyal", retrievedPet.GetCustomTrait());
    }

    @Test
    public void testGetAllUsersPets() throws Exception {
        Pet pet1 = new Dog("Buddy", 3, "Brown", 8.5f, 7.5f, false, "Friendly", "Loyal");
        Pet pet2 = new Cat("Max", 4, "Black", 9.0f, 8.0f, true, "Playful", "Curious");
        sqlitePetDAO.addPet(pet1, 1);
        sqlitePetDAO.addPet(pet2, 1);

        List<Pet> pets = sqlitePetDAO.getAllUsersPets(1);

        assertNotNull(pets);
        assertEquals(2, pets.size());
    }

    @Test
    public void testGetAllPets() throws Exception {
        Pet pet1 = new Dog("Buddy", 3, "Brown", 8.5f, 7.5f, false, "Friendly", "Loyal");
        Pet pet2 = new Cat("Max", 4, "Black", 9.0f, 8.0f, true, "Playful", "Curious");
        sqlitePetDAO.addPet(pet1, 1);
        sqlitePetDAO.addPet(pet2, 2);

        List<Pet> pets = sqlitePetDAO.getAllPets();

        assertNotNull(pets);
        assertEquals(2, pets.size());
    }
}
