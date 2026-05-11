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
}
