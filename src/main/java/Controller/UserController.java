package Controller;
import Database.*;
import Model.*;
import java.sql.SQLException;
import DTO.*;

public class UserController {

    private final ProfileDAO profileDAO = new ProfileDAO();
    private ProfileDTO currentUser;



    // denna metod är till för kontroller vid skapandet av nytt konto
    public boolean registerUser(String username, String password, String name, String email) {
        try {
            Profile existing = profileDAO.getProfileByUsername(username);

            if (existing != null) {
                return false;
            }

            Profile newProfile = new Profile(username, password, name, email);
            profileDAO.createProfile(newProfile);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // vid inloggning returnerar den en profil dto till guit annars null
    public ProfileDTO login(String username, String password) {
        try {
            Profile profile = profileDAO.getProfileByUsername(username);
            if (profile == null) {
                return null;
            }
            if (!profile.getPassword().equals(password)) {
                return null;
            }
            ProfileDTO loggedInUser = new ProfileDTO(profile.getId(), profile.getUsername(), profile.getPassword(), profile.getName(), profile.getEmail());
            this.currentUser = loggedInUser;
            return loggedInUser;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
}
