package Database;

import Database.*;
import java.sql.*;

public class LikedRecipeDAO {

    // sparar likat recept till en användare
    public void likedRecipe(int profieId, String mealId) throws SQLException {
        //lägger in båda idn i tabellen, dubletter ignoreras
        String sql = "INSERT INTO liked_recipes (profie_id, meal_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, profieId); // lägger in frågetecken
            preparedStatement.setString(2, mealId);
            preparedStatement.execute();
        }
    }

    //avlika recept
    public void unlikeRecipe(int profieId, String mealId) throws SQLException {
        //tar bort rad där båda idn matchar
        String sql = "DELETE FROM liked_recipes WHERE profie_id = ? AND meal_id = ?";
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, profieId);
            preparedStatement.setString(2, mealId);
            preparedStatement.execute();
        }
    }

    // vet ej om nödvändigt men kontrollerar om ett recept är gillat
    public boolean isLiked(int profieId, String mealId) throws SQLException {
        // söker efter en rad i tabbelen där idn matchar
        String sql = "SELECT 1 * FROM liked_recipes WHERE profie_id = ? AND meal_id = ?";
        try (Connection connection = DbConnection.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, profieId);
            preparedStatement.setString(2, mealId);
            ResultSet resultSet = preparedStatement.executeQuery(); // kör frågan och sparar i resultSet
            return resultSet.next(); // returnerar true om en rad hittades annars false
        }
    }
}
