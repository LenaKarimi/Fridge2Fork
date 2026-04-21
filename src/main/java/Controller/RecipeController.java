package Controller;

import DTO.RecepieDTO;
import Model.Recepie;
import TheMealDbAPI.MealMapper;
import TheMealDbAPI.MealRepository;
import TheMealDbAPI.HttpTheMealDbClient;
import DTO.TheMealDbDTO;

import java.util.ArrayList;
import java.util.List;

public class RecipeController {
    //hanterar logik kring recept, 70% gräns osv

    //De olika klasserna som används från Api:et
    private MealRepository mealRepository;
    private MealMapper mealMapper;

    public RecipeController(){
        this.httpTheMealDbClient = new HttpTheMealDbClient();
        this.mealMapper = new MealMapper();

    }
     //Funktion för att ska en arraylist av de ingredienser som finns baserat på det primära ingrediensen
    public List<Recepie> searchRecipes (String mainIngredient) throws Exception {

        if(mainIngredient==null || mainIngredient.isBlank()){
            return new ArrayList<>();
        }

        List<Recepie> recipes = new ArrayList<>();

        List<TheMealDbDTO> meals = mealRepository.getMealsByIngredient(mainIngredient);

        for (TheMealDbDTO meal : meals){
            TheMealDbDTO detailedMeal = mealRepository.getMealById(meal.idMeal);

            Recepie recipe = mealMapper.toDomain(detailedMeal);


            recipes.add(recipe);


        }
        return recipes;

    }

    //Gör om objekt till strängar detta ska användas i gui sen.
    public List<String> searchRecipeNames(String mainIngredient) throws Exception{
        List<Recepie> recipes = searchRecipes(mainIngredient);

        List<String> names = new ArrayList<>();

        for (Recepie recipe : recipes){
            names.add(recipe.getName());


        }
        return names;
    }

    //recipe
    //gör om om objektet från ett modelobjekt till ett DTO-objekt
    public RecepieDTO getRecepieDTO(Recepie recepie){
        return new RecepieDTO(recepie.getName(), recepie.getImageUrl());
    }


}
