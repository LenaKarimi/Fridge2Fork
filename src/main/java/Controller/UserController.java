package Controller;
import Database.*;
import Model.*;
import java.sql.SQLException;
import DTO.*;

public class UserController {

    private final ProfileDAO profileDAO = new ProfileDAO();

    // denna metod är till för kontroller vid skapandet av nytt konto
    public boolean registerUser(String username, String password, String name, String email) throws SQLException {

        Profile existing = profileDAO.getProfileByUsername(username);

        if (existing != null) {
            return false;
        }

        Profile newProfile = new Profile(username, password, name, email);
        profileDAO.createProfile(newProfile);
        return true;
    }

    // vid inloggning returnerar den en profil dto till guit annars null
    public ProfileDTO login(String username, String password) throws SQLException {
        Profile profile = profileDAO.getProfileByUsername(username);
        if (profile != null) {
            return null;
        }
        if (!profile.getPassword().equals(password)) {
            return null;
        }
        return new ProfileDTO(profile.getId(), profile.getUsername(), profile.getPassword(), profile.getName(), profile.getEmail());
    }
}
