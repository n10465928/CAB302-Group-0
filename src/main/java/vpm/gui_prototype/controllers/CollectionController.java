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
import java.util.List;

/**
 * The CollectionController class manages the collection view of pets in the application.
 * It handles the display of pets, user interactions with pet tiles, and scene transitions.
 */
public class CollectionController {

    private static final int NUM_SLOTS = 8;  // Number of slots available for pets
    private static final String DEFAULT_IMAGE_PATH = "/assets/default.png";  // Default image path for pets
    private static final String PET_IMAGE_PATH_FORMAT = "/assets/%s.png";  // Format for pet images based on type

    private final PetManager petManager;  // Manages pet data
    private final int userId;  // Current user's ID

    @FXML
    private GridPane petGrid;  // GridPane to display pet tiles
    @FXML
    private Button createPetButton;  // Button to create a new pet
    @FXML
    private Button settingsButton;  // Button to open settings
    @FXML
    private Label messageLabel;  // Label for displaying messages to the user
    @FXML
    private Button logoutButton;  // Button to log out

    /**
     * Constructs a CollectionController and initializes the pet manager and user ID.
     */
    public CollectionController() {
        this.petManager = new PetManager(new SqlitePetDAO());
        this.userId = UserSession.getInstance().getUserId();
    }

    /**
     * Initializes the controller after its root element has been processed.
     * This method sets up the pet slots for display.
     */
    @FXML
    public void initialize() {
        setupPetSlots();
    }

    /**
     * Sets up the slots for displaying pets in the grid.
     * It clears any existing children and populates the grid with pet tiles or empty slots.
     */
    private void setupPetSlots() {
        petGrid.getChildren().clear();
        List<Pet> userPets = petManager.getAllUsersPets(userId);

        for (int i = 0; i < NUM_SLOTS; i++) {
            VBox tile = (i < userPets.size()) ? createPetTile(userPets.get(i)) : createEmptyPetTile();
            petGrid.add(tile, i % 4, i / 4);
        }
    }

    /**
     * Creates a tile for an existing pet.
     *
     * @param pet The pet to display in the tile.
     * @return A VBox containing the pet's information.
     */
    private VBox createPetTile(Pet pet) {
        VBox tile = createTileLayout();
        tile.getChildren().addAll(createPetTypeText(pet), createPetImageView(pet), createPetNameText(pet));
        tile.setOnMouseClicked(e -> openPetInteraction(pet));
        return tile;
    }

    /**
     * Creates a tile for an empty pet slot.
     *
     * @return A VBox indicating the slot is empty.
     */
    private VBox createEmptyPetTile() {
        VBox tile = createTileLayout();
        tile.getChildren().addAll(createDefaultImageView(), createEmptySlotText());
        tile.setOnMouseClicked(e -> showErrorMessage("Error: This tile does not contain a pet!"));
        return tile;
    }

    /**
     * Creates a layout for a pet tile.
     *
     * @return A VBox with the specified styles for a pet tile.
     */
    private VBox createTileLayout() {
        VBox tile = new VBox();
        tile.setStyle("-fx-border-color: #00796B; -fx-border-width: 2; -fx-background-color: #B2DFDB; -fx-alignment: center; -fx-pref-width: 200; -fx-pref-height: 200; -fx-border-radius: 10; -fx-background-radius: 10;");
        tile.setPrefSize(200, 200);
        tile.setSpacing(5);
        return tile;
    }

    /**
     * Creates a Text node displaying the type of the pet.
     *
     * @param pet The pet whose type is to be displayed.
     * @return A Text node showing the pet's type.
     */
    private Text createPetTypeText(Pet pet) {
        Text petTypeText = new Text(pet.getType());
        petTypeText.setStyle("-fx-font-size: 18px; -fx-fill: #004D40; -fx-font-weight: bold;");
        return petTypeText;
    }

    /**
     * Creates an ImageView displaying the pet's image.
     *
     * @param pet The pet whose image is to be displayed.
     * @return An ImageView for the pet's image.
     */
    private ImageView createPetImageView(Pet pet) {
        ImageView petImageView = new ImageView();
        petImageView.setFitWidth(100);
        petImageView.setFitHeight(100);
        petImageView.setPreserveRatio(true);
        setImage(petImageView, getPetImagePath(pet.getType()));
        return petImageView;
    }

