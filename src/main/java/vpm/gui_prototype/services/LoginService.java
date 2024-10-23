package vpm.gui_prototype.services;

import vpm.gui_prototype.models.UserStuff.UserSession;

/**
 * Service class to handle user login operations.
 */
public class LoginService {

    /**
     * Logs in a user by setting their user ID in the current session.
     * @param userId The ID of the user to log in.
     */
    public void login(int userId) {
        UserSession.getInstance().setUserId(userId); // Set the user ID in the session
    }
}
