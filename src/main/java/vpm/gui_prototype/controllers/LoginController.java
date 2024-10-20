package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.services.LoginService;

import java.io.IOException;

/**
 * Controller for the LoginView.
 * Manages user inputs and navigation on the login page.
 */
public class LoginController {

    // UI elements
    @FXML
    private Button LoginButton; // Button to log in

    @FXML
    private Button ExitButton; // Button to exit the application

    @FXML
    private TextField usernameField; // Text field for username input

    @FXML
    private PasswordField passwordField; // Password field for password input

    @FXML
    private Label errorMessageLabel; // Label to display error messages

    // Database variable for user data access
    private IUserDAO userDAO;

    /**
     * Constructor for LoginController. Initializes the user DAO.
     */
    public LoginController() {
        // Initialize the user DAO to interact with the user database
        userDAO = new SqliteUserDAO();
    }

    /**
     * Validates that the input fields are not empty.
     *
     * @param username the user's username input string
     * @param password the user's password input string
     * @return a message indicating if inputs are valid or an error message
     */
    public String validInputs(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            return "Good"; // Inputs are valid
        }
        return "Please input all credentials"; // Error message
    }

    /**
     * Verifies that the username and password match an existing user in the database.
     *
     * @param username the user's username input string
     * @param password the user's password input string
     * @return true if the user exists, otherwise false
     */
    public boolean verifyUser(String username, String password) {
        return userDAO.verifyUser(username, password); // Verify credentials using DAO
    }

    /**
     * Handles the login button press event.
     * Validates user inputs, verifies user credentials, and navigates to the collection view if successful.
     */
    @FXML
    void onLoginPress() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate inputs
        String validationMessage = validInputs(username, password);
        if (!validationMessage.equals("Good")) {
            errorMessageLabel.setText(validationMessage); // Display validation error
            return;
        }

        // Verify user credentials
        if (!verifyUser(username, password)) {
            errorMessageLabel.setText("Username or password is incorrect"); // Display login error
            return;
        }

        // Successful login - set user session and navigate to CollectionView
        try {
            LoginService loginService = new LoginService();
            loginService.login(userDAO.getUserID(username, password)); // Set user session
            navigateToCollectionView(); // Navigate to the collection view
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for any exceptions
        }
    }

    /**
     * Handles the registration button press event.
     * Navigates to the registration screen.
     */
    @FXML
    void onRegisterPress() {
        try {
            navigateToRegisterView(); // Navigate to the register view
        } catch (Exception e) {
            e.printStackTrace(); // Print stack trace for any exceptions
        }
    }

    /**
     * Navigates to the collection view after a successful login.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    private void navigateToCollectionView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
        Stage stage = (Stage) LoginButton.getScene().getWindow(); // Get the current stage
        stage.setScene(new Scene(loader.load())); // Load the new scene
        stage.setTitle("Pet Collection"); // Set the window title
    }

    /**
     * Navigates to the registration view for new user registration.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    private void navigateToRegisterView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/RegisterView.fxml"));
        Stage stage = (Stage) LoginButton.getScene().getWindow(); // Get the current stage
        stage.setScene(new Scene(loader.load())); // Load the new scene
        stage.setTitle("Register"); // Set the window title
    }

    /**
     * Handles the exit button press event.
     * Closes the application.
     */
    @FXML
    void onExitPress() {
        Stage stage = (Stage) ExitButton.getScene().getWindow(); // Get the current stage
        stage.close(); // Close the application
    }
}
