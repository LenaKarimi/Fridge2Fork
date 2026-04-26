package Controller;

import Model.Recepie;
import TheMealDbAPI.MealMapper;
import TheMealDbAPI.MealRepository;
import TheMealDbAPI.HttpTheMealDbClient;
import DTO.*;

import java.util.ArrayList;
import java.util.List;

public class RecipeController {
    private HttpTheMealDbClient httpTheMealDBClient;
    //hanterar logik kring recept, 70% gräns osv

    //De olika klasserna som används från Api:et
    private MealRepository mealRepository;
    private MealMapper mealMapper;


    public RecipeController(){
        this.mealMapper = new MealMapper();
        this.httpTheMealDBClient = new HttpTheMealDbClient();
        this.mealRepository = new MealRepository(httpTheMealDBClient);

    }
     //Funktion för att ska en arraylist av de ingredienser som finns baserat på det primära ingrediensen
    public List<Recepie> searchRecipes (List<String> proteins) throws Exception {

        List<Recepie> recipes = new ArrayList<>();

        if(proteins==null || proteins.isEmpty()){
            return recipes;
        }

        for(String protein : proteins){
            if(protein == null || protein.isBlank()){
                continue;
            }
            List<TheMealDbDTO> meals = mealRepository.getMealsByIngredient(protein);

            for(TheMealDbDTO meal : meals) {
                TheMealDbDTO mealName = mealRepository.getMealById(meal.idMeal);

                Recepie recepie = mealMapper.toDomain(mealName);
                recipes.add(recepie);

            }
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
