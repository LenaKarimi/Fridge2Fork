package Database;

import Model.*;
import Database.DbConnection;
import java.sql.*;


public class ProfileDAO {

    //denna metod koppar upp sig till databasen och skapar en ny användare
    public void createProfile(Profile profile) throws SQLException{
        String sql = "INSERT INTO Profile (username, password_hash, name, email) VALUES (?, ?, ?, ?)"; // det vi vill skriva
        try (Connection connection = DbConnection.getConnection(); // försöker connecta via connaction klassen
             PreparedStatement insertObject = connection.prepareStatement(sql)) { // objekt som håller sql-query och kör den mot darabasen
            insertObject.setString(1, profile.getUsername());
            insertObject.setString(2,profile.getPassword());
            insertObject.setString(3, profile.getName());
            insertObject.setString(4, profile.getEmail());
            insertObject.executeUpdate();
        }
    }

    // denna metoden kopplar upp sig mot databasen och hämtar användaren baserat på användarnamn
    public Profile getProfileByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM Profile WHERE username = ?"; // vår sql query
        try (Connection connection = DbConnection.getConnection(); // försöker göra en koppling
             PreparedStatement selectObject = connection.prepareStatement(sql)) { // vårt insert objekt
            selectObject.setString(1, username); // användarnamnet v skcikar in från parametern
            ResultSet resultSet = selectObject.executeQuery(); // kör queryn i databasen och sparar resultatet
            if (resultSet.next()) { // returnerar true = det finns data, false = tomt resultat , har med pekaern att göra
                return mapToProfile(resultSet); // tar data från raden och skapar ett profile objekt
            }
        }
        return null;
    }

    // mapper, returnerar profile objekt
    private Profile mapToProfile(ResultSet resultSet) throws SQLException {
        return new Profile( // skickar till kostruktorn och skapar nytt objekt
                resultSet.getString("username"), // hämtar värdet från varje kolumn i tabellen
                resultSet.getString("password_hash"),
                resultSet.getString("name"),
                resultSet.getString("email")
        );
    }

}
