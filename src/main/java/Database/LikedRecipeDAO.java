package Database;


import java.sql.*;

/**
 * Data Access Object for managing liked recipes in the database.
 * Handles saving, removing and checking liked recipes per user.
 * @author Racil
 */
public class LikedRecipeDAO {


    /**
     * Saves a liked recipe for a given user.
     * Duplicate entries are silently ignored.
     * @param profieId the ID of the user
     * @param mealId the ID of the recipe to like
     * @throws SQLException if a database access error occurs
     */
    public void likedRecipe(int profieId, String mealId) throws SQLException {

        String sql = "INSERT INTO liked_recipes (profile_id, meal_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, profieId);
            preparedStatement.setString(2, mealId);
            preparedStatement.execute();
        }
    }


    /**
     * Removes a liked recipe for a given user.
     * @param profieId the ID of the user
     * @param mealId the ID of the recipe to unlike
     * @throws SQLException if a database access error occurs
     */
    public void unlikeRecipe(int profieId, String mealId) throws SQLException {

        String sql = "DELETE FROM liked_recipes WHERE profile_id = ? AND meal_id = ?";
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, profieId);
            preparedStatement.setString(2, mealId);
            preparedStatement.execute();
        }
    }


    /**
     * Checks whether a specific recipe is liked by a given user.
     * @param profieId the ID of the user
     * @param mealId the ID of the recipe to check
     * @return true if the recipe is liked, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean isLiked(int profieId, String mealId) throws SQLException {

        String sql = "SELECT 1 FROM liked_recipes WHERE profile_id = ? AND meal_id = ?";
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, profieId);
            preparedStatement.setString(2, mealId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }
}
