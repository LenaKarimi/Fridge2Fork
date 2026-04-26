package Controller;


import Model.Recepie;

import java.util.List;

public class ControllerTest {
    public static void main (String[] args){
        try{
            RecipeController recipeController = new RecipeController();
            List<Recepie> chickenrecipes = recipeController.searchRecipes("chicken");
            List<Recepie> beefrecipes = recipeController.searchRecipes("beef");
            List<String> names = recipeController.searchRecipeNames("chicken");

            for (String name : names){
                System.out.println (name);
            }

            for (Recepie recipe : chickenrecipes){
               System.out.println(recipe.getName());
            }

            for (Recepie recipe : beefrecipes){
                System.out.println(recipe.getName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}

