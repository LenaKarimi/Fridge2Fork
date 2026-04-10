package TheMealDbAPI;

import Model.Category;

import java.util.List;

public class Main {
    public static void main (String[] args){
        HttpTheMealDbClient httpTheMealDbClient = new HttpTheMealDbClient();

        MealRepository mealRepository = new MealRepository(httpTheMealDbClient);

        Category.MealManager mealManager = new Category.MealManager(mealRepository);

        List<TheMealDbDTO> results = mealManager.searchForRecepie("chicken", "Indian");

        for (TheMealDbDTO recepie : results){
            System.out.println(recepie.strMeal);
        }

    }
}
