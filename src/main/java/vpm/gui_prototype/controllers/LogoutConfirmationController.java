package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.function.Consumer;

public class LogoutConfirmationController {

    private Consumer<Boolean> onConfirmationCallback;

    @FXML
    private Label confirmationMessage;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;

    public void setMessage(String message) {
        confirmationMessage.setText(message);
    }

    @FXML
    private void onConfirm() {
        if (onConfirmationCallback != null) {
            onConfirmationCallback.accept(true); // Notify that the user confirmed
        }
        // Close the dialog
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel() {
        if (onConfirmationCallback != null) {
            onConfirmationCallback.accept(false); // Notify that the user canceled
        }
        // Close the dialog
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setOnConfirmationCallback(Consumer<Boolean> callback) {
        this.onConfirmationCallback = callback;
    }
}
