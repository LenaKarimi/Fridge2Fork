package Database;

import Model.*;
import Database.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing recipes in the database.
 * Handles saving, retrieving and mapping recipe records including ingredients stored as JSON.
 * @author Intisaar
 */
public class RecipeDAO {

    private final ObjectMapper mapper = new ObjectMapper(); // skapar ett mapper objekt

    /**
     * Saves a recipe to the database.
     * If a recipe with the same meal ID already exists, the insert is silently ignored.
     * @param recipe the recipe to save
     * @throws SQLException if a database access error occurs
     * @throws JsonProcessingException if the ingredient list cannot be serialised to JSON
     */
    public void saveRecipe(Recipe recipe) throws SQLException, JsonProcessingException { // sparar recept i databasen
       // on confilct = inget görs om receptet redan finns
        String sql = """
                INSERT INTO recipes (meal_id, name, instructions, image_url, cuisine, cuisine_group, ingredients)
                VALUES (?, ?, ?, ?, ?, ?, ?::jsonb)
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

    /**
     * Retrieves a single recipe from the database by its meal ID.
     * @param mealId the ID of the recipe to retrieve
     * @return the matching Recipe, or null if no match is found
     * @throws SQLException if a database access error occurs
     * @throws JsonProcessingException if the stored ingredient JSON cannot be parsed
     */
    public Recipe getRecipeById(String mealId) throws SQLException, JsonProcessingException { // hämta recept från databasen
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

    /**
     * Returns all recipes that a given user has liked.
     * Joins the recipes and liked_recipes tables to find the matches.
     * @param profileId the ID of the user
     * @return list of liked recipes
     * @throws SQLException if a database access error occurs
     * @throws JsonProcessingException if stored ingredient JSON cannot be parsed
     */
    public List<Recipe> getLikedRecipes(int profileId) throws SQLException, JsonProcessingException { // hämtar alla recept som en profil gillat
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

    // mapper som gör om rad från databasen (resultSet) till ett objekt
    /**
     * Maps a row from the database result set to a Recipe object.
     * Deserialises the ingredient list from JSON and converts the cuisine string to an enum.
     * @param resultSet the result set positioned at the current row
     * @return a Recipe built from the result set data
     * @throws SQLException if a database access error occurs
     * @throws JsonProcessingException if the ingredient JSON cannot be parsed
     */
    private Recipe mapToRecipe(ResultSet resultSet) throws SQLException, JsonProcessingException {
        String cuisineString = resultSet.getString("cuisine"); // hämtar cuisine som text från databasen
        Cuisine cuisine = null;
        if (cuisineString != null) {
            try {
                cuisine = Cuisine.valueOf(cuisineString); // konverterar text till enum värde
            } catch (IllegalArgumentException ignored) {} // om en match ej finns förblir det null
        }

        List<Ingredient> ingredients = new ArrayList<>();
        String ingredientsJson = resultSet.getString("ingredients"); // hämtar ingridienserna som json-sträng
        if (ingredientsJson != null) {
            ingredients = mapper.readValue( //konverterar json sträng till ingridiens objekt
                    ingredientsJson,
                    mapper.getTypeFactory().constructCollectionType(List.class, Ingredient.class) // talar om för object mapper att json ska bli en list
            );
        }

        //skapar och returnerar ett nytt recepie objekt med data från databasen
        return new Recipe(
                resultSet.getString("meal_id"),
                resultSet.getString("name"),
                resultSet.getString("instructions"),
                resultSet.getString("image_url"),
                ingredients,
                cuisine
        );
    }
}
