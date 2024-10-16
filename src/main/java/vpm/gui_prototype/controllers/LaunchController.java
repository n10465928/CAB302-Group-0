package vpm.gui_prototype.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

/**
 * Controller for the LaunchView.
 * Displays the launch screen for 2.5 seconds before navigating to the login screen.
 */
public class LaunchController {

    /**
     * Initializes the launch view and sets up the transition to the login screen.
     */
    @FXML
    public void initialize() {
        // Create a pause transition that lasts for 1.5 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));

        // Set the action to perform after the pause
        pause.setOnFinished(event -> showLoginScreen());

        // Start the pause transition
        pause.play();
    }

    /**
     * Navigates to the login screen by loading the LoginView FXML.
     * This method is called after the pause in the launch view.
     */
    private void showLoginScreen() {
        try {
            // Load the login view FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/LoginView.fxml"));
            Parent loginView = loader.load();

            // Get the current stage and set the new scene to the login view
            Stage stage = (Stage) Stage.getWindows().filtered(window -> window.isShowing()).get(0);
            stage.setScene(new Scene(loginView));
            stage.setTitle("Login"); // Set the window title to "Login"
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace for any IOException
        }
    }
}
