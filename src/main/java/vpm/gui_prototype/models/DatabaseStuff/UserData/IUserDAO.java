package vpm.gui_prototype.models.DatabaseStuff.UserData;

import vpm.gui_prototype.models.UserStuff.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This interface is to work with User Credentials
 */
public interface IUserDAO {
    /**
     * Add a new user to the database
     * @param user The user to add
     */
    public void addUser(User user);

    /**
     * Updates an existing user in the database
     * @param user The user to update
     */
    public void updateUser(User user);

    /**
     * Delete a user from the database
     * @param user The user to delete
     */
    public void deleteUser(User user);

    /**
     * Retrieves a user from the database
     * @param id The ID of the user to retrieve
     * @return The user with the given ID, or null if not found
     */
    public User getUser(int id);

    /**
     * Retrieves a user from the database
     * @param username the username
     * @return The user with the given username
     */
    public User getUserByUsername(String username);

    /**
     * Retrieves all users from the database
     * @return A list of all users in the database
     */
    public List<User> getAllUsers();

    /**
     * Verifies the login credentials
     * @param username The username
     * @param password The password
     * @return True if the password matches the username, else false.
     */
    public boolean verifyUser(String username, String password);

    /**
     * Retrieves a user's ID from the database based on their username and password
     * @param username The username
     * @param password The password
     * @return The user's ID, or 0 if not found
     */
    public int getUserID(String username, String password);

    /**
     * Retrieves the last interaction time for a user from the database
     * @param userId The ID of the user
     * @return The last interaction time as a LocalDateTime object, or null if not set
     */
    public LocalDateTime getLastInteractionTime(int userId);

    /**
     * Sets the last interaction time for a user in the database
     * @param userId The ID of the user
     * @param lastInteractionTime The LocalDateTime object representing the last interaction time
     */
    public void setLastInteractionTime(int userId, LocalDateTime lastInteractionTime);
}
