package vpm.gui_prototype.services;

import vpm.gui_prototype.models.UserStuff.UserSession;

/**
 *Service class to handle user login operations.
 */
public class LoginService {


    public void login(int userId) {
        UserSession.getInstance().setUserId(userId); // Set the user ID in the session
    }
}
