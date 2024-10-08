package vpm.gui_prototype.models.UserStuff;

import java.time.LocalDateTime;

public class User {
    // Fields representing the attributes of a User
    private int id;                           // Unique identifier for the user
    private String username;                  // The username of the user
    private String password;                  // The password of the user
    private String email;                     // The user's email address
    private String phone;                     // The user's phone number
    private LocalDateTime lastInteractionTime; // Timestamp of the last interaction with the system

    // Constructor to initialize a new User object with username, password, email, and phone
    public User(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.lastInteractionTime = null;      // Initialize last interaction time as null (not set)
    }

    // Getter and setter for the user's unique ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter and setter for the user's username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and setter for the user's password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and setter for the user's email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and setter for the user's phone number
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter for the last interaction time, returns the last time the user interacted with the system
    public LocalDateTime getLastInteractionTime() {
        return lastInteractionTime;
    }

    // Setter for the last interaction time, used to update the time when the user interacts with the system
    public void setLastInteractionTime(LocalDateTime lastInteractionTime) {
        this.lastInteractionTime = lastInteractionTime;
    }
}
