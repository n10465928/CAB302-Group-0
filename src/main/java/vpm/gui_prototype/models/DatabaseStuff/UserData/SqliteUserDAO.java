package vpm.gui_prototype.models.DatabaseStuff.UserData;

import vpm.gui_prototype.models.UserStuff.User;
import vpm.gui_prototype.services.PasswordHashingService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of IUserDAO for managing user data in a SQLite database.
 */
public class SqliteUserDAO implements IUserDAO {
    // Database connection object
    public Connection connection;
    // Service for handling password hashing
    public PasswordHashingService hashService;

    // DateTimeFormatter to parse and format timestamps
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructs an instance of SqliteUserDAO and establishes a connection
     * to the database. It also creates the users table if it does not exist.
     */
    public SqliteUserDAO() {
        connection = UserSqliteConnection.getInstance(); // Obtain database connection
        hashService = new PasswordHashingService(); // Initialize password hashing service
        createTable(); // Ensure the users table is created
    }

    /**
     * Creates the users table in the database if it does not already exist.
     */
    public void createTable() {
        try {
            Statement statement = connection.createStatement();
            // SQL query to create the users table
            String query = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username VARCHAR NOT NULL," +
                    "password VARCHAR NOT NULL," +
                    "email VARCHAR NULL," +
                    "phone VARCHAR NULL," +
                    "last_interaction_time DATETIME" +
                    ")";
            statement.execute(query); // Execute the create table statement
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions that occur
        }
    }

    @Override
    public void addUser(User user) {
        try {
            // Prepare statement to insert a new user
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (username, password, email, phone, last_interaction_time) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getLastInteractionTime() != null ? user.getLastInteractionTime().format(FORMATTER) : null);

            statement.executeUpdate(); // Execute the insert
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1)); // Set the generated user ID
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions that occur
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            // Prepare statement to update user information
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET username = ?, password = ?, email = ?, phone = ?, last_interaction_time = ? WHERE id = ?"
            );
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getLastInteractionTime() != null ? user.getLastInteractionTime().format(FORMATTER) : null);
            statement.setInt(6, user.getId());

            statement.executeUpdate(); // Execute the update
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions that occur
        }
    }

    @Override
    public void deleteUser(User user) {
        try {
            // Prepare statement to delete a user by ID
            PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id = ?");
            statement.setInt(1, user.getId());
            statement.executeUpdate(); // Execute the deletion
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions that occur
        }
    }

    @Override
    public User getUser(int id) {
        try {
            // Prepare statement to fetch a specific user by ID
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Retrieve user properties from ResultSet
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String lastInteractionTimeString = resultSet.getString("last_interaction_time");

                // Parse the last interaction time if it exists
                LocalDateTime lastInteractionTime = lastInteractionTimeString != null ? LocalDateTime.parse(lastInteractionTimeString, FORMATTER) : null;

                // Create and return a User object
                User user = new User(username, password, email, phone);
                user.setId(id); // Set user ID
                user.setLastInteractionTime(lastInteractionTime); // Set last interaction time
                return user; // Return the user object
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions that occur
        }
        return null; // Return null if the user is not found
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            // Execute query to fetch all users
            String query = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                // Retrieve each user's properties
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String lastInteractionTimeString = resultSet.getString("last_interaction_time");

                // Parse last interaction time if it exists
                LocalDateTime lastInteractionTime = lastInteractionTimeString != null ? LocalDateTime.parse(lastInteractionTimeString, FORMATTER) : null;

                // Create a User object and add it to the list
                User user = new User(username, password, email, phone);
                user.setId(id);
                user.setLastInteractionTime(lastInteractionTime);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions that occur
        }
        return users; // Return the list of users
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            // Prepare statement to fetch a user by username
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Retrieve user properties
                int id = resultSet.getInt("id");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String lastInteractionTimeString = resultSet.getString("last_interaction_time");

                // Parse last interaction time if it exists
                LocalDateTime lastInteractionTime = lastInteractionTimeString != null ? LocalDateTime.parse(lastInteractionTimeString, FORMATTER) : null;

                // Create and return a User object
                User user = new User(username, password, email, phone);
                user.setId(id);
                user.setLastInteractionTime(lastInteractionTime);
                return user; // Return the user object
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions that occur
        }
        return null; // Return null if the user is not found
    }

    @Override
    public boolean verifyUser(String username, String password) {
        User user = getUserByUsername(username); // Fetch user by username
        if (user == null) return false; // Return false if user not found
        // Verify the password using the hashing service
        return (user.getPassword().equals(hashService.getHash(password)));
    }

    @Override
    public int getUserID(String username, String password) {
        int userId = 0; // Default user ID
        try {
            // Prepare statement to fetch user ID by username and password
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id FROM users WHERE username = ? AND password = ?"
            );
            statement.setString(1, username);
            statement.setString(2, hashService.getHash(password)); // Hash the password before querying
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("id"); // Retrieve user ID
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log any exceptions that occur
        }
        return userId; // Return the user ID
    }

    @Override
    public LocalDateTime getLastInteractionTime(int userId) {
        LocalDateTime lastInteractionTime = null; // Default last interaction time
        try {
            // Prepare statement to fetch the last interaction time for a user
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT last_interaction_time FROM users WHERE id = ?"
            );
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String lastInteractionTimeString = resultSet.getString("last_interaction_time");
                lastInteractionTime = lastInteractionTimeString != null ? LocalDateTime.parse(lastInteractionTimeString, FORMATTER) : null; // Parse if exists
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions
        }
        return lastInteractionTime; // Return last interaction time
    }

    @Override
    public void setLastInteractionTime(int userId, LocalDateTime lastInteractionTime) {
        try {
            // Prepare statement to update last interaction time for a user
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET last_interaction_time = ? WHERE id = ?"
            );
            statement.setString(1, lastInteractionTime != null ? lastInteractionTime.format(FORMATTER) : null); // Format the timestamp
            statement.setInt(2, userId);

            statement.executeUpdate(); // Execute the update
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQL exceptions
        }
    }
}
