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
        List<Recipe> recipes = new ArrayList<>();

        if(proteins == null || proteins.isEmpty()){
            return recipes;
        }

        for(String protein : proteins){
            if(protein == null || protein.isBlank()){
                continue;
            }
            List<TheMealDbDTO> meals = mealRepository.getMealsByIngredient(protein);

            if (meals != null) {
                for(TheMealDbDTO meal : meals) {
                    TheMealDbDTO detailedMeal = mealRepository.getMealById(meal.idMeal);
                    Recipe recipe = mealMapper.toDomain(detailedMeal);
                    recipes.add(recipe);
                }
            }
        }
        return recipes;
    }

    //Returnerar bara namnen på recepten som hittats
    public List<String> searchRecipeNames(List<String> ingredients) throws Exception {
        List<Recipe> recipes = searchRecipes(ingredients);
        List<String> names = new ArrayList<>();

        for (Recipe recipe : recipes){
            names.add(recipe.getName());
        }
        return names;
    }

    //Mappar ett Recepie-objekt till ett DTO-objekt
    public RecepieDTO getRecepieDTO(Recipe recipe){
        if (recipe == null) return null;
        return new RecepieDTO(recipe.getName(), recipe.getImageUrl());
    }

    //Mappar en hel lista av Recepie till en lista av DTO
    public List<RecepieDTO> getRecepieDTOList(List<Recipe> recipes){
        List<RecepieDTO> dtos = new ArrayList<>();
        for (Recipe r : recipes){
            dtos.add(getRecepieDTO(r));
        }
        return dtos;
    }
}