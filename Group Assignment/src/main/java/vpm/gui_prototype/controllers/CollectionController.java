package vpm.gui_prototype.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.services.PetService;

import java.io.IOException;

public class CollectionController {

    @FXML
    private GridPane petGrid;

    @FXML
    private Button createPetButton;

    @FXML
    private Label messageLabel; // Label to display temporary messages

    private PetService petService = new PetService(); // Initial empty service

    @FXML
    public void initialize() {
        setupPetSlots();
    }

    public void setPetService(PetService petService) {
        this.petService = petService;
        setupPetSlots(); // Refresh pet slots
    }

    // Set up pet slots in the grid
    private void setupPetSlots() {
        petGrid.getChildren().clear(); // Clear existing slots

        int numSlots = 8; // We have 8 slots to display

        for (int i = 0; i < numSlots; i++) {
            VBox tile;
            if (i < petService.getAllPets().size()) {
                Pet pet = petService.getAllPets().get(i);
                tile = createPetTile(pet);
            } else {
                tile = createEmptyPetTile();
            }
            petGrid.add(tile, i % 4, i / 4); // Add tiles to the grid in 2 rows, 4 columns
        }
    }

    // Create a filled pet slot tile
    private VBox createPetTile(Pet pet) {
        VBox tile = new VBox();
        tile.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: lightblue; -fx-alignment: center; -fx-pref-width: 200; -fx-pref-height: 200;");
        tile.setSpacing(10);

        Text petInfo = new Text(pet.GetName() + "\n" + pet.GetType());
        tile.getChildren().add(petInfo);

        // Set the tile to be clickable and open the pet management screen
        tile.setOnMouseClicked(e -> openPetManagement(pet));

        return tile;
    }

    // Create an empty pet slot tile
    private VBox createEmptyPetTile() {
        VBox tile = new VBox();
        tile.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-background-color: lightgray; -fx-alignment: center; -fx-pref-width: 200; -fx-pref-height: 200;");
        tile.setSpacing(10);

        Text emptyText = new Text("Empty Slot");
        tile.getChildren().add(emptyText);

        // Set the tile to be clickable and show a message if clicked
        tile.setOnMouseClicked(e -> showTemporaryMessage("This tile does not contain a pet!"));

        return tile;
    }

    // Show a temporary message for 3 seconds
    private void showTemporaryMessage(String message) {
        messageLabel.setText(message);
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> messageLabel.setText("")); // Clear the message after 3 seconds
        pause.play();
    }

    // Open the pet management screen for a specific pet
    private void openPetManagement(Pet pet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/PetManagementView.fxml"));
            Scene scene = new Scene(loader.load());

            PetManagementController petManagementController = loader.getController();
            petManagementController.setPet(pet); // Pass the pet to the management screen

            Stage stage = (Stage) petGrid.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Manage Pet: " + pet.GetName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle "Create Pet" button click to open the Pet Creation screen
    @FXML
    private void onCreatePet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/PetCreationView.fxml"));
            Scene scene = new Scene(loader.load());

            PetCreationController petCreationController = loader.getController();
            petCreationController.setPetService(petService);

            Stage stage = (Stage) createPetButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Create New Pet");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
