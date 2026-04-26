package Controller;

import Model.Recipe;
import TheMealDbAPI.MealMapper;
import TheMealDbAPI.MealRepository;
import TheMealDbAPI.HttpTheMealDbClient;
import DTO.*;

import java.util.ArrayList;
import java.util.List;

public class RecipeController {
    private HttpTheMealDbClient httpTheMealDBClient;
    private MealRepository mealRepository;
    private MealMapper mealMapper;

    public RecipeController(){
        this.mealMapper = new MealMapper();
        this.httpTheMealDBClient = new HttpTheMealDbClient();
        this.mealRepository = new MealRepository(httpTheMealDBClient);
    }

    //Hämtar kompletta receptobjekt baserat på en lista av ingredienser
    public List<Recipe> searchRecipes(List<String> proteins) throws Exception {
        System.out.println("RecipeController: searchRecipes called with " + proteins);
        List<Recipe> recipes = new ArrayList<>();

        if (proteins == null || proteins.isEmpty()){
            return recipes;
        }

        final int MAX_MEALS_PER_INGREDIENT = 5; // begränsa antal detaljanrop per ingrediens för snabbare respons

        for (String protein : proteins){
            if (protein == null || protein.isBlank()){
                continue;
            }
            System.out.println("RecipeController: fetching meals for protein = " + protein);
            List<TheMealDbDTO> meals = mealRepository.getMealsByIngredient(protein);

<<<<<<< HEAD
            if (meals != null) {
                int count = 0;
                for (TheMealDbDTO meal : meals) {
                    if (meal == null || meal.idMeal == null) continue;
                    if (count >= MAX_MEALS_PER_INGREDIENT) break;
                    count++;

                    System.out.println("RecipeController: fetching detail for id = " + meal.idMeal);
                    TheMealDbDTO detailedMeal = mealRepository.getMealById(meal.idMeal);
                    if (detailedMeal != null) {
                        Recipe recipe = mealMapper.toDomain(detailedMeal);
                        recipes.add(recipe);
                    }
                }
            } else {
                System.out.println("RecipeController: no meals returned for " + protein);
=======
            for(TheMealDbDTO meal : meals) {
                TheMealDbDTO mealName = mealRepository.getMealById(meal.idMeal);

                Recepie recepie = mealMapper.toDomain(mealName);
                recipes.add(recepie);

>>>>>>> parent of 48a46e9 (Update RecipeController.java)
            }
        }
        System.out.println("RecipeController: total recipes collected = " + recipes.size());
        return recipes;
    }

<<<<<<< HEAD
    //Returnerar bara namnen på recepten som hittats
    public List<String> searchRecipeNames(List<String> ingredients) throws Exception {
        List<Recipe> recipes = searchRecipes(ingredients);
=======
    //Gör om objekt till strängar detta ska användas i gui sen.
    public List<String> searchRecipeNames(String mainIngredient) throws Exception{
        List<Recepie> recipes = searchRecipes(mainIngredient);

>>>>>>> parent of 48a46e9 (Update RecipeController.java)
        List<String> names = new ArrayList<>();

        for (Recipe recipe : recipes){
            names.add(recipe.getName());
        }
        return names;
    }

<<<<<<< HEAD
    //Mappar ett Recipe-objekt till ett DTO-objekt
    public RecepieDTO getRecepieDTO(Recipe recipe){
        if (recipe == null) return null;
        return new RecepieDTO(recipe.getName(), recipe.getImageUrl());
    }

    //Mappar en hel lista av Recipe till en lista av DTO
    public List<RecepieDTO> getRecepieDTOList(List<Recipe> recipes){
        List<RecepieDTO> dtos = new ArrayList<>();
        for (Recipe r : recipes){
            dtos.add(getRecepieDTO(r));
        }
        return dtos;
    }
}
=======
    //recipe
    //gör om om objektet från ett modelobjekt till ett DTO-objekt
    public RecepieDTO getRecepieDTO(Recepie recepie){
        return new RecepieDTO(recepie.getName(), recepie.getImageUrl());
    }


}
>>>>>>> parent of 48a46e9 (Update RecipeController.java)
