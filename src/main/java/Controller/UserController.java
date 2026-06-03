package Controller;
import Database.*;
import Model.*;
import java.sql.SQLException;
import DTO.*;

/**
 * Controller responsible for user authentication and profile management.
 * Handles registration, login, logout and profile updates.
 * @author Lena, Racil
 */
public class UserController {

    private final ProfileDAO profileDAO = new ProfileDAO();
    private ProfileDTO currentUser;
    private String loginError;



    /**
     * Registers a new user account.
     * Validates that all fields are filled in, that the password meets requirements
     * and that the username is not already taken.
     * @param username the desired username
     * @param password the desired password
     * @param name the user's full name
     * @param email the user's email address
     * @return null if registration is successful, or an error message string if it fails
     */
    public String registerUser(String username, String password, String name, String email) {
       if (username.isBlank() || password.isBlank() || name.isBlank() || email.isBlank()) {
           return "Fill in all fields!";
       }

       String passwordError = safePassword(password);
       if (passwordError != null) {
           return passwordError;
       }

        try {
            Profile existing = profileDAO.getProfileByUsername(username);

            if (existing != null) {
                return "Username is already taken!";
            }

            Profile newProfile = new Profile(username, password, name, email);
            profileDAO.createProfile(newProfile);
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Something went wrong, try again!";
        }
    }


    /**
     * Attempts to log in a user with the given credentials.
     * Sets the current user on success.
     * @param username the username to log in with
     * @param password the password to log in with
     * @return a ProfileDTO for the logged-in user, or null if login fails
     */
    public ProfileDTO login(String username, String password) {
        if (username.isBlank() || password.isBlank()) {
            loginError = "Fill in all fields!";
            return null;
        }
        try {
            Profile profile = profileDAO.getProfileByUsername(username);
            if (profile == null) {
                loginError = "Username not found!";
                return null;
            }
            if (!profile.getPassword().equals(password)) {
                loginError = "Wrong password!";
                return null;
            }
            loginError = null;
            ProfileDTO loggedInUser = new ProfileDTO(profile.getId(), profile.getUsername(), profile.getPassword(), profile.getName(), profile.getEmail());
            this.currentUser = loggedInUser;
            return loggedInUser;
        } catch (SQLException e) {
            e.printStackTrace();
            loginError = "Something went wrong, try again!";
            return null;
        }
    }

    /**
     * returns the current login error message.
     * @return the login error message, null if no login error
     */
    public String getLoginError() {
        return loginError;
    }

    /**
     * Updates the profile information for a given user.
     * Validates that all fields are filled in and that the password meets requirements.
     * @param id the ID of the user to update
     * @param username the new username
     * @param password the new password
     * @param name the new full name
     * @param email the new email address
     * @return null if the update is successful, or an error message string if it fails
     */
    public String updateProfile(int id, String username, String password, String name, String email) {

        if (password.isBlank() || name.isBlank() || email.isBlank() || username.isBlank()) {
            return "Fill in all fields!";
        }

        if (password != null && !password.isBlank()) {
            String passwordError = safePassword(password);
            if (passwordError != null) {
                return passwordError;
            }
        }

        try {
            Profile updated = new Profile(id, username, password, name, email);
            profileDAO.updateProfile(updated);
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return "Something went wrong, try again!";
        }
    }

    /**
     * Returns the current loged in user
     * @return the current loged in user
     */
    public ProfileDTO getCurrentUser() {
        return currentUser;
    }


    /**
     * Logs out the current user by clearing the session.
     */
    public void logout() {
        this.currentUser = null;
    }

    /**
     * Validates that a password meets the security requirements.
     * The password must be at least 10 characters and contain at least one
     * uppercase letter, one lowercase letter and one digit.
     * @param password the password to validate
     * @return null if the password is valid, or an error message string if it is not
     */
    private String safePassword(String password) {

        if (password.length() < 10) {
            return "Password must be at least 10 characters!";
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUppercase = true;
            if (Character.isLowerCase(c)) hasLowercase = true;
            if (Character.isDigit(c)) hasNumber = true;
        }

        if (!hasUppercase) return  "Password must contain at least one uppercase letter!";
        if (!hasLowercase) return "Password must contain at least one lowercase letter!";
        if (!hasNumber) return "Password must contain at least one number!";

        return null;
    }

}
