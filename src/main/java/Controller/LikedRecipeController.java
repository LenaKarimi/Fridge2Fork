package Controller;

import Database.*;
import Model.*;
import java.util.List;
import java.sql.SQLException;


/**
 * Controller responsible for handling liked (favourited) recipes.
 * Communicates with the database to save, remove and retrieve liked recipes.
 * @author Racil
 */
public class LikedRecipeController {
    private final LikedRecipeDAO likedRecipeDAO = new LikedRecipeDAO();
    private final RecipeDAO recipeDAO = new RecipeDAO();

    // metod som sparar recept som är gilalde i databasen
    /**
     * Saves a recipe as liked for a given user.
     * Stores the recipe in the database if it does not already exist, then links it to the user.
     * @param profieId the ID of the user liking the recipe
     * @param recipe the recipe to like
     */
    public void likeRecipe(int profieId, Recipe recipe)  {
        try {
            recipeDAO.saveRecipe(recipe); // sparar receptet om det ej redan finns
            likedRecipeDAO.likedRecipe(profieId, recipe.getId()); // kopplar till användaren
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ta bort gillning
    /**
     * Removes a liked recipe for a given user.
     * @param profieId the ID of the user
     * @param mealId the ID of the recipe to unlike
     */
    public void unlikeRecipe(int profieId, String mealId)  {
        try {
            likedRecipeDAO.unlikeRecipe(profieId, mealId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //hämta alla gillade recept för en användare
    /**
     * Returns all liked recipes for a given user.
     * @param profieId the ID of the user
     * @return list of liked recipes, or an empty list if none found
     */
    public List<Recipe> getLikedRecipes(int profieId) {
        try {
            return recipeDAO.getLikedRecipes(profieId);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    //denna metod behövs så att hjärtat är ifyllt ifall receptet
    //visas under en senare generering, då hjärtat alltid ska vara
    // ifyllt om det är likat av en användraen
    /**
     * Checks whether a specific recipe is liked by a given user.
     * Used to determine whether to display a filled or empty heart icon.
     * @param profieId the ID of the user
     * @param mealId the ID of the recipe to check
     * @return true if the recipe is liked, false otherwise
     */
    public boolean isLiked(int profieId, String mealId) {
        try {
            return likedRecipeDAO.isLiked(profieId, mealId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
