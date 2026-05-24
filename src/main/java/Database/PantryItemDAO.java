package Database;

import Database.*;
import Model.*;
import DTO.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PantryItemDAO {

    // lägger till en ny profukt i databasen för den specifika personen
    public void addItem(int profileId, String name, LocalDate expiryDate) throws SQLException {
        String sql = "INSERT INTO pantry_items (profile_id, name, expiry_date) VALUES (?, ?, ?)"; // detta är vår sql request
        try (Connection connection = DbConnection.getConnection(); // vi öppnar db uppkopplingen
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, profileId); // skcikar in våra frågetecken
            preparedStatement.setString(2, name);
            preparedStatement.setDate(3, Date.valueOf(expiryDate));
            preparedStatement.executeUpdate();
        }
    }

    // hämtar enbart de produkter som ej passerat utgångsdatumet
    public List<PantryItem> getPantryItems(int profileId) throws SQLException {
       String sql = """
               SELECT id, name, expiry_date FROM pantry_items
               WHERE profile_id = ? AND expiry_date >= CURRENT_DATE
               ORDER BY expiry_date ASC
               """; // hämtar produkter med utgångsdatum som är dagens datum eller senare
       List<PantryItem> pantryItems = new ArrayList<>(); // lista som fylls på med produkter
       try (Connection connection = DbConnection.getConnection();
       PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, profileId);
            ResultSet resultSet = preparedStatement.executeQuery(); // kör frågan och sparar resultatet
            while (resultSet.next()) { // loopar resultatet
                pantryItems.add(mapToPantryItem(resultSet, profileId)); //gör om till ett model objekt plus lägger till i listan
            }
       }
       return pantryItems; // returnerar listan
    }

    //resnar utgångna produkter
    public void deletePantryItem(int itemId) throws SQLException {
        String sql = "DELETE FROM pantry_items WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, itemId);
            preparedStatement.executeUpdate();
        }
    }

    public void deleteExpiredItems(int profileId) throws SQLException {
        String sql = "DELETE FROM pantry_items WHERE profile_id = ? AND expiry_date < CURRENT_DATE";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, profileId);
            preparedStatement.executeUpdate();
        }

    }

    private PantryItem mapToPantryItem(ResultSet resultSet, int profileId) throws SQLException {
        return new PantryItem(
                resultSet.getInt("id"),
                profileId,
                resultSet.getString("name"),
                resultSet.getDate("expiry_date").toLocalDate()
        );
    }
}
