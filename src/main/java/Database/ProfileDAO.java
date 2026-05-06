package Database;

import Model.*;
import Database.DbConnection;
import java.sql.*;


public class ProfileDAO {

    //denna metod koppar upp sig till databasen och skapar en ny användare
    public void createProfile(Profile profile) throws SQLException{
        String sql = "INSERT INTO profiles (username, password_hash, name, email) VALUES (?, ?, ?, ?)"; // det vi vill skriva
        Connection connection = DbConnection.getConnection();
        try (PreparedStatement insertObject = connection.prepareStatement(sql)) { // försöker connecta via connaction klassen

            insertObject.setString(1, profile.getUsername());
            insertObject.setString(2,profile.getPassword());
            insertObject.setString(3, profile.getName());
            insertObject.setString(4, profile.getEmail());
            insertObject.executeUpdate();
        }
    }

    // denna metoden kopplar upp sig mot databasen och hämtar användaren baserat på användarnamn
    public Profile getProfileByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM profiles WHERE username = ?"; // vår sql query
        Connection connection = DbConnection.getConnection();
        try (PreparedStatement selectObject = connection.prepareStatement(sql)) { // försöker göra en koppling

            selectObject.setString(1, username); // användarnamnet v skcikar in från parametern
            try (ResultSet resultSet = selectObject.executeQuery()) { // kör queryn i databasen och sparar resultatet
                 if (resultSet.next()) { // returnerar true = det finns data, false = tomt resultat , har med pekaern att göra
                     return mapToProfile(resultSet); // tar data från raden och skapar ett profile objekt
                 }
            }
        }
        return null;
    }

    public void updateProfile(Profile profile) throws SQLException{
        String sql = "UPDATE profiles SET username = ?, password_hash = ?, name = ?, email = ? WHERE id = ?";
        Connection connection = DbConnection.getConnection();
        try (PreparedStatement updateObject = connection.prepareStatement(sql)) {
            updateObject.setString(1, profile.getUsername());
            updateObject.setString(2, profile.getPassword());
            updateObject.setString(3, profile.getName());
            updateObject.setString(4, profile.getEmail());
            updateObject.setInt(5, profile.getId());
            updateObject.executeUpdate();

        }
    }

    // mapper, returnerar profile objekt
    private Profile mapToProfile(ResultSet resultSet) throws SQLException {
        return new Profile( // skickar till kostruktorn och skapar nytt objekt
                resultSet.getInt("id"),
                resultSet.getString("username"), // hämtar värdet från varje kolumn i tabellen
                resultSet.getString("password_hash"),
                resultSet.getString("name"),
                resultSet.getString("email")
        );
    }

}
