package vpm.gui_prototype.models.UserStuff;

/**
 * Singleton class to manage the user session, holding the user ID.
 */
public class UserSession {
    private static UserSession instance; // Singleton instance of UserSession
    private int userId;                  // The ID of the currently logged-in user

    /**
     * Private constructor to prevent instantiation from outside.
     */
    private UserSession() {
    }

    /**
     * Gets the singleton instance of UserSession.
     * @return The single instance of UserSession.
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession(); // Create instance if it doesn't exist
        }
        return instance; // Return the existing instance
    }

    /**
     * Sets the user ID for the current session.
     * @param userId The ID of the user to set.
     */
    public void setUserId(int userId) {
        this.userId = userId; // Update the user ID
    }

    /**
     * Gets the user ID for the current session.
     * @return The ID of the currently logged-in user.
     */
    public int getUserId() {
        return userId; // Return the current user ID
    }
}
