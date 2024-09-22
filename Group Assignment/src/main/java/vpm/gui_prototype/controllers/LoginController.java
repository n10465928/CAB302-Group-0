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

public class LoginController {

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

    private IUserDAO userDAO;

    public LoginController() {
        // Initialize the user DAO to interact with the user database.
        userDAO = new SqliteUserDAO();
    }

    // Validates that the input fields have values.
    public String validInputs(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty()) {
            return "Good";
        }
        return "Please input all credentials";
    }

    // Verifies that the user exists with the given username and password.
    public boolean verifyUser(String username, String password) {
        return userDAO.verifyUser(username, password);
    }

    // Handles login button press.
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
    @FXML
    void onRegisterPress() {
        try {
            navigateToRegisterView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Navigate to CollectionView after successful login.
    private void navigateToCollectionView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
        Stage stage = (Stage) LoginButton.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Pet Collection");
    }

    // Navigate to RegisterView for new user registration.
    private void navigateToRegisterView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/RegisterView.fxml"));
        Stage stage = (Stage) LoginButton.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
        stage.setTitle("Register");
    }

    @FXML
    void onExitPress() {
        // Close application
        Stage stage = (Stage) ExitButton.getScene().getWindow();
        stage.close();
    }
}
