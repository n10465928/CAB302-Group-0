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

public class CollectionController {

    private static final int NUM_SLOTS = 8;
    private static final String DEFAULT_IMAGE_PATH = "/assets/default.png";
    private static final String PET_IMAGE_PATH_FORMAT = "/assets/%s.png";

    private final PetManager petManager;
    private final int userId;

    @FXML
    private GridPane petGrid;
    @FXML
    private Button createPetButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Label messageLabel;
    @FXML
    private Button logoutButton;

    public CollectionController() {
        this.petManager = new PetManager(new SqlitePetDAO());
        this.userId = UserSession.getInstance().getUserId();
    }

    @FXML
    public void initialize() {
        setupPetSlots();
    }

    private void setupPetSlots() {
        petGrid.getChildren().clear();
        List<Pet> userPets = petManager.getAllUsersPets(userId);

        for (int i = 0; i < NUM_SLOTS; i++) {
            VBox tile = (i < userPets.size()) ? createPetTile(userPets.get(i)) : createEmptyPetTile();
            petGrid.add(tile, i % 4, i / 4);
        }
    }

    private VBox createPetTile(Pet pet) {
        VBox tile = createTileLayout();
        tile.getChildren().addAll(createPetTypeText(pet), createPetImageView(pet), createPetNameText(pet));
        tile.setOnMouseClicked(e -> openPetInteraction(pet));
        return tile;
    }

    private VBox createEmptyPetTile() {
        VBox tile = createTileLayout();
        tile.getChildren().addAll(createDefaultImageView(), createEmptySlotText());
        tile.setOnMouseClicked(e -> showErrorMessage("Error: This tile does not contain a pet!"));
        return tile;
    }

    private VBox createTileLayout() {
        VBox tile = new VBox();
        tile.setStyle("-fx-border-color: #00796B; -fx-border-width: 2; -fx-background-color: #B2DFDB; -fx-alignment: center; -fx-pref-width: 200; -fx-pref-height: 200; -fx-border-radius: 10; -fx-background-radius: 10;");
        tile.setPrefSize(200, 200);
        tile.setSpacing(5);
        return tile;
    }

    private Text createPetTypeText(Pet pet) {
        Text petTypeText = new Text(pet.getType());
        petTypeText.setStyle("-fx-font-size: 18px; -fx-fill: #004D40; -fx-font-weight: bold;");
        return petTypeText;
    }

    private ImageView createPetImageView(Pet pet) {
        ImageView petImageView = new ImageView();
        petImageView.setFitWidth(100);
        petImageView.setFitHeight(100);
        petImageView.setPreserveRatio(true);
        setImage(petImageView, getPetImagePath(pet.getType()));
        return petImageView;
    }

    private Text createPetNameText(Pet pet) {
        Text petNameText = new Text(pet.getName());
        petNameText.setStyle("-fx-font-size: 16px; -fx-fill: #00796B; -fx-font-weight: bold;");
        return petNameText;
    }

    private ImageView createDefaultImageView() {
        ImageView defaultImageView = new ImageView();
        defaultImageView.setFitWidth(100);
        defaultImageView.setFitHeight(100);
        defaultImageView.setPreserveRatio(true);
        setImage(defaultImageView, DEFAULT_IMAGE_PATH);
        return defaultImageView;
    }

    private Text createEmptySlotText() {
        Text emptyText = new Text("Empty Slot");
        emptyText.setStyle("-fx-font-size: 16px; -fx-fill: black;");
        return emptyText;
    }

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

    private void showErrorMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> messageLabel.setText(""));
        pause.play();
    }

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

    @FXML
    private void onCreatePetPress() {
        changeScene("/vpm/gui_prototype/fxml/PetCreationView.fxml", "Create New Pet");
    }

    @FXML
    private void openSettingsView() {
        changeScene("/vpm/gui_prototype/fxml/SettingsView.fxml", "Settings", true);
    }

    @FXML
    private void onLogoutPress() {
        changeScene("/vpm/gui_prototype/fxml/LoginView.fxml", "Logout");
    }

    private void changeScene(String fxmlPath, String title) {
        changeScene(fxmlPath, title, false);
    }

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
