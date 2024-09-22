package vpm.gui_prototype.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.UserStuff.User;
import vpm.gui_prototype.models.UserStuff.UserSession;

/**
 * controller for SettingsView
 */
public class SettingsController {
    //UI elements
    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private Label messageLabel; // Label to display error or success messages
    //user and DAO
    private User user;
    private IUserDAO userDAO;

    /**
     * initialises the DAO, gets the user and the users details
     */
    @FXML
    public void initialize() {
        // Initialize the DAO
        userDAO = new SqliteUserDAO();

        // Get the current user ID from UserSession
        int userId = UserSession.getInstance().getUserId();

        if (userId != -1) { // Check if userId is valid
            // Fetch the user from the database using userId
            user = userDAO.getUser(userId);
        }

        // Populate fields with user data or placeholder text
        if (user != null) {
            populateField(usernameField, user.getUsername(), "Enter username");
            populateField(passwordField, user.getPassword(), "Enter password");
            populateField(emailField, user.getEmail(), "Enter email");
            populateField(phoneField, user.getPhone(), "Enter phone number");
        } else {
            showErrorMessage("User not found in the database.");
        }
    }

    /**
     * Populates the fields
     * @param field a textfield to populate
     * @param value a string of the value to populate the field with
     * @param placeholder string for a placeholder
     */
    // Helper method to populate fields or show placeholder text
    private void populateField(TextField field, String value, String placeholder) {
        if (value != null && !value.isEmpty()) {
            field.setText(value);
        } else {
            field.setPromptText(placeholder); // Set placeholder text
            field.setStyle("-fx-prompt-text-fill: derive(-fx-control-inner-background, -30%);"); // Gray placeholder text
        }
    }

    /**
     * checks if an email is valid
     * @param email the users email input string
     * @return
     */
    // Method to validate email format
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    /**
     * checks if the users phone number input is valid
     * @param phoneNumber the users phoneNumber input string
     * @return
     */
    // Method to validate phone number format (assuming 10-digit phone number)
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    /**
     * save the users details to the database
     */
    // Method to save updated user details
    @FXML
    private void saveUserDetails() {
        if (user == null) {
            showErrorMessage("Error: No user found to update.");
            return;
        }

        // Retrieve new values from fields, only validate non-empty fields
        String newUsername = usernameField.getText();
        String newPassword = passwordField.getText();
        String newEmail = emailField.getText();
        String newPhone = phoneField.getText();

        // Check and update username if provided
        if (!newUsername.isEmpty() && !newUsername.equals(user.getUsername())) {
            // Check if username already exists
            User existingUserByUsername = userDAO.getUserByUsername(newUsername);
            if (existingUserByUsername != null && existingUserByUsername.getId() != user.getId()) {
                showErrorMessage("Error: Username already exists. Please choose a different username.");
                return;
            }
            user.setUsername(newUsername);
        }

        // Check and update password if provided
        if (!newPassword.isEmpty() && !newPassword.equals(user.getPassword())) {
            // Check if password already exists
            for (User u : userDAO.getAllUsers()) {
                if (u.getPassword().equals(newPassword) && u.getId() != user.getId()) {
                    showErrorMessage("Error: Password already exists. Please choose a different password.");
                    return;
                }
            }
            user.setPassword(newPassword);
        }

        // Check and update email if provided
        if (!newEmail.isEmpty() && !newEmail.equals(user.getEmail())) {
            if (!isValidEmail(newEmail)) {
                showErrorMessage("Error: Invalid email format. Please include an '@' symbol and a domain.");
                return;
            }
            user.setEmail(newEmail);
        }

        // Check and update phone if provided
        if (!newPhone.isEmpty() && !newPhone.equals(user.getPhone())) {
            if (!isValidPhoneNumber(newPhone)) {
                showErrorMessage("Error: Invalid phone number. Please enter a 10-digit phone number.");
                return;
            }
            user.setPhone(newPhone);
        }

        // Save user details in the database
        try {
            userDAO.updateUser(user); // Update user in the database
            showSuccessMessage("User details updated successfully!");
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            showErrorMessage("Error: Could not update user details.");
            e.printStackTrace();
        }
    }

    /**
     * close the settings window
     */
    // Method to handle the "Back" button click
    @FXML
    private void goBack() {
        // Close the current stage (Settings window)
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    /**
     * display an error message for 3 seconds
     * @param message a string of the message to be displayed
     */
    // Show an error message in the label for 3 seconds
    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;"); // Set error message style
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> messageLabel.setText("")); // Clear the message after 3 seconds
        pause.play();
    }

    // Show a success message in the label for 3 seconds
    private void showSuccessMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 16px; -fx-font-weight: bold;"); // Set success message style
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> messageLabel.setText("")); // Clear the message after 3 seconds
        pause.play();
    }
}
