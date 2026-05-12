package Controller;

import Database.*;
import Model.*;
import java.util.List;
import java.sql.SQLException;


public class LikedRecipeController {
    private final LikedRecipeDAO likedRecipeDAO = new LikedRecipeDAO();
    private final RecipeDAO recipeDAO = new RecipeDAO();

    // metod som sparar recept som är gilalde i databasen
    public void likeRecipe(int profieId, Recipe recipe)  {
        try {
            recipeDAO.saveRecipe(recipe); // sparar receptet om det ej redan finns
            likedRecipeDAO.likedRecipe(profieId, recipe.getId()); // kopplar till användaren
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ta bort gillning
    public void unlikeRecipe(int profieId, String mealId)  {
        try {
            likedRecipeDAO.unlikeRecipe(profieId, mealId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //hämta alla gillade recept för en användare
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
    public boolean isLiked(int profieId, String mealId) {
        try {
            return likedRecipeDAO.isLiked(profieId, mealId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
