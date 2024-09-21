package vpm.gui_prototype.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class LaunchController {

    @FXML
    public void initialize() {
        // Create a pause transition that lasts for 2.5 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));

        // After the pause, navigate to the login screen
        pause.setOnFinished(event -> showLoginScreen());

        // Start the transition
        pause.play();
    }

    // Navigate to the login screen
    private void showLoginScreen() {
        try {
            // Load the login view FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/LoginView.fxml"));
            Parent loginView = loader.load();

            // Get the current stage and set the new scene
            Stage stage = (Stage) Stage.getWindows().filtered(window -> window.isShowing()).get(0);
            stage.setScene(new Scene(loginView));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
