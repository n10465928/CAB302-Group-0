package vpm.gui_prototype.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import vpm.gui_prototype.models.DatabaseStuff.UserData.IUserDAO;
import vpm.gui_prototype.models.DatabaseStuff.UserData.SqliteUserDAO;
import vpm.gui_prototype.models.UserStuff.User;

import java.io.IOException;
import java.util.Objects;

public class RegisterController {

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField confirmPasswordField;

    private IUserDAO userDAO;

    public RegisterController() {
        userDAO = new SqliteUserDAO();
    }

    //helper functions verifying data is ready to be sent to database
    //check if all fields are filled, true if they are otherwise faslse
    public boolean fieldsFilled(String username, String password, String confirmPassword){
        return !username.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty();
    }
    //check password and confirm password match, true if they do otherwise false
    public boolean matchingPassword(String password, String confirmPassword){
        return Objects.equals(password, confirmPassword);
    }
    //checks that data  is ready to interact with database, returns true if it is, otherwise returns false and outputs errors
    public boolean inputsReady(String username, String password, String confirmPassword){
        if(!fieldsFilled(username, password, confirmPassword)){
            // ToDo: Make UI effects to alert users that they need to fill in all the boxes
            System.out.println("Lack of credentials");
            return false;
        }
        else if(!matchingPassword(password, confirmPassword)){
            // ToDo: Make UI effects to alert users that the username existed
            System.out.println(username + " is already existed");
            return false;
        }
        else{return true;}
    }

    @FXML
    void onRegisterPress() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check inputs are valid
        if (!inputsReady(username, password, confirmPassword)){
            return;
        }

        // Check if the user existed
        if (userDAO.getUserByUsername(username) != null) {
            // ToDo: Make UI effects to alert users that the username existed
            System.out.println(username + " is already existed");
            return;
        }

        // Todo: Email and Phone can be added later in the profile stuff if we have one
        User user = new User(username, password, null, null);
        userDAO.addUser(user);
        // Todo: Maybe shows something that indicates successfully registered

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vpm/gui_prototype/fxml/LoginView.fxml"));
            Parent LoginView = loader.load();

            // Get the current stage and load new scene
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(LoginView));

            //change title
            stage.setTitle("Register");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
