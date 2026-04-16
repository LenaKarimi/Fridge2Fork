package API;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main (String[] args){
        ApiClient client = new ApiClient();
        MealManager mealManager = new MealManager(client);

        ArrayList<String> userIngredients = new ArrayList<>();
        userIngredients.add("soy sauce");
        userIngredients.add("chicken breast");
        userIngredients.add("egg");
        userIngredients.add("honey");
        userIngredients.add("garlic");
        userIngredients.add("carrot");
        userIngredients.add("curry powder");
        userIngredients.add("chicken stock");
        userIngredients.add("sunflower oil");
        userIngredients.add("onions");
        userIngredients.add("plain flour");

        List<TheMealDbDTO> meals = mealManager.searchForRecepie("chicken_breast", "Japanese",userIngredients);
        System.out.println("Antal recept: " + meals.size());
        for (TheMealDbDTO m : meals){
            System.out.println("Name: " + m.getStrMeal());
            List<String> ing = m.getIngredientList();
            List<String> mea = m.getMeasureList();

            for (int i = 0; i<mea.size(); i++){
                System.out.println(ing.get(i) + " - " + mea.get(i));
            }
            System.out.println(m.getStrInstructions());
        }

    }
}
