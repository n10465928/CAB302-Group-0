package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.UserStuff.User;

import java.io.IOException;

public class RegisterController {

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;

    private IUserDAO userDAO;

    public RegisterController() {
        userDAO = new SqliteUserDAO();
    }

    @FXML
    void onRegisterPress() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if textboxes are filled
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // ToDo: Make UI effects to alert users that they need to fill in all the boxes
            System.out.println("Lack of credentials");
            return;
        }

        // Check if the user existed
        if (userDAO.getUserByUsername(username) != null) {
            // ToDo: Make UI effects to alert users that the username existed
            System.out.println(username + " is already existed");
            return;
        }
        // Check if the password and confirm password is identical
        if (!password.equals(confirmPassword)) {
            // ToDo: Make UI effects to alert users that confirm password is not identical with provided password
            System.out.println("Password does not match");
            return;
        }

        // Todo: Email and Phone can be added later in the profile stuff if we have one
        User user = new User(username, password, null, null);
        userDAO.addUser(user);
        // Todo: Maybe shows something that indicates successfully registered

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/LoginView.fxml"));
            Parent LoginView = loader.load();

            // Get the current stage and load new scene
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(LoginView));

            //change title
            stage.setTitle("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
