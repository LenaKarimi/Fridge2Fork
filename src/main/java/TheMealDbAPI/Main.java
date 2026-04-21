package TheMealDbAPI;

import DTO.*;

import java.util.List;

public class Main {
    public static void main (String[] args){
        HttpTheMealDbClient httpTheMealDbClient = new HttpTheMealDbClient();

        MealRepository mealRepository = new MealRepository(httpTheMealDbClient);

        TheMealManager mealManager = new TheMealManager(mealRepository);

        List<TheMealDbDTO> results = mealManager.searchForRecepie("chicken", "Indian");

        for (TheMealDbDTO recepie : results){
            System.out.println(recepie.strMeal);
            System.out.println(recepie.strInstructions);
        }

    }
}
