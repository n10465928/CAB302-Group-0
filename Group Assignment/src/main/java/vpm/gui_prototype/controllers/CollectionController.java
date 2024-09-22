package vpm.gui_prototype.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;

import java.io.IOException;
import java.io.InputStream;

/**
 * Controller class for CollectionView, handling all functionality of the buttons and generation of the tiles
 */

public class CollectionController {

    private PetManager petManager;
    int userId = UserSession.getInstance().getUserId();

    @FXML
    private GridPane petGrid;

    @FXML
    private Button createPetButton; // Button to create a new pet

    @FXML
    private Button settingsButton;

    @FXML
    private Label petNameLabel;

    @FXML
    private Button LogoutButton;

    @FXML
    private Label messageLabel; // Label to display error messages

    private Pet currentPet;

    /**
     * Constructs a CollectionController to handle the Collection View
     */
    public CollectionController() {
        petManager = new PetManager(new SqlitePetDAO());
    }

    /**
     * Initialises the pet tiles
     */
    @FXML
    public void initialize() {
        setupPetSlots();
    }

    // Set up pet slots in the grid
    private void setupPetSlots() {
        petGrid.getChildren().clear(); // Clear existing tiles

        int numSlots = 8; // Number of tiles to display

        for (int i = 0; i < numSlots; i++) {
            VBox tile;
            if (i < petManager.getAllUsersPets(userId).size()) {
                Pet pet = petManager.getAllUsersPets(userId).get(i);
                tile = createPetTile(pet); // Create filled tile
            } else {
                tile = createEmptyPetTile(); // Create empty tile with default image
            }
            petGrid.add(tile, i % 4, i / 4); // Arrange tiles in 2 rows, 4 columns
        }
    }

    // Create a filled pet slot tile with pet type, image, and name
    private VBox createPetTile(Pet pet) {
        VBox tile = new VBox();
        tile.setStyle("-fx-border-color: #00796B; -fx-border-width: 2; -fx-background-color: #B2DFDB; -fx-alignment: center; -fx-pref-width: 200; -fx-pref-height: 200; -fx-border-radius: 10; -fx-background-radius: 10;");
        tile.setPrefSize(200, 200); // Fixed size to maintain uniformity
        tile.setMaxSize(200, 200);  // Prevent tiles from resizing
        tile.setMinSize(200, 200);  // Set minimum size to prevent shrinking

        tile.setSpacing(5);

        // Text for pet type
        Text petTypeText = new Text(pet.GetType());
        petTypeText.setStyle("-fx-font-size: 18px; -fx-fill: #004D40; -fx-font-weight: bold;");

        // Create ImageView for the pet image
        ImageView petImageView = new ImageView();
        petImageView.setFitWidth(100);
        petImageView.setFitHeight(100);
        petImageView.setPreserveRatio(true);

        // Set the image based on the pet type
        String imagePath = getPetImagePath(pet.GetType());
        if (imagePath != null) {
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                petImageView.setImage(new Image(imageStream));
            } else {
                // Log an error if the image is not found
                System.err.println("Error: Pet image not found for path: " + imagePath);
            }
        } else {
            // Set a default image if the pet type is not recognized
            petImageView.setImage(new Image(getClass().getResourceAsStream("/assets/default.png")));
        }

        // Text for pet name
        Text petNameText = new Text(pet.GetName());
        petNameText.setStyle("-fx-font-size: 16px; -fx-fill: #00796B; -fx-font-weight: bold;");

        // Add the pet type, image, and name to the tile in this order
        tile.getChildren().addAll(petTypeText, petImageView, petNameText);

        // Set the tile to be clickable and open the pet management screen
        tile.setOnMouseClicked(e -> openPetInteraction(pet));

        return tile;
    }

    // Get the image path for the corresponding pet type
    private String getPetImagePath(String petType) {
        switch (petType.toLowerCase()) {
            case "cat":
                return "/assets/cat.png";
            case "dog":
                return "/assets/dog.png";
            case "fish":
                return "/assets/fish.png";
            case "bird":
                return "/assets/bird.png";
            default:
                return null; // No specific image path for this type
        }
    }

    // Create an empty pet slot tile with the default image
    private VBox createEmptyPetTile() {
        VBox tile = new VBox();
        tile.setStyle("-fx-border-color: #B0BEC5; -fx-border-width: 2; -fx-background-color: #ECEFF1; -fx-alignment: center; -fx-pref-width: 200; -fx-pref-height: 200; -fx-border-radius: 10; -fx-background-radius: 10;");
        tile.setPrefSize(200, 200); // Fixed size to maintain uniformity
        tile.setMaxSize(200, 200);  // Prevent tiles from resizing
        tile.setMinSize(200, 200);  // Set minimum size to prevent shrinking

        tile.setSpacing(5);

        // Create ImageView for the default image
        ImageView defaultImageView = new ImageView();
        defaultImageView.setFitWidth(100);
        defaultImageView.setFitHeight(100);
        defaultImageView.setPreserveRatio(true);

        // Load the default image from resources
        InputStream defaultImageStream = getClass().getResourceAsStream("/assets/default.png");
        if (defaultImageStream == null) {
            System.err.println("Error: Default image not found.");
            return tile; // Return empty tile if image is not found
        }
        defaultImageView.setImage(new Image(defaultImageStream));

        // Text for empty slot
        Text emptyText = new Text("Empty Slot");
        emptyText.setStyle("-fx-font-size: 16px; -fx-fill: #B0BEC5;");

        // Add the default image and text to the tile
        tile.getChildren().addAll(defaultImageView, emptyText);

        // Set the tile to be clickable and show an error message if clicked
        tile.setOnMouseClicked(e -> showErrorMessage("Error: This tile does not contain a pet!"));

        return tile;
    }

    // Show an error message in the label for 3 seconds
    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;"); // Set error message style
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> messageLabel.setText("")); // Clear the message after 3 seconds
        pause.play();
    }

    // Open the pet management screen for a specific pet
    private void openPetInteraction(Pet pet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/PetInteractionView.fxml"));
            Scene scene = new Scene(loader.load());

            PetInteractionController petInteractionController = loader.getController();
            petInteractionController.setPet(pet); // Pass the pet to the management screen

            Stage stage = (Stage) petGrid.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Manage Pet: " + pet.GetName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onCreatePetPress() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/PetCreationView.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) createPetButton.getScene().getWindow(); // Get the current stage
            stage.setScene(scene);
            stage.setTitle("Create New Pet");
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error: Could not open Pet Creation view.");
        }
    }

    // Method to open Settings View
    @FXML
    private void openSettingsView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/SettingsView.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Settings");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Handle Exit button action
    @FXML
    private void onLogoutPress() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/LoginView.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) LogoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Logout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
