package vpm.gui_prototype.models.DatabaseStuff.PetData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import vpm.gui_prototype.models.PetStuff.Pet;

public class SqlitePetDAO implements IPetDAO {
    private Connection connection;
    private int highestPetIndex;

    public SqlitePetDAO() {
        connection = PetDatabaseSqliteConnection.getInstance();
        createPetTable();
        // Used for testing, to be removed later
        //insertSampleData();
    }

    private void createPetTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String createPetsTableQuery = "CREATE TABLE IF NOT EXISTS pets ("
                    + "userId INTEGER NOT NULL,"
                    + "petId INTEGER AUTOINCREMENT,"
                    + "petName VARCHAR NOT NULL,"
                    + "petType VARCHAR NOT NULL,"
                    + "petAge INTEGER NOT NULL,"
                    + "petColour VARCHAR NOT NULL"
                    + "petHealth INTEGER NOT NULL,"
                    + "petHappiness INTEGER NOT NULL,"
                    + "petHunger INTEGER NOT NULL,"
                    + "petMood VARCHAR NOT NULL"
                    + ")";
            statement.execute(createPetsTableQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    private void insertSampleData() {
        try {
            // Clear before inserting
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM contacts";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO contacts (firstName, lastName, phone, email) VALUES "
                    + "('John', 'Doe', '0423423423', 'johndoe@example.com'),"
                    + "('Jane', 'Doe', '0423423424', 'janedoe@example.com'),"
                    + "('Jay', 'Doe', '0423423425', 'jaydoe@example.com')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
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
            if (numberOfPets < 6) {
                PreparedStatement insertPet = connection.prepareStatement("INSERT INTO pets (userId, petName" +
                        ", petType, petAge, petColour, petHealth, petHappiness, petHunger, petMood) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                insertPet.setInt(1, userId);
                insertPet.setString(2, pet.GetName());
                insertPet.setString(3, pet.GetType());
                insertPet.setInt(4, pet.GetAge());
                insertPet.setString(5, pet.GetColour());
                insertPet.setFloat(7, pet.GetHappiness());
                insertPet.setFloat(8, pet.GetFoodSatisfaction());
                insertPet.executeUpdate();
            }
            //Need to add Else statement that lets user know they have reached the maximum number of pets.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePet(Pet pet, int userId) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE pets SET petName = ?" +
                    ", petType = ?, petAge = ?, petColour = ?, petHealth = ?, petHappiness = ?" +
                    ", petHunger = ?, petMood = ? WHERE userId = ? AND petId = ?");
            statement.setString(1, pet.GetName());
            statement.setString(2, pet.GetType());
            statement.setInt(3, pet.GetAge());
            statement.setString(4, pet.GetColour());
            statement.setFloat(6, pet.GetHappiness());
            statement.setFloat(7, pet.GetFoodSatisfaction());
            statement.setInt(9, userId);
            statement.setInt(10, pet.GetID());
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
            statement.setInt(2, pet.GetID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                float petHappiness = resultSet.getFloat("petHappiness");
                float petFoodSatisfaction = resultSet.getFloat("petFoodSatisfaction");
                Pet pet = new Pet(petId, petName, petAge, petType, petColour, petHappiness, petFoodSatisfaction);
                return pet;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pet> getAllPets(int userId) {
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
                float petHappiness = resultSet.getFloat("petHappiness");
                float petFoodSatisfaction = resultSet.getFloat("petFoodSatisfaction");
                int petId = resultSet.getInt("petId");
                Pet pet = new Pet(petId, petName, petAge, petType, petColour, petHappiness, petFoodSatisfaction);
                pets.add(pet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pets;
    }
}