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
           return "Fyll i alla fält!";
       }

        try {
            Profile existing = profileDAO.getProfileByUsername(username);

            if (existing != null) {
                return "Användarnamnet är upptaget!";
            }

            Profile newProfile = new Profile(username, password, name, email);
            profileDAO.createProfile(newProfile);
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Något gick fel, försök igen";
        }
    }

    // vid inloggning returnerar den en profil dto till guit annars null
    public ProfileDTO login(String username, String password) {
        if (username.isBlank() || password.isBlank()) {
            loginError = "Fyll i både användarnamn och lösenord!";
            return null;
        }
        try {
            Profile profile = profileDAO.getProfileByUsername(username);
            if (profile == null) {
                loginError = "Användaren finns inte";
                return null;
            }
            if (!profile.getPassword().equals(password)) {
                loginError = "Fel lösenord";
                return null;
            }
            loginError = null;
            ProfileDTO loggedInUser = new ProfileDTO(profile.getId(), profile.getUsername(), profile.getPassword(), profile.getName(), profile.getEmail());
            this.currentUser = loggedInUser;
            return loggedInUser;
        } catch (SQLException e) {
            e.printStackTrace();
            loginError = "Något gick fel, försök igen";
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
}
