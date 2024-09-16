package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button LoginButton;

    @FXML
    private Button RegisterButton;


    //go to login screen
    @FXML
    void onLoginPress() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/CollectionView.fxml"));
            Parent LoginView = loader.load();

            // Get the current stage and load new scene
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(new Scene(LoginView));

            //change title
            stage.setTitle("Pet Collection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onRegisterPress() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/RegisterView.fxml"));
            Parent LoginView = loader.load();

            // Get the current stage and load new scene
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            stage.setScene(new Scene(LoginView));

            //change title
            stage.setTitle("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
