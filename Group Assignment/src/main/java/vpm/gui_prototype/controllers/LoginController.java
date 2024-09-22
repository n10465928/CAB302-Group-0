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
 * A controller for the LoginView, handles user inputs and navigation in this page
 */
public class LoginController {
    //UI elements
    @FXML
    private Button LoginButton;


    @FXML
    private Button ExitButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorMessageLabel;
    //database variable
    private IUserDAO userDAO;

    /**
     * Constructor for LoginController, initialises a user DAO
     */
    public LoginController() {
        // Initialize the user DAO to interact with the user database.
        userDAO = new SqliteUserDAO();
    }

    // Validates that the input fields have values.

    /**
     * Method to check  if the users inputs are valid before sending to database
     * @param username the users username input string
     * @param password the users passsword input string
     * @return true if inputs are valid, otherwise false
     */
    public String validInputs(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            return "Good";
        }
        return "Please input all credentials";
    }

    // Verifies that the user exists with the given username and password.

    /**
     * verifies that the username and password inputs match an existing user in te database
     * @param username users username input string
     * @param password users password input string
     * @return
     */
    public boolean verifyUser(String username, String password) {
        return userDAO.verifyUser(username, password);
    }

    // Handles login button press.

    /**
     * handles login press, checks if inputs are valid then checks if they match a user in the database
     * giving an error message or logging in appropriately
     */
    @FXML
    void onLoginPress() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Validate inputs
        if (!validInputs(username, password).equals("Good")) {
            errorMessageLabel.setText(validInputs(username, password));
            return;
        }

        // Verify user credentials
        if (!verifyUser(username, password)) {
            errorMessageLabel.setText("Username or password is incorrect");
            return;
        }

        // Successful login - set user session and navigate to CollectionView.
        try {
            LoginService loginService = new LoginService();
            loginService.login(userDAO.getUserID(username, password));
            navigateToCollectionView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handles registration button press.

    /**
     * Takes the user to the register screen
     */
    @FXML
    void onRegisterPress() {
        try {
            navigateToRegisterView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Navigate to CollectionView after successful login.

    /**
     * Navigates to the collection view if login is successful
     * @throws IOException
     */
    private void navigateToCollectionView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
        Stage stage = (Stage) LoginButton.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Pet Collection");
    }

    // Navigate to RegisterView for new user registration.

    /**
     * navigates to register view
     * @throws IOException
     */
    private void navigateToRegisterView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/RegisterView.fxml"));
        Stage stage = (Stage) LoginButton.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Register");
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
}
