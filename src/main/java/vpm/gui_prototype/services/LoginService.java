package vpm.gui_prototype.services;

import vpm.gui_prototype.models.UserStuff.UserSession;

public class LoginService {
    public void login(int userId) {
        UserSession.getInstance().setUserId(userId);
    }
}
