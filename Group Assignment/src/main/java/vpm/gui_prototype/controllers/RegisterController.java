package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.UserStuff.User;

import java.io.IOException;
import java.util.Objects;

public class RegisterController {

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    @FXML Button ExitButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField passwordFieldVisible;

    @FXML
    private TextField confirmPasswordFieldVisible;

    @FXML
    private Label errorMessageLabel;

    private IUserDAO userDAO;

    public RegisterController() {
        userDAO = new SqliteUserDAO();
    }

    //helper functions verifying data is ready to be sent to database
    //check if all fields are filled, true if they are otherwise false
    public boolean fieldsFilled(String username, String password, String confirmPassword){
        return !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty();
    }
    //check password and confirm password match, true if they do otherwise false
    public boolean matchingPassword(String password, String confirmPassword){
        return Objects.equals(password, confirmPassword);
    }
    //checks that data  is ready to interact with database, returns true if it is, otherwise returns false and outputs errors
    public boolean inputsReady(String username, String password, String confirmPassword){
        if(!fieldsFilled(username, password, confirmPassword)){
            errorMessageLabel.setText("Please ensure you fill in all fields");
            return false;
        }
        else if(!matchingPassword(password, confirmPassword)){
            errorMessageLabel.setText("Passwords do not match");
            return false;
        }
        else{return true;}
    }

    // return true if the user exists, false otherwise
    public boolean checkExistingUser(String username) {
        return userDAO.getUserByUsername(username) != null;
    }

    public String evaluateUsername(String username) {
        /*
            Some conditions:
                - In range of [6, 20] characters
                - No special character
         */
        if (username.length() < 6 || username.length() > 20) {
            return "Username must be from 6 to 20 characters long";
        }
        for (int i = 0; i < username.length(); i ++) {
            char c = username.charAt(i);
            if (!Character.isDigit(c) && !Character.isAlphabetic(c)) {
                return "Username must not contain special characters";
            }
        }
        return "Good";
    }
    public String evaluatePassword(String password) {
        /*
            Some conditions:
                - Has an uppercase letter
                - Longer than 8 characters
                - Has a special character
         */
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        boolean hasUppercase = false;
        boolean hasSpecial = false;
        for (int i = 0; i < password.length(); i ++) {
            char c = password.charAt(i);
            hasUppercase |= Character.isUpperCase(c);
            hasSpecial |= (!Character.isAlphabetic(c) & !Character.isDigit(c));
        }

        if (hasUppercase & hasSpecial) {
            return "Good";
        } if (!hasUppercase) {
            return "Password must have an uppercase letter";
        }
        return "Password must have a special character";
    }

    @FXML
        // Go back to login screen
    void onBackPress() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/LoginView.fxml"));
            Parent LoginView = loader.load();

            // Get the current stage and load new scene
            Stage stage = (Stage) backButton.getScene().getWindow();  // Use backButton instead of registerButton
            stage.setScene(new Scene(LoginView));

            // Change title
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onRegisterPress() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check inputs are valid
        if (!inputsReady(username, password, confirmPassword)){
            return;
        }

        // Check if the user existed
        if (checkExistingUser(username)) {
            errorMessageLabel.setText("The username " + username + " is already taken");
            return;
        }

        // Check if the username satisfies the conditions
        // return good if passed all conditions
        // return failed condition otherwise
        if (!evaluateUsername(username).equals("Good")) {
            errorMessageLabel.setText(evaluateUsername(username));
            return;
        }

        // Check if the password is strong enough
        // return good if passed all conditions
        // return failed condition otherwise
        if (!evaluatePassword(password).equals("Good")) {
            errorMessageLabel.setText(evaluatePassword(password));
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

    @FXML
    void onExitPress() {
        // Close application
        Stage stage = (Stage) ExitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void showPassword() {
        passwordFieldVisible.setText(passwordField.getText());
        passwordFieldVisible.setVisible(true);
        passwordField.setVisible(false);
    }

    @FXML
    void hidePassword() {
        passwordField.setText(passwordFieldVisible.getText());
        passwordField.setVisible(true);
        passwordFieldVisible.setVisible(false);
    }

    @FXML
    void showConfirmPassword() {
        confirmPasswordFieldVisible.setText(confirmPasswordField.getText());
        confirmPasswordFieldVisible.setVisible(true);
        confirmPasswordField.setVisible(false);
    }

    @FXML
    void hideConfirmPassword() {
        confirmPasswordField.setText(confirmPasswordFieldVisible.getText());
        confirmPasswordField.setVisible(true);
        confirmPasswordFieldVisible.setVisible(false);
    }
}