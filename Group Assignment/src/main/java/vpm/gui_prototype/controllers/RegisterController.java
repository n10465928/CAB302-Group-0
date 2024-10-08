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
import vpm.gui_prototype.services.PasswordHashingService;

import java.io.IOException;
import java.util.Objects;

/**
 * Controller for RegisterView
 */
public class RegisterController {
    //UI elements
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
    //DAO
    private IUserDAO userDAO;
    // Service
    private PasswordHashingService hashService;

    /**
     * Constructor for RegisterController
     */
    public RegisterController() {
        userDAO = new SqliteUserDAO();
        hashService = new PasswordHashingService();
    }

    //helper functions verifying data is ready to be sent to database
    //check if all fields are filled, true if they are otherwise false

    /**
     * Checks if all of the input fiels have a user input
     * @param username the users username input string
     * @param password the users password input string
     * @param confirmPassword the users confirm passwrod input string
     * @return true if fields are all filled, otherwise false
     */
    public boolean fieldsFilled(String username, String password, String confirmPassword){
        return !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty();
    }
    //check password and confirm password match, true if they do otherwise false

    /**
     * Verifies that the password and confirm password inputs match
     * @param password users password input string
     * @param confirmPassword users confirm password string
     * @return true if the passwords match, otherwise false
     */
    public boolean matchingPassword(String password, String confirmPassword){
        return Objects.equals(password, confirmPassword);
    }
    //checks that data  is ready to interact with database, returns true if it is, otherwise returns false and outputs errors

    /**
     * Verifies inputs arae ready to be sent to database, i.e; all fields filled and passwords match, using helper functions
     * @param username the users username input string
     * @param password the users password input string
     * @param confirmPassword the users confirm password input string
     * @return
     */
    public String inputsReady(String username, String password, String confirmPassword){
        if (!fieldsFilled(username, password, confirmPassword)) {
            return "Please ensure you fill in all fields";
        }
        if (!matchingPassword(password, confirmPassword)) {
            return "Passwords do not match";
        }
        return "Good";
    }

    // return true if the user exists, false otherwise

    /**
     * checks if the user exists in the database
     * @param username users username input string
     * @return true if it exists, otherwise false
     */
    public boolean checkExistingUser(String username) {
        return userDAO.getUserByUsername(username) != null;
    }

    /**
     * Evaluates if the users username meets the conditions:
     *      -range of 6 to 20 characters
     *      -no special characters
     * @param username the users username input string
     * @return true if it meets the connections, otherwise false
     */
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

    /**
     * Evaluates if the password is strong enough by meeting the following conditions:
     *      -has an uppercase letter
     *      -greater than 8 characters
     *      -contains a special character
     * @param password the users password input string
     * @return true if valid, otherwise false
     */
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

    /**
     * returns to login screen
     */
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

    /**
     * attemps to register user, shows error messages if fails, otherwise returns to login screen
     */
    @FXML
    void onRegisterPress() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check inputs are valid
        if (!inputsReady(username, password, confirmPassword).equals("Good")){
            errorMessageLabel.setText(inputsReady(username, password, confirmPassword));
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
        String storedPassword = hashService.getHash(password);
        User user = new User(username, storedPassword, null, null);
        userDAO.addUser(user);

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

    /**
     * closes the application
     */
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