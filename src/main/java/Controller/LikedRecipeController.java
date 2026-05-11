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

    //hämta alla gillade recept för en användare
}
