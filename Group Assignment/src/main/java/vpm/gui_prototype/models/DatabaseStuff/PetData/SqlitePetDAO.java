package vpm.gui_prototype.models.DatabaseStuff.PetData;

import vpm.gui_prototype.models.PetStuff.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that queries the SQL database for CRUD operations.
 */
public class SqlitePetDAO implements IPetDAO {
    private Connection connection;

    /**
     * Method that creates an instance of the connection to the Pets DAO
     */
    public SqlitePetDAO() {
        connection = SqlitePetDatabaseConnection.getInstance();
        createPetTable();
    }

    private void createPetTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String createPetsTableQuery = "CREATE TABLE IF NOT EXISTS pets ("
                    + "userId INTEGER NOT NULL,"
                    + "petId INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "petName VARCHAR NOT NULL,"
                    + "petType VARCHAR NOT NULL,"
                    + "petAge INTEGER NOT NULL,"
                    + "petColour VARCHAR NOT NULL"
                    + ")";
            statement.execute(createPetsTableQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that queries the SQL pets database and inserts a new pet into the database.
     * @param pet The pet to add.
     * @param userId The user's id.
     */
    @Override
    public void addPet(Pet pet, int userId) {
        try {
            PreparedStatement selectAllUsersPets = connection.prepareStatement("SELECT * FROM pets WHERE userID = ?");
            selectAllUsersPets.setInt(1, userId);
            ResultSet rs = selectAllUsersPets.executeQuery();
            int numberOfPets = 0;
            while( rs.next()){
                numberOfPets++;
            }
            if (numberOfPets < 8) {
                PreparedStatement insertPet = connection.prepareStatement("INSERT INTO pets (userId, petName" +
                        ", petType, petAge, petColour) VALUES (?, ?, ?, ?, ?)");
                insertPet.setInt(1, userId);
                insertPet.setString(2, pet.GetName());
                insertPet.setString(3, pet.GetType());
                insertPet.setInt(4, pet.GetAge());
                insertPet.setString(5, pet.GetColour());
                //insertPet.setFloat(6, pet.GetHappiness());
                //insertPet.setFloat(7, pet.GetFoodSatisfaction());
                //insertPet.setBoolean(8, pet.GetIsDirty());
                insertPet.executeUpdate();
            }
            //Need to add Else statement that lets user know they have reached the maximum number of pets.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that queries the SQL pets database and updates the pets on the database
     * @param pet The pet to update.
     * @param userId The user's id.
     */
    @Override
    public void updatePet(Pet pet, int userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE pets SET petName = ?" +
                    ", petType = ?, petAge = ?, petColour = ?" +
                    " WHERE userId = ? AND petId = ?");
            statement.setString(1, pet.GetName());
            statement.setString(2, pet.GetType());
            statement.setInt(3, pet.GetAge());
            statement.setString(4, pet.GetColour());
            //statement.setFloat(5, pet.GetHappiness());
            //statement.setFloat(6, pet.GetFoodSatisfaction());
            //statement.setBoolean(7, pet.GetIsDirty());
            statement.setInt(5, userId);
            statement.setInt(6, pet.GetPetID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that queries the SQL pets database and deletes the pet from the database.
     * @param pet The pet to delete.
     * @param userId The user's id.
     */
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

    /**
     * Method that queries the SQL pets database and gets a pet using a usersId and petId.
     * @param userId The id of the user.
     * @param petId The id of the pet to retrieve.
     * @return Return the pet if found
     */
    @Override
    public Pet getPet(int userId, int petId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM contacts " +
                    "WHERE userId = ? AND petId = ?");
            statement.setInt(1, userId);
            statement.setInt(2, petId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String petName = resultSet.getString("petName");
                String petType = resultSet.getString("petType");
                int petAge = resultSet.getInt("petAge");
                String petColour = resultSet.getString("petColour");
                //Float petHappiness = resultSet.getFloat("petHappiness");
                //Float petFoodSatisfaction = resultSet.getFloat("petFoodSatisfaction");
                //Boolean petIsDirty = resultSet.getBoolean("petIsDirty");
                Pet pet = new Pet(petName, petAge, petType, petColour);
                pet.SetPetID(petId);
                return pet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method that queries the SQL pets database and returns all the users pets using userId.
     * @param userId The id of the user whose pets to retrieve.
     * @return
     */
    @Override
    public List<Pet> getAllUsersPets(int userId) {
        List<Pet> pets = new ArrayList<>();
        try {
            PreparedStatement selectAllUsersPets = connection.prepareStatement("SELECT * FROM pets WHERE userID = ?");
            selectAllUsersPets.setInt(1, userId);
            ResultSet resultSet = selectAllUsersPets.executeQuery();
            while (resultSet.next()) {
                String petName = resultSet.getString("petName");
                String petType = resultSet.getString("petType");
                int petAge = resultSet.getInt("petAge");
                String petColour = resultSet.getString("petColour");
                //Float petHappiness = resultSet.getFloat("petHappiness");
                //Float petFoodSatisfaction = resultSet.getFloat("petFoodSatisfaction");
                //Boolean petIsDirty = resultSet.getBoolean("petIsDirty");
                Pet pet = new Pet(petName, petAge, petType, petColour);
                pets.add(pet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pets;
    }

    /**
     * Method that queries the SQL pets database and returns all the pets in the database.
     * @return
     */
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
                //Float petHappiness = resultSet.getFloat("petHappiness");
                //Float petFoodSatisfaction = resultSet.getFloat("petFoodSatisfaction");
                //Boolean petIsDirty = resultSet.getBoolean("petIsDirty");
                Pet pet = new Pet(petName, petAge, petType, petColour);
                pet.SetUserID(userId);
                pet.SetPetID(petId);
                pets.add(pet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pets;
    }
}