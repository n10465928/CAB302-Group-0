package vpm.gui_prototype.models.DatabaseStuff.PetData;

import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.PetStuff.Dog;
import vpm.gui_prototype.models.PetStuff.Cat;
import vpm.gui_prototype.models.PetStuff.Bird;
import vpm.gui_prototype.models.PetStuff.Fish;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that queries the SQL database for CRUD operations.
 */
public class SqlitePetDAO implements IPetDAO {
    public Connection connection;

    /**
     * Method that creates an instance of the connection to the Pets DAO
     */
    public SqlitePetDAO() {
        connection = SqlitePetDatabaseConnection.getInstance();
        createPetTable();
    }

    public void createPetTable() {
        try {
            Statement statement = connection.createStatement();
            String createPetsTableQuery = "CREATE TABLE IF NOT EXISTS pets ("
                    + "userId INTEGER NOT NULL,"
                    + "petId INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "petName VARCHAR NOT NULL,"
                    + "petType VARCHAR NOT NULL,"
                    + "petAge INTEGER NULL,"
                    + "petColour VARCHAR NULL,"
                    + "petHappiness FLOAT NOT NULL,"
                    + "petFoodSatisfaction FLOAT NOT NULL,"
                    + "petIsDirty BOOLEAN NOT NULL,"
                    + "petPersonality VARCHAR NULL,"
                    + "petCustomTrait VARCHAR NULL"
                    + ")";
            statement.execute(createPetsTableQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addPet(Pet pet, int userId) {
        try {
            PreparedStatement selectAllUsersPets = connection.prepareStatement("SELECT * FROM pets WHERE userID = ?");
            selectAllUsersPets.setInt(1, userId);
            ResultSet rs = selectAllUsersPets.executeQuery();
            int numberOfPets = 0;
            while (rs.next()) {
                numberOfPets++;
            }
            if (numberOfPets < 8) {
                PreparedStatement insertPet = connection.prepareStatement("INSERT INTO pets (userId, petName"
                        + ", petType, petAge, petColour, petHappiness, petFoodSatisfaction"
                        + ", petIsDirty, petPersonality, petCustomTrait)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                insertPet.setInt(1, userId);
                insertPet.setString(2, pet.GetName());
                insertPet.setString(3, pet.GetType());
                insertPet.setInt(4, pet.GetAge());
                insertPet.setString(5, pet.GetColour());
                insertPet.setFloat(6, pet.GetHappiness());
                insertPet.setFloat(7, pet.GetFoodSatisfaction());
                insertPet.setBoolean(8, pet.GetIsDirty());
                insertPet.setString(9, pet.GetPersonality());
                insertPet.setString(10, pet.GetCustomTrait());
                insertPet.executeUpdate();
            }
            // Add Else statement to notify user they have reached the maximum number of pets.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePet(Pet pet, int userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE pets SET petName = ?"
                    + ", petType = ?, petAge = ?, petColour = ?, petHappiness = ?"
                    + ", petFoodSatisfaction = ?, petIsDirty = ?"
                    + ", petPersonality = ?, petCustomTrait = ?"
                    + " WHERE userId = ? AND petId = ?");
            statement.setString(1, pet.GetName());
            statement.setString(2, pet.GetType());
            statement.setInt(3, pet.GetAge());
            statement.setString(4, pet.GetColour());
            statement.setFloat(5, pet.GetHappiness());
            statement.setFloat(6, pet.GetFoodSatisfaction());
            statement.setBoolean(7, pet.GetIsDirty());
            statement.setString(8, pet.GetPersonality());
            statement.setString(9, pet.GetCustomTrait());
            statement.setInt(10, userId);
            statement.setInt(11, pet.GetPetID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePet(Pet pet, int userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM pets WHERE userId = ? AND petId = ?");
            statement.setInt(1, userId);
            statement.setInt(2, pet.GetPetID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Pet getPet(int userId, int petId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM pets WHERE userId = ? AND petId = ?");
            statement.setInt(1, userId);
            statement.setInt(2, petId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String petName = resultSet.getString("petName");
                String petType = resultSet.getString("petType");
                int petAge = resultSet.getInt("petAge");
                String petColour = resultSet.getString("petColour");
                Float petHappiness = resultSet.getFloat("petHappiness");
                Float petFoodSatisfaction = resultSet.getFloat("petFoodSatisfaction");
                Boolean petIsDirty = resultSet.getBoolean("petIsDirty");
                String petPersonality = resultSet.getString("petPersonality");
                String petCustomTrait = resultSet.getString("petCustomTrait");

                // Create the appropriate subclass of Pet
                Pet pet = createPetSubclass(petType, petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality, petCustomTrait);
                pet.SetUserID(userId);
                pet.SetPetID(petId);
                return pet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pet> getAllUsersPets(int userId) {
        List<Pet> pets = new ArrayList<>();
        try {
            PreparedStatement selectAllUsersPets = connection.prepareStatement("SELECT * FROM pets WHERE userID = ?");
            selectAllUsersPets.setInt(1, userId);
            ResultSet resultSet = selectAllUsersPets.executeQuery();
            while (resultSet.next()) {
                int petId = resultSet.getInt("petId");
                String petName = resultSet.getString("petName");
                String petType = resultSet.getString("petType");
                int petAge = resultSet.getInt("petAge");
                String petColour = resultSet.getString("petColour");
                Float petHappiness = resultSet.getFloat("petHappiness");
                Float petFoodSatisfaction = resultSet.getFloat("petFoodSatisfaction");
                Boolean petIsDirty = resultSet.getBoolean("petIsDirty");
                String petPersonality = resultSet.getString("petPersonality");
                String petCustomTrait = resultSet.getString("petCustomTrait");

                Pet pet = createPetSubclass(petType, petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality, petCustomTrait);
                pet.SetUserID(userId);
                pet.SetPetID(petId);
                pets.add(pet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pets;
    }

    @Override
    public List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM pets";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                int petId = resultSet.getInt("petId");
                String petName = resultSet.getString("petName");
                String petType = resultSet.getString("petType");
                int petAge = resultSet.getInt("petAge");
                String petColour = resultSet.getString("petColour");
                Float petHappiness = resultSet.getFloat("petHappiness");
                Float petFoodSatisfaction = resultSet.getFloat("petFoodSatisfaction");
                Boolean petIsDirty = resultSet.getBoolean("petIsDirty");
                String petPersonality = resultSet.getString("petPersonality");
                String petCustomTrait = resultSet.getString("petCustomTrait");

                Pet pet = createPetSubclass(petType, petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality, petCustomTrait);
                pet.SetUserID(userId);
                pet.SetPetID(petId);
                pets.add(pet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pets;
    }

    // Helper method to create the appropriate subclass of Pet
    private Pet createPetSubclass(String petType, String petName, int petAge, String petColour, Float petHappiness, Float petFoodSatisfaction, Boolean petIsDirty, String petPersonality, String petCustomTrait) {
        switch (petType.toLowerCase()) {
            case "dog":
                return new Dog(petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality, petCustomTrait);
            case "cat":
                return new Cat(petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality, petCustomTrait);
            case "bird":
                return new Bird(petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality, petCustomTrait);
            case "fish":
                return new Fish(petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality, petCustomTrait);
            default:
                throw new IllegalArgumentException("Unknown pet type: " + petType);
        }
    }
}
