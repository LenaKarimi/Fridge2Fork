package Database;

import Model.*;
import DTO.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing pantry items in the database.
 * Handles adding, retrieving, deleting and mapping pantry items per user.
 * @author Racil
 */
public class PantryItemDAO {


    /**
     * Adds a new pantry item for a given user.
     * @param profileId the ID of the user
     * @param name the name of the ingredient
     * @param expiryDate the expiry date of the ingredient
     * @throws SQLException if a database access error occurs
     */
    public void addItem(int profileId, String name, LocalDate expiryDate) throws SQLException {
        String sql = "INSERT INTO pantry_items (profile_id, name, expiry_date) VALUES (?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, profileId);
            preparedStatement.setString(2, name);
            preparedStatement.setDate(3, Date.valueOf(expiryDate));
            preparedStatement.executeUpdate();
        }
    }


    /**
     * Returns all pantry items for a given user that have not yet expired.
     * Results are ordered by expiry date ascending.
     * @param profileId the ID of the user
     * @return list of valid pantry items
     * @throws SQLException if a database access error occurs
     */
    public List<PantryItem> getPantryItems(int profileId) throws SQLException {
       String sql = """
               SELECT id, name, expiry_date FROM pantry_items
               WHERE profile_id = ? AND expiry_date >= CURRENT_DATE
               ORDER BY expiry_date ASC
               """;
       List<PantryItem> pantryItems = new ArrayList<>();
       try (Connection connection = DbConnection.getConnection();
       PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, profileId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                pantryItems.add(mapToPantryItem(resultSet, profileId));
            }
       }
       return pantryItems;
    }


    /**
     * Deletes a specific pantry item from the database by its ID.
     * @param itemId the ID of the item to delete
     * @throws SQLException if a database access error occurs
     */
    public void deletePantryItem(int itemId) throws SQLException {
        String sql = "DELETE FROM pantry_items WHERE id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, itemId);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Deletes all expired pantry items for a given user.
     * An item is considered expired if its expiry date is before today.
     * @param profileId the ID of the user
     * @throws SQLException if a database access error occurs
     */
    public void deleteExpiredItems(int profileId) throws SQLException {
        String sql = "DELETE FROM pantry_items WHERE profile_id = ? AND expiry_date < CURRENT_DATE";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, profileId);
            preparedStatement.executeUpdate();
        }

    }

    /**
     * Maps a row from the database result set to a PantryItem object.
     * @param resultSet the result set positioned at the current row
     * @param profileId the ID of the user the item belongs to
     * @return a PantryItem built from the result set data
     * @throws SQLException if a database access error occurs
     */
    private PantryItem mapToPantryItem(ResultSet resultSet, int profileId) throws SQLException {
        return new PantryItem(
                resultSet.getInt("id"),
                profileId,
                resultSet.getString("name"),
                resultSet.getDate("expiry_date").toLocalDate()
        );
    }
}
