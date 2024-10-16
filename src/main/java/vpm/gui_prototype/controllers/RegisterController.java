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
 * Controller for the Registration View.
 */
public class RegisterController {

    // UI elements
    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    @FXML
    private Button exitButton;

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

    // DAO and service for user data management
    private IUserDAO userDAO;
    private PasswordHashingService hashService;

    /**
     * Constructor for RegisterController.
     * Initializes the User DAO and the Password Hashing Service.
     */
    public RegisterController() {
        userDAO = new SqliteUserDAO();
        hashService = new PasswordHashingService();
    }

    /**
     * Checks if all input fields are filled.
     *
     * @param username The username input string.
     * @param password The password input string.
     * @param confirmPassword The confirm password input string.
     * @return true if all fields are filled, otherwise false.
     */
    public boolean fieldsFilled(String username, String password, String confirmPassword) {
        return !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty();
    }

    /**
     * Verifies that the password and confirm password inputs match.
     *
     * @param password User's password input string.
     * @param confirmPassword User's confirm password string.
     * @return true if the passwords match, otherwise false.
     */
    public boolean matchingPassword(String password, String confirmPassword) {
        return Objects.equals(password, confirmPassword);
    }

    /**
     * Checks if inputs are ready to be sent to the database.
     *
     * @param username The username input string.
     * @param password The password input string.
     * @param confirmPassword The confirm password input string.
     * @return A message indicating the result of the checks.
     */
    public String inputsReady(String username, String password, String confirmPassword) {
        if (!fieldsFilled(username, password, confirmPassword)) {
            return "Please ensure you fill in all fields";
        }
        if (!matchingPassword(password, confirmPassword)) {
            return "Passwords do not match";
        }
        return "Good";
    }

    /**
     * Checks if the user already exists in the database.
     *
     * @param username User's username input string.
     * @return true if the user exists, otherwise false.
     */
    public boolean checkExistingUser(String username) {
        return userDAO.getUserByUsername(username) != null;
    }

    /**
     * Evaluates if the username meets specific conditions:
     * - Length between 6 and 20 characters
     * - No special characters
     *
     * @param username The username input string.
     * @return A message indicating whether the username is valid.
     */
    public String evaluateUsername(String username) {
        if (username.length() < 6 || username.length() > 20) {
            return "Username must be from 6 to 20 characters long";
        }
        for (char c : username.toCharArray()) {
            if (!Character.isDigit(c) && !Character.isAlphabetic(c)) {
                return "Username must not contain special characters";
            }
        }
        return "Good";
    }

    /**
     * Evaluates if the password is strong enough by checking:
     * - Contains an uppercase letter
     * - Longer than 8 characters
     * - Contains a special character
     *
     * @param password The password input string.
     * @return A message indicating whether the password is valid.
     */
    public String evaluatePassword(String password) {
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }

        boolean hasUppercase = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            hasUppercase |= Character.isUpperCase(c);
            hasSpecial |= (!Character.isAlphabetic(c) && !Character.isDigit(c));
        }

        if (hasUppercase && hasSpecial) {
            return "Good";
        }
        if (!hasUppercase) {
            return "Password must have an uppercase letter";
        }
        return "Password must have a special character";
    }

    /**
     * Navigates back to the login screen.
     */
    @FXML
    void onBackPress() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/LoginView.fxml"));
            Parent loginView = loader.load();

            // Get the current stage and load the new scene
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(loginView));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Attempts to register the user. Displays error messages if registration fails,
     * otherwise navigates to the login screen.
     */
    @FXML
    void onRegisterPress() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if inputs are valid
        String inputCheckResult = inputsReady(username, password, confirmPassword);
        if (!inputCheckResult.equals("Good")) {
            errorMessageLabel.setText(inputCheckResult);
            return;
        }

        // Check if the user exists
        if (checkExistingUser(username)) {
            errorMessageLabel.setText("The username " + username + " is already taken");
            return;
        }

        // Evaluate the username
        String usernameEvaluationResult = evaluateUsername(username);
        if (!usernameEvaluationResult.equals("Good")) {
            errorMessageLabel.setText(usernameEvaluationResult);
            return;
        }

        // Evaluate the password
        String passwordEvaluationResult = evaluatePassword(password);
        if (!passwordEvaluationResult.equals("Good")) {
            errorMessageLabel.setText(passwordEvaluationResult);
            return;
        }

        // Hash the password and create the User object
        String storedPassword = hashService.getHash(password);
        User user = new User(username, storedPassword, null, null);
        userDAO.addUser(user);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/LoginView.fxml"));
            Parent loginView = loader.load();

            // Get the current stage and load the new scene
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(loginView));
            stage.setTitle("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the application.
     */
    @FXML
    void onExitPress() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Shows the password in a visible field.
     */
    @FXML
    void showPassword() {
        passwordFieldVisible.setText(passwordField.getText());
        passwordFieldVisible.setVisible(true);
        passwordField.setVisible(false);
    }

    /**
     * Hides the password in the input field.
     */
    @FXML
    void hidePassword() {
        passwordField.setText(passwordFieldVisible.getText());
        passwordField.setVisible(true);
        passwordFieldVisible.setVisible(false);
    }

    /**
     * Shows the confirm password in a visible field.
     */
    @FXML
    void showConfirmPassword() {
        confirmPasswordFieldVisible.setText(confirmPasswordField.getText());
        confirmPasswordFieldVisible.setVisible(true);
        confirmPasswordField.setVisible(false);
    }

    /**
     * Hides the confirm password in the input field.
     */
    @FXML
    void hideConfirmPassword() {
        confirmPasswordField.setText(confirmPasswordFieldVisible.getText());
        confirmPasswordField.setVisible(true);
        confirmPasswordFieldVisible.setVisible(false);
    }
}
