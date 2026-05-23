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
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
