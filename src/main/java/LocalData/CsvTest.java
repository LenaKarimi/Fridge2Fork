package LocalData;

import Model.Recipe;
import Model.Ingredient;

import java.nio.file.Path;
import java.util.List;

public class CsvTest {

    public static void main(String[] args) throws Exception {

        Path csvDir = Path.of("data");

        List<Recipe> recipes = CsvStore.readMeals(csvDir);

        System.out.println("Antal recept inlästa: " + recipes.size());

        Recipe first = recipes.get(0);
        System.out.println("Första receptet: " + first.getName());
        System.out.println("Cuisine: " + first.getCuisine());
        System.out.println("Antal ingredienser: " + first.getIngredients().size());

        for (Ingredient ing : first.getIngredients()) {
            System.out.println("  - " + ing.getName() + " | " + ing.getMeasure());
        }
    }
}