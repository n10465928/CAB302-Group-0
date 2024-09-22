package vpm.gui_prototype;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vpm.gui_prototype.services.GlobalPetStatService;

public class Main extends Application {

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

        // Start the GlobalPetStatService
        GlobalPetStatService petStatService = GlobalPetStatService.getInstance();
        petStatService.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
