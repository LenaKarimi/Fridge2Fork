package TheMealDbAPI;

import Model.Category;
import Model.Cuisine;
import Model.Diet;
import Model.Recipe;

import java.util.List;

public class Main {
    public static void main (String[] args){
        HttpTheMealDbClient httpTheMealDbClient = new HttpTheMealDbClient();

        MealRepository mealRepository = new MealRepository(httpTheMealDbClient);
        MealMapper mapper = new MealMapper();

        TheMealManager mealManager = new TheMealManager(mealRepository);

        List<Recipe> results = mealManager.searchForRecepie("spinach", "Italian", "Vegan");
        List<Recipe> result2 = mealManager.searchForRecepie("eggs", "Egyptian", "Vegetarian");
        List<Recipe> result3 = mealManager.searchForRecepie("peas", "India", "Vegetarian");



        for (Recipe recepie : results){
            System.out.println(recepie.getName());
            System.out.println(recepie.getCuisine());
            System.out.println(recepie.getDiet());
            System.out.println(recepie.getInstructions());
        }
        System.out.println();
        for (Recipe recepie : result2){
            System.out.println(recepie.getName());
            System.out.println(recepie.getCuisine());
            System.out.println(recepie.getDiet());
            System.out.println(recepie.getInstructions());
        }
        System.out.println();
        for (Recipe recepie : result3){
            System.out.println(recepie.getName());
            System.out.println(recepie.getCuisine());
            System.out.println(recepie.getDiet());
            System.out.println(recepie.getInstructions());
        }
        System.out.println();

        TheMealDbDTO dto = mealRepository.getMealById("52867");
        Recipe recipe = mapper.toDomain(dto);
        System.out.println(recipe.getName() + " - " + recipe.getDiet());

    }
}
