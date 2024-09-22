package vpm.gui_prototype.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.UserStuff.UserSession;
import vpm.gui_prototype.services.GlobalPetCleanlinessService;
import vpm.gui_prototype.services.GlobalPetStatService;

import java.io.IOException;
import java.io.InputStream;

public class PetInteractionController {

    private PetManager petManager;
    int userId = UserSession.getInstance().getUserId();


    @FXML
    private Label HappinessField;
    @FXML
    private Label HungerField;
    @FXML
    private Label CleanField;
    @FXML
    private Label picturePetName;
    @FXML
    private Label picturePetType;
    @FXML
    private ImageView image;
    @FXML
    private Label petName;
    @FXML
    private Label petType;
    @FXML
    private Label petColour;
    @FXML
    private Label petPersonality;

    private Pet currentPet;

    public void setPet(Pet pet) {
        currentPet = pet;
        updatePetDetails();
    }

    @FXML
    public void initialize() {
        petManager = new PetManager(new SqlitePetDAO());
        try {
            // Set the controller reference in the global service
            GlobalPetStatService.getInstance().setController(this);
            startGlobalPetStatService();
        } catch (Exception e) {
            System.err.println("Error initializing PetInteractionController: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update the pet details on the interaction view
    private void updatePetDetails() {
        if (currentPet != null) {
            HappinessField.setText(String.format("%.1f", currentPet.GetHappiness()));
            HungerField.setText(String.format("%.1f", currentPet.GetFoodSatisfaction()));
            CleanField.setText(currentPet.GetIsDirty() ? "Dirty" : "Clean");
            picturePetName.setText(currentPet.GetName());
            picturePetType.setText(currentPet.GetType());
            petName.setText(currentPet.GetName());
            petType.setText(currentPet.GetType());
            petColour.setText(currentPet.GetColour());
            petPersonality.setText(currentPet.GetPersonality());
            setImage();
        }
    }

    // Refresh UI manually
    public void refreshUI() {
        Platform.runLater(this::updatePetDetails); // Refresh UI on JavaFX Application Thread
    }

    private void setImage() {
        String imagePath = getPetImagePath(currentPet.GetType());
        if (imagePath != null) {
            InputStream imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                image.setImage(new Image(imageStream));
            } else {
                System.err.println("Error: Pet image not found for path: " + imagePath);
            }
        } else {
            image.setImage(new Image(getClass().getResourceAsStream("/assets/default.png")));
        }
    }

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
                return null;
        }
    }

    @FXML
    private void onPlay() {
        currentPet.IncreaseHappiness(1f);
        petManager.updatePet(currentPet, userId); // Update the database
        refreshUI(); // Refresh UI fields
    }

    @FXML
    private void onFeed() {
        currentPet.Feed(1f);
        petManager.updatePet(currentPet, userId); // Update the database
        refreshUI(); // Refresh UI fields
    }

    @FXML
    private void onClean() {
        currentPet.Clean();
        petManager.updatePet(currentPet, userId); // Update the database
        refreshUI(); // Refresh UI fields
    }

    @FXML
    private void onEditName() {
        openEditPetFieldView("name");
    }

    @FXML
    private void onEditAge() {
        openEditPetFieldView("age");
    }

    @FXML
    private void onEditColour() {
        openEditPetFieldView("colour");
    }

    @FXML
    private void onEditPersonality() {
        openEditPetFieldView("personality");
    }

    private void openEditPetFieldView(String field) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/EditPetFieldView.fxml"));
            Scene scene = new Scene(loader.load());

            EditPetFieldController editPetFieldController = loader.getController();
            editPetFieldController.setPetAndField(currentPet, field);

            // Pass a callback to refresh pet details on save
            editPetFieldController.setOnSaveCallback(this::refreshUI);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Pet " + field.substring(0, 1).toUpperCase() + field.substring(1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onInteraction() {
        goBackToCollectionView();
    }

    @FXML
    private void onDelete() {
        if (currentPet != null) {
            petManager.deletePet(currentPet, userId); // Ensure petManager is initialized before calling this
            goBackToCollectionView();
        }
    }

    private void goBackToCollectionView() {
        try {
            stopGlobalPetStatService(); // Stop services when leaving the view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
            Scene scene = new Scene(loader.load());

            CollectionController collectionController = loader.getController();
            collectionController.initialize(); // Call initialize to refresh the pet collection

            Stage stage = (Stage) HappinessField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pet Collection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Start both global services when the interaction screen is active
    private void startGlobalPetStatService() {
        try {
            GlobalPetStatService statService = GlobalPetStatService.getInstance();
            GlobalPetCleanlinessService cleanlinessService = GlobalPetCleanlinessService.getInstance();

            statService.startService(); // Start happiness and hunger decrement service
            cleanlinessService.startService(); // Start cleanliness service
        } catch (Exception e) {
            System.err.println("Error starting global services: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Stop both global services when exiting the interaction screen
    private void stopGlobalPetStatService() {
        try {
            GlobalPetStatService statService = GlobalPetStatService.getInstance();
            GlobalPetCleanlinessService cleanlinessService = GlobalPetCleanlinessService.getInstance();

            statService.stopService(); // Stop happiness and hunger decrement service
            cleanlinessService.stopService(); // Stop cleanliness service
        } catch (Exception e) {
            System.err.println("Error stopping global services: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
