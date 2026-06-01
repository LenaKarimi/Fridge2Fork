package Model;

/**
 * Represents a user profile in the application.
 * Contains user information.
 * @author Racil
 * @author Intisaar
 */
public class Profile {
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;

    /**
     * Constructs a Profile object with an ID.
     * @param id the unique user ID
     * @param username the username
     * @param password the password
     * @param name the full name of the user
     * @param email the email address
     */
    public Profile(int id, String username, String password, String name, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    /**
     * Constructs a Profile object without an ID.
     * @param username the username
     * @param password the password
     * @param name the full name of the user
     * @param email the email address
     */
    public Profile(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    /**
     * Returns the unique database ID of this user profile.
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
}


