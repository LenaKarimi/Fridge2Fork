package Database;

import Model.*;
import Database.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {

    private final ObjectMapper mapper = new ObjectMapper(); // skapar ett mapper objekt

    public void saveRecipe(Recipe recipe) throws SQLException { // sparar recept i databasen
       // on confilct = inget görs om receptet redan finns
        String sql = """
                INSERT INTO recipes (meal_id, name, instructions, image_url, cuisine, cuisine_group, ingredients)
                VALUES (?, ?, ?, ?, ?, ?::jsonb)
                ON CONFLICT (meal_id) DO NOTHING 
                """;
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) { // förebereder ett ps för sql frågan
            preparedStatement.setString(1, recipe.getId()); // alla frågetecken i sql querryn
            preparedStatement.setString(2, recipe.getName());
            preparedStatement.setString(3, recipe.getInstructions());
            preparedStatement.setString(4, recipe.getImageUrl());
            preparedStatement.setString(5, recipe.getCuisine() !=null ? recipe.getCuisine().name() : null); // sätter cuisin och cuisine type som text annars null om den ej finns
            preparedStatement.setString(6, recipe.getCuisine() != null ? recipe.getCuisine().getCuisineGroup().name() : null );
            preparedStatement.setString(7, mapper.writeValueAsString(recipe.getIngredients())); // konverterar ingridiens listan till json
            preparedStatement.executeUpdate(); // kör sql frågan
        }
    }

    public Recipe getRecipeById(String mealId) throws SQLException { // hämta recept från databasen
        String sql = "SELECT * FROM recipes WHERE meal_id = ?"; // hämtar alla kolumner där meal id matchar efterfrågan
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, mealId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) { // om matchning hittas konvertera till recept objekt
                return mapToRecipe(resultSet);
            }
        }
        return null;
    }

    public List<Recipe> getLikedRecipes(int profileId) throws SQLException { // hämtar alla recept som en profil gillat
        String sql = """
                SELECT r.* FROM recipes r
                JOIN liked_recipes lr ON r.meal_id = lr.meal_id
                WHERE lr.profile_id = ?
                """; // koppar ihop recepies och liked recepies för att hitta rätt
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, profileId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) { // loopar igenom resultatet
                recipes.add(mapToRecipe(resultSet)); // konverterar varje rad till ett recepie objekt och lägger till det i listan
            }
            return recipes;
        }
    }

    // mapper som gör om rad från databasen till ett objekt
    private Recipe mapToRecipe(ResultSet resultSet) throws SQLException {
        String
    }


}
