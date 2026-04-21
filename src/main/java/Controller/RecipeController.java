package Controller;

import Model.Recipe;
import TheMealDbAPI.MealMapper;
import TheMealDbAPI.MealRepository;
import TheMealDbAPI.HttpTheMealDbClient;
import TheMealDbAPI.TheMealDbDTO;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

public class RecipeController {
    //hanterar logik kring recept, 70% gräns osv

    //De olika klasserna som används från Api:et
    private MealRepository mealRepository;
    private MealMapper mealMapper;
    private HttpTheMealDbClient httpTheMealDbClient;

    public RecipeController(){
        this.httpTheMealDbClient = new HttpTheMealDbClient();
        this.mealRepository = new MealRepository(this.httpTheMealDbClient);
        this.mealMapper = new MealMapper();

    }
     //Funktion för att ska en arraylist av de ingredienser som finns baserat på det primära ingrediensen
    public List<Recipe> searchRecipes (String mainIngredient) throws Exception {

        if(mainIngredient==null || mainIngredient.isBlank()){
            return new ArrayList<>();
        }

        List<Recipe> recipes = new ArrayList<>();

        List<TheMealDbDTO> meals = mealRepository.getMealsByIngredient(mainIngredient);

        for (TheMealDbDTO meal : meals){
            TheMealDbDTO detailedMeal = mealRepository.getMealById(meal.idMeal);

            Recipe recipe = mealMapper.toDomain(detailedMeal);

            recipes.add(recipe);


        }
        return recipes;

    }

    //Gör om objekt till strängar detta ska användas i gui sen.
    public List<String> searchRecipeNames(String mainIngredient) throws Exception{
        List<Recipe> recipes = searchRecipes(mainIngredient);

        List<String> names = new ArrayList<>();

        for (Recipe recipe : recipes){
            names.add(recipe.getName());


        }
        return names;
    }
}
