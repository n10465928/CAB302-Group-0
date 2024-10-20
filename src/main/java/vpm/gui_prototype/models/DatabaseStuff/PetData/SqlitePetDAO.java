package vpm.gui_prototype.models.DatabaseStuff.PetData;

import vpm.gui_prototype.models.PetStuff.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the IPetDAO interface for performing CRUD operations
 * on pets in a SQLite database.
 */
public class SqlitePetDAO implements IPetDAO {
    private final Connection connection;

    /**
     * Constructs an instance of SqlitePetDAO and establishes a connection
     * to the database. It also creates the pets table if it does not exist.
     */
    public SqlitePetDAO() {
        connection = SqlitePetDatabaseConnection.getInstance();
        createTables();  // Create necessary tables if they don't exist
    }

    /**
     * Creates the pets and users tables in the database if they do not already exist.
     */
    private void createTables() {
        try (Statement statement = connection.createStatement()) {
            // Create pets table
            String createPetsTableQuery = "CREATE TABLE IF NOT EXISTS pets (" +
                    "userId INTEGER NOT NULL, " +
                    "petId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "petName VARCHAR NOT NULL, " +
                    "petType VARCHAR NOT NULL, " +
                    "petAge INTEGER NULL, " +
                    "petColour VARCHAR NULL, " +
                    "petHappiness FLOAT NOT NULL, " +
                    "petFoodSatisfaction FLOAT NOT NULL, " +
                    "petIsDirty BOOLEAN NOT NULL, " +
                    "petPersonality VARCHAR NULL" +
                    ")";
            statement.execute(createPetsTableQuery);

            // Create users table with lastInteractionTime column
            String createUsersTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username VARCHAR NOT NULL, " +
                    "password VARCHAR NOT NULL, " +
                    "email VARCHAR, " +
                    "phone VARCHAR, " +
                    "lastInteractionTime TIMESTAMP NULL" +  // New column for storing last interaction time
                    ")";
            statement.execute(createUsersTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addPet(Pet pet, int userId) {
        try {
            PreparedStatement insertPet = connection.prepareStatement("INSERT INTO pets (userId, petName, petType, petAge, petColour, " +
                    "petHappiness, petFoodSatisfaction, petIsDirty, petPersonality) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertPet.setInt(1, userId);
            insertPet.setString(2, pet.getName());
            insertPet.setString(3, pet.getType());
            insertPet.setInt(4, pet.getAge());
            insertPet.setString(5, pet.getColour());
            insertPet.setFloat(6, pet.getHappiness());
            insertPet.setFloat(7, pet.getFoodSatisfaction());
            insertPet.setBoolean(8, pet.getIsDirty());
            insertPet.setString(9, pet.getPersonality());
            insertPet.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePet(Pet pet, int userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE pets SET petName = ?, petType = ?, petAge = ?, petColour = ?, " +
                    "petHappiness = ?, petFoodSatisfaction = ?, petIsDirty = ?, petPersonality = ? WHERE userId = ? AND petId = ?");
            statement.setString(1, pet.getName());
            statement.setString(2, pet.getType());
            statement.setInt(3, pet.getAge());
            statement.setString(4, pet.getColour());
            statement.setFloat(5, pet.getHappiness());
            statement.setFloat(6, pet.getFoodSatisfaction());
            statement.setBoolean(7, pet.getIsDirty());
            statement.setString(8, pet.getPersonality());
            statement.setInt(9, userId);
            statement.setInt(10, pet.getPetID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePet(Pet pet, int userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM pets WHERE userId = ? AND petId = ?");
            statement.setInt(1, userId);
            statement.setInt(2, pet.getPetID());
            statement.executeUpdate();
        } catch (SQLException e) {
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
                return createPetFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pet> getAllUsersPets(int userId) {
        List<Pet> pets = new ArrayList<>();
        try {
            PreparedStatement selectAllUsersPets = connection.prepareStatement("SELECT * FROM pets WHERE userId = ?");
            selectAllUsersPets.setInt(1, userId);
            ResultSet resultSet = selectAllUsersPets.executeQuery();
            while (resultSet.next()) {
                pets.add(createPetFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    @Override
    public List<Pet> getAllPets() {
        List<Pet> pets = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM pets");
            while (resultSet.next()) {
                pets.add(createPetFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    /**
     * Retrieves the last interaction time for a user from the database.
     *
     * @param userId The ID of the user.
     * @return The last interaction time as a LocalDateTime object, or null if not found.
     */
    public LocalDateTime getLastInteractionTime(int userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT lastInteractionTime FROM users WHERE userId = ?");
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp("lastInteractionTime");
                if (timestamp != null) {
                    return timestamp.toLocalDateTime();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Return null if no interaction time is found
    }

    /**
     * Sets the last interaction time for a user in the database.
     *
     * @param userId The ID of the user.
     * @param interactionTime The interaction time to save.
     */
    public void setLastInteractionTime(int userId, LocalDateTime interactionTime) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE users SET lastInteractionTime = ? WHERE userId = ?");
            statement.setTimestamp(1, Timestamp.valueOf(interactionTime));
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a Pet object from the ResultSet.
     *
     * @param resultSet The ResultSet containing the pet data.
     * @return A Pet object.
     * @throws SQLException If an SQL error occurs while accessing the ResultSet.
     */
    private Pet createPetFromResultSet(ResultSet resultSet) throws SQLException {
        int petId = resultSet.getInt("petId");
        String petName = resultSet.getString("petName");
        String petType = resultSet.getString("petType");
        int petAge = resultSet.getInt("petAge");
        String petColour = resultSet.getString("petColour");
        float petHappiness = resultSet.getFloat("petHappiness");
        float petFoodSatisfaction = resultSet.getFloat("petFoodSatisfaction");
        boolean petIsDirty = resultSet.getBoolean("petIsDirty");
        String petPersonality = resultSet.getString("petPersonality");

        Pet pet = createPetSubclass(petType, petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality);
        pet.setPetID(petId);
        return pet;
    }

    /**
     * Creates the appropriate subclass of Pet based on the pet type.
     *
     * @param petType The type of the pet.
     * @param petName The name of the pet.
     * @param petAge The age of the pet.
     * @param petColour The colour of the pet.
     * @param petHappiness The happiness level of the pet.
     * @param petFoodSatisfaction The food satisfaction level of the pet.
     * @param petIsDirty Indicates if the pet is dirty.
     * @param petPersonality The personality of the pet.
     * @return A Pet object of the appropriate subclass.
     */
    private Pet createPetSubclass(String petType, String petName, int petAge, String petColour, float petHappiness, float petFoodSatisfaction, boolean petIsDirty, String petPersonality) {
        // Code to create specific subclasses (Dog, Cat, etc.) based on petType
        switch (petType.toLowerCase()) {
            case "dog":
                return new Dog(petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality);
            case "cat":
                return new Cat(petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality);
            case "bird":
                return new Bird(petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality);
            case "fish":
                return new Fish(petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality);
            default:
                throw new IllegalArgumentException("Unknown pet type: " + petType);
        }
    }
}
