package Controller;
import Database.*;
import Model.*;
import java.sql.SQLException;
import DTO.*;

public class UserController {

    private final ProfileDAO profileDAO = new ProfileDAO();
    private ProfileDTO currentUser;
    private String loginError;



    // denna metod är till för kontroller vid skapandet av nytt konto
    public String registerUser(String username, String password, String name, String email) {
       if (username.isBlank() || password.isBlank() || name.isBlank() || email.isBlank()) {
           return "Fill in all fields!";
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

    // vid inloggning returnerar den en profil dto till guit annars null
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

    public String getLoginError() {
        return loginError;
    }


    public void updateProfile(int id, String username, String password, String name, String email) {
        try {
            Profile updated = new Profile(id, username, password, name, email);
            profileDAO.updateProfile(updated);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ProfileDTO getCurrentUser() {
        return currentUser;
    }

    //jag la bara till en liten logout metod :D //Maya
    public void logout() {
        this.currentUser = null;
    }

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
        if (!hasNumber) return "Password must contain at least one number!"

        return null;
    }
}
