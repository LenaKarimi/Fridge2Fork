package Controller;


import Model.Recipe;

import java.util.List;

public class ControllerTest {
    public static void main (String[] args){
        try{
            RecipeController recipeController = new RecipeController();
            List<Recipe> chickenrecipes = recipeController.searchRecipes("chicken");
            List<Recipe> beefrecipes = recipeController.searchRecipes("beef");
            List<String> names = recipeController.searchRecipeNames("chicken");

            for (String name : names){
                System.out.println (name);
            }

            for (Recipe recipe : chickenrecipes){
               System.out.println(recipe.getName());
            }

            for (Recipe recipe : beefrecipes){
                System.out.println(recipe.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

