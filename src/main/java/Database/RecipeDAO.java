package Database;

import Model.*;
import Database.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {

    private final ObjectMapper mapper = new ObjectMapper(); // skapar ett mapper objekt

    public void saveRecipe(Recipe recipe) throws SQLException, JsonProcessingException { // sparar recept i databasen
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
