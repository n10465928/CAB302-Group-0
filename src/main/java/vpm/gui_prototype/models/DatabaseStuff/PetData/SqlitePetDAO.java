package vpm.gui_prototype.models.DatabaseStuff.PetData;

import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.PetStuff.Dog;
import vpm.gui_prototype.models.PetStuff.Cat;
import vpm.gui_prototype.models.PetStuff.Bird;
import vpm.gui_prototype.models.PetStuff.Fish;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the IPetDAO interface for performing CRUD operations
 * on pets in a SQLite database.
 */
public class SqlitePetDAO implements IPetDAO {
    public Connection connection;

    /**
     * Constructs an instance of SqlitePetDAO and establishes a connection
     * to the database. It also creates the pets table if it does not exist.
     */
    public SqlitePetDAO() {
        connection = SqlitePetDatabaseConnection.getInstance();
        createPetTable();
    }

    /**
     * Creates the pets table in the database if it does not already exist.
     */
    public void createPetTable() {
        try (Statement statement = connection.createStatement()) {
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
                    "petPersonality VARCHAR NULL," +
                    "lastInteractionTime TIMESTAMP NULL" +
                    ")";
            statement.execute(createPetsTableQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addPet(Pet pet, int userId) {
        try {
            // Check the number of pets for the user
            PreparedStatement selectAllUsersPets = connection.prepareStatement("SELECT * FROM pets WHERE userId = ?");
            selectAllUsersPets.setInt(1, userId);
            ResultSet rs = selectAllUsersPets.executeQuery();
            int numberOfPets = 0;
            while (rs.next()) {
                numberOfPets++;
            }

            // Allow adding a pet if the limit is not reached
            if (numberOfPets < 8) {
                PreparedStatement insertPet = connection.prepareStatement("INSERT INTO pets (userId, petName, petType, petAge, petColour, " +
                        "petHappiness, petFoodSatisfaction, petIsDirty, petPersonality, lastInteractionTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                insertPet.setInt(1, userId);
                insertPet.setString(2, pet.getName());
                insertPet.setString(3, pet.getType());
                insertPet.setInt(4, pet.getAge());
                insertPet.setString(5, pet.getColour());
                insertPet.setFloat(6, pet.getHappiness());
                insertPet.setFloat(7, pet.getFoodSatisfaction());
                insertPet.setBoolean(8, pet.getIsDirty());
                insertPet.setString(9, pet.getPersonality());
                insertPet.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                insertPet.executeUpdate();
            } else {
                // Add logic to notify the user about the maximum limit of pets
                System.out.println("Maximum number of pets reached for user ID: " + userId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePet(Pet pet, int userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE pets SET petName = ?, petType = ?, petAge = ?, petColour = ?, " +
                    "petHappiness = ?, petFoodSatisfaction = ?, petIsDirty = ?, petPersonality = ?, lastInteractionTime = ? WHERE userId = ? AND petId = ?");
            statement.setString(1, pet.getName());
            statement.setString(2, pet.getType());
            statement.setInt(3, pet.getAge());
            statement.setString(4, pet.getColour());
            statement.setFloat(5, pet.getHappiness());
            statement.setFloat(6, pet.getFoodSatisfaction());
            statement.setBoolean(7, pet.getIsDirty());
            statement.setString(8, pet.getPersonality());
            statement.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(10, userId);
            statement.setInt(11, pet.getPetID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve the last interaction time from the database
    public LocalDateTime getLastInteractionTime(int petId) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT lastInteractionTime FROM pets WHERE petId = ?");
            statement.setInt(1, petId);
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
        return null;
    }

    // Method to set the last interaction time for a pet
    public void setLastInteractionTime(int petId) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE pets SET lastInteractionTime = ? WHERE petId = ?");
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));  // Set current time
            statement.setInt(2, petId);
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
                return createPetFromResultSet(resultSet, userId, petId);
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
                pets.add(createPetFromResultSet(resultSet, userId, resultSet.getInt("petId")));
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
                pets.add(createPetFromResultSet(resultSet, resultSet.getInt("userId"), resultSet.getInt("petId")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pets;
    }

    /**
     * Creates a Pet object from the ResultSet based on the pet type.
     *
     * @param resultSet The ResultSet containing the pet data.
     * @param userId The ID of the user who owns the pet.
     * @param petId The ID of the pet.
     * @return A Pet object corresponding to the data in the ResultSet.
     * @throws SQLException If an SQL error occurs while accessing the ResultSet.
     */
    private Pet createPetFromResultSet(ResultSet resultSet, int userId, int petId) throws SQLException {
        String petName = resultSet.getString("petName");
        String petType = resultSet.getString("petType");
        int petAge = resultSet.getInt("petAge");
        String petColour = resultSet.getString("petColour");
        float petHappiness = resultSet.getFloat("petHappiness");
        float petFoodSatisfaction = resultSet.getFloat("petFoodSatisfaction");
        boolean petIsDirty = resultSet.getBoolean("petIsDirty");
        String petPersonality = resultSet.getString("petPersonality");

        Pet pet = createPetSubclass(petType, petName, petAge, petColour, petHappiness, petFoodSatisfaction, petIsDirty, petPersonality);
        pet.setUserID(userId);
        pet.setPetID(petId);
        return pet;
    }

    /**
     * Creates the appropriate subclass of Pet based on the pet type.
     *
     * @param petType             The type of the pet (e.g., "dog", "cat").
     * @param petName             The name of the pet.
     * @param petAge              The age of the pet.
     * @param petColour           The colour of the pet.
     * @param petHappiness        The happiness level of the pet.
     * @param petFoodSatisfaction The food satisfaction level of the pet.
     * @param petIsDirty          The dirty status of the pet.
     * @param petPersonality      The personality of the pet.
     * @return A Pet object of the appropriate subclass.
     * @throws IllegalArgumentException if the pet type is unknown.
     */
    private Pet createPetSubclass(String petType, String petName, int petAge, String petColour, float petHappiness, float petFoodSatisfaction, boolean petIsDirty, String petPersonality) {
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
