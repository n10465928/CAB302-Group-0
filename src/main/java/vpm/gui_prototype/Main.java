package vpm.gui_prototype;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application class for the Virtual Pet Simulator.
 */
public class Main extends Application {

    /**
     * The entry point for the JavaFX application.
     *
     * @param primaryStage The primary stage for the application.
     * @throws Exception if loading the FXML file fails.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the initial scene from the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/LaunchView.fxml"));
        Scene scene = new Scene(loader.load());

        // Set the scene and title for the primary stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Virtual Pet Simulator");

        // Show the primary stage
        primaryStage.show();
    }

    /**
     * The main method to launch the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
