package vpm.gui_prototype.models.DatabaseStuff.UserData;

import vpm.gui_prototype.models.UserStuff.User;

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
     * Retrives a user from the database
     * @param username the username
     * @return the user with the given ID
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
     * Retrives a user from the database
     * @param username the username
     * @param password the password
     * @return the user with the given ID
     */
    public int getUserID(String username, String password);
}
