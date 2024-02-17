package com.example.kataquiz;

/**
 * Holds information of a specific user
 */
public class User {
    // instance variables
    private String UID; // unique UID of this user
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    /**
     * Default constructor for a User
     */
    public User() {}

    /**
     * Constructor for User object
     * @param UID unique UID of user
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email of user
     * @param password password of user
     */
    public User(String UID, String firstName, String lastName, String email, String password) {
        this.UID = UID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /**
     * Gets the UID for this user
     * @return the UID of this user
     */
    public String getUID() {
        return UID;
    }

    /**
     * Sets the UID for this user
     * @param UID the new UID
     */
    public void setUID(String UID) {
        this.UID = UID;
    }

    /**
     * Gets the first name of this user
     * @return the first name of this user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of this user
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of this user
     * @return the last name of this user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the user
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the email of this user
     * @return the email of this user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of this user
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of this user
     * @return the password of this user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of this user
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