    /**
     * Creates a Text node displaying the name of the pet.
     *
     * @param pet The pet whose name is to be displayed.
     * @return A Text node showing the pet's name.
     */
    private Text createPetNameText(Pet pet) {
        Text petNameText = new Text(pet.getName());
        petNameText.setStyle("-fx-font-size: 16px; -fx-fill: #00796B; -fx-font-weight: bold;");
        return petNameText;
    }

    /**
     * Creates an ImageView for the default image used when no pet is present.
     *
     * @return An ImageView with the default image.
     */
    private ImageView createDefaultImageView() {
        ImageView defaultImageView = new ImageView();
        defaultImageView.setFitWidth(100);
        defaultImageView.setFitHeight(100);
        defaultImageView.setPreserveRatio(true);
        setImage(defaultImageView, DEFAULT_IMAGE_PATH);
        return defaultImageView;
    }

    /**
     * Creates a Text node indicating an empty slot.
     *
     * @return A Text node showing "Empty Slot".
     */
    private Text createEmptySlotText() {
        Text emptyText = new Text("Empty Slot");
        emptyText.setStyle("-fx-font-size: 16px; -fx-fill: black;");
        return emptyText;
    }

    /**
     * Sets the image for the given ImageView from the specified image path.
     * If the image cannot be found, it sets the default image.
     *
     * @param imageView The ImageView to set the image for.
     * @param imagePath The path of the image to load.
     */
    private void setImage(ImageView imageView, String imagePath) {
        try (InputStream imageStream = getClass().getResourceAsStream(imagePath)) {
            if (imageStream != null) {
                imageView.setImage(new Image(imageStream));
            } else {
                System.err.println("Error: Image not found for path: " + imagePath);
                imageView.setImage(new Image(getClass().getResourceAsStream(DEFAULT_IMAGE_PATH)));
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }

    /**
     * Gets the image path for a pet based on its type.
     *
     * @param petType The type of the pet.
     * @return The image path for the pet, or null if the type is unrecognized.
     */
    private String getPetImagePath(String petType) {
        switch (petType.toLowerCase()) {
            case "cat":
            case "dog":
            case "fish":
            case "bird":
                return String.format(PET_IMAGE_PATH_FORMAT, petType.toLowerCase());
            default:
                return null;
        }
    }

    /**
     * Displays an error message to the user for a brief duration.
     *
     * @param message The error message to display.
     */
    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> messageLabel.setText(""));
        pause.play();
    }

    /**
     * Opens the pet interaction view for a specific pet.
     *
     * @param pet The pet to manage.
     */
    private void openPetInteraction(Pet pet) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/PetInteractionView.fxml"));
            Scene scene = new Scene(loader.load());

            PetInteractionController petInteractionController = loader.getController();
            petInteractionController.setPet(pet);

            Stage stage = (Stage) petGrid.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Manage Pet: " + pet.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event when the create pet button is pressed.
     * Changes the scene to the Pet Creation view.
     */
    @FXML
    private void onCreatePetPress() {
        changeScene("/vpm/gui_prototype/fxml/PetCreationView.fxml", "Create New Pet");
    }

    /**
     * Opens the settings view when the settings button is pressed.
     */
    @FXML
    private void openSettingsView() {
        changeScene("/vpm/gui_prototype/fxml/SettingsView.fxml", "Settings", true);
    }

    /**
     * Handles the event when the logout button is pressed.
     * Changes the scene to the login view.
     */
    @FXML
    private void onLogoutPress() {
        changeScene("/vpm/gui_prototype/fxml/LoginView.fxml", "Logout");
    }

    /**
     * Changes the current scene to a new one specified by the FXML path and title.
     *
     * @param fxmlPath The path to the FXML file for the new scene.
     * @param title The title for the new stage.
     */
    private void changeScene(String fxmlPath, String title) {
        changeScene(fxmlPath, title, false);
    }

    /**
     * Changes the current scene, optionally creating a new stage.
     *
     * @param fxmlPath The path to the FXML file for the new scene.
     * @param title The title for the new stage.
     * @param newStage Indicates whether to create a new stage.
     */
    private void changeScene(String fxmlPath, String title, boolean newStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            Stage stage = newStage ? new Stage() : (Stage) createPetButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            if (newStage) {
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showErrorMessage("Error: Could not open " + title + " view.");
        }
    }
}
