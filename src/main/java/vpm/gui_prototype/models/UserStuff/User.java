package vpm.gui_prototype.models.UserStuff;

import java.time.LocalDateTime;

/**
 * Represents a user in the system with attributes such as username, password,
 * email, phone number, and last interaction time.
 */
public class User {
    // Fields representing the attributes of a User
    private int id;                           // Unique identifier for the user
    private String username;                  // The username of the user
    private String password;                  // The password of the user
    private String email;                     // The user's email address
    private String phone;                     // The user's phone number
    private LocalDateTime lastInteractionTime; // Timestamp of the last interaction with the system

    /**
     * Constructor to initialize a new User object with username, password, email, and phone.
     * @param username The username of the user.
     * @param password The password of the user.
     * @param email The email address of the user.
     * @param phone The phone number of the user.
     */
    public User(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.lastInteractionTime = null;      // Initialize last interaction time as null (not set)
    }

    // Getter and setter for the user's unique ID
    public int getId() {
        return id;  // Returns the user's unique ID
    }

    public void setId(int id) {
        this.id = id;  // Sets the user's unique ID
    }

    // Getter and setter for the user's username
    public String getUsername() {
        return username;  // Returns the user's username
    }

    public void setUsername(String username) {
        this.username = username;  // Sets the user's username
    }

    // Getter and setter for the user's password
    public String getPassword() {
        return password;  // Returns the user's password
    }

    public void setPassword(String password) {
        this.password = password;  // Sets the user's password
    }

    // Getter and setter for the user's email
    public String getEmail() {
        return email;  // Returns the user's email
    }

    public void setEmail(String email) {
        this.email = email;  // Sets the user's email
    }

    // Getter and setter for the user's phone number
    public String getPhone() {
        return phone;  // Returns the user's phone number
    }

    public void setPhone(String phone) {
        this.phone = phone;  // Sets the user's phone number
    }

    /**
     * Gets the last interaction time.
     * @return The last time the user interacted with the system.
     */
    public LocalDateTime getLastInteractionTime() {
        return lastInteractionTime;  // Returns the last interaction time
    }

    /**
     * Sets the last interaction time.
     * @param lastInteractionTime The timestamp to set as the last interaction time.
     */
    public void setLastInteractionTime(LocalDateTime lastInteractionTime) {
        this.lastInteractionTime = lastInteractionTime;  // Updates the last interaction time
    }
}
