package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.PetData.SqlitePetDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.PetStuff.Pet;
import vpm.gui_prototype.models.DatabaseStuff.PetData.PetManager;
import vpm.gui_prototype.models.UserStuff.UserSession;

import java.io.IOException;
import java.time.LocalDateTime;

public class DeleteConfirmationController {

    private Pet pet;  // The pet to be deleted
    private PetManager petManager;
    private IUserDAO userDAO;
    int userId = UserSession.getInstance().getUserId(); // Manager for pet-related database operations

    @FXML
    private Label confirmationMessage;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    public DeleteConfirmationController() {
        petManager = new PetManager(new SqlitePetDAO());
        this.userDAO = new SqliteUserDAO();
    }

    public void setPet(Pet pet) {
        this.pet = pet;
        confirmationMessage.setText("Are you sure you want to delete " + pet.getName() + "?");
    }

    @FXML
    private void onConfirmDelete() {
        if (pet != null) {
            petManager.deletePet(pet, userId); // Delete the pet from the database
            goBackToCollectionView();  // Navigate back to the collection view
        }
    }

    @FXML
    private void onCancel() {
        closeWindow();  // Simply close the dialog
    }

    private void closeWindow() {
        Stage stage = (Stage) confirmationMessage.getScene().getWindow();
        stage.close();
    }

    private void goBackToCollectionView() {
        try {
            userDAO.setLastInteractionTime(userId, LocalDateTime.now());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
            Scene scene = new Scene(loader.load());

            CollectionController collectionController = loader.getController();
            collectionController.initialize(); // Make sure to initialize the collection view

            Stage stage = (Stage) confirmButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Pet Collection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
