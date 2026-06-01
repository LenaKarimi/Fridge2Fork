package DTO;

/**
 * Data Transfer Object (DTO) representing a user profile.
 * It is used to transfer user data between layers.
 * @author Racil
 */
public class ProfileDTO {
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;

    /**
     * Constructs a ProfileDTO with all user fields.
     * @param id the user ID
     * @param username the username
     * @param password the password
     * @param name the full name
     * @param email the email address
     */
    public ProfileDTO(int id, String username, String password, String name, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the unique database ID of this user.
     * @return the user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the login username of this user.
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of this user.
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the full display name of this user.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the email address of this user.
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the login username for this user.
     * Used when the user updates their profile information.
     * @param username the new username to assign
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the full display name for this user.
     * Used when the user updates their profile information.
     * @param name the new full name to assign
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the email address for this user.
     * Used when the user updates their profile information.
     * @param email the new email address to assign
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
