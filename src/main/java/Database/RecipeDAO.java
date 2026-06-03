package Database;

import Model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for managing recipes in the database.
 * Handles saving, retrieving and mapping recipe records including ingredients stored as JSON.
 * @author Racil
 */
public class RecipeDAO {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Saves a recipe to the database.
     * If a recipe with the same meal ID already exists, the insert is silently ignored.
     * @param recipe the recipe to save
     * @throws SQLException if a database access error occurs
     * @throws JsonProcessingException if the ingredient list cannot be serialised to JSON
     */
    public void saveRecipe(Recipe recipe) throws SQLException, JsonProcessingException {

        String sql = """
                INSERT INTO recipes (meal_id, name, instructions, image_url, cuisine, cuisine_group, ingredients)
                VALUES (?, ?, ?, ?, ?, ?, ?::jsonb)
                ON CONFLICT (meal_id) DO NOTHING 
                """;
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, recipe.getId());
            preparedStatement.setString(2, recipe.getName());
            preparedStatement.setString(3, recipe.getInstructions());
            preparedStatement.setString(4, recipe.getImageUrl());
            preparedStatement.setString(5, recipe.getCuisine() !=null ? recipe.getCuisine().name() : null);
            preparedStatement.setString(6, recipe.getCuisine() != null ? recipe.getCuisine().getCuisineGroup().name() : null );
            preparedStatement.setString(7, mapper.writeValueAsString(recipe.getIngredients()));
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Retrieves a single recipe from the database by its meal ID.
     * @param mealId the ID of the recipe to retrieve
     * @return the matching Recipe, or null if no match is found
     * @throws SQLException if a database access error occurs
     * @throws JsonProcessingException if the stored ingredient JSON cannot be parsed
     */
    public Recipe getRecipeById(String mealId) throws SQLException, JsonProcessingException {
        String sql = "SELECT * FROM recipes WHERE meal_id = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, mealId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
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
    public List<Recipe> getLikedRecipes(int profileId) throws SQLException, JsonProcessingException {
        String sql = """
                SELECT r.* FROM recipes r
                JOIN liked_recipes lr ON r.meal_id = lr.meal_id
                WHERE lr.profile_id = ?
                """;
        List<Recipe> recipes = new ArrayList<>();
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, profileId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                recipes.add(mapToRecipe(resultSet));
            }
            return recipes;
        }
    }

    /**
     * Maps a row from the database result set to a Recipe object.
     * Deserialises the ingredient list from JSON and converts the cuisine string to an enum.
     * @param resultSet the result set positioned at the current row
     * @return a Recipe built from the result set data
     * @throws SQLException if a database access error occurs
     * @throws JsonProcessingException if the ingredient JSON cannot be parsed
     */
    private Recipe mapToRecipe(ResultSet resultSet) throws SQLException, JsonProcessingException {
        String cuisineString = resultSet.getString("cuisine");
        Cuisine cuisine = null;
        if (cuisineString != null) {
            try {
                cuisine = Cuisine.valueOf(cuisineString);
            } catch (IllegalArgumentException ignored) {}
        }

        List<Ingredient> ingredients = new ArrayList<>();
        String ingredientsJson = resultSet.getString("ingredients");
        if (ingredientsJson != null) {
            ingredients = mapper.readValue(
                    ingredientsJson,
                    mapper.getTypeFactory().constructCollectionType(List.class, Ingredient.class)
            );
        }


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
