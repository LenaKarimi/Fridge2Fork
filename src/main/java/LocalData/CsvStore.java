package LocalData;

import Model.Cuisine;
import Model.Ingredient;
import Model.Recipe;
import Model.Diet;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Utility class for reading recipe data from local CSV files.
 * Parses meals and ingredients into Recipe domain objects for offline use.
 * @author Intisaar
 */
public final class CsvStore {

    private CsvStore() {
    }

    /**
     * Checks whether the required CSV export files exist in the given directory.
     * @param csvDir the directory to check
     * @return true if both meals.csv and meal_ingredients.csv exist, false otherwise
     */
    public static boolean hasMealExport(Path csvDir) {
        return Files.exists(csvDir.resolve("meals.csv"))
                && Files.exists(csvDir.resolve("meal_ingredients.csv"));
    }

    /**
     * Reads all recipes from the CSV files in the given directory.
     * Combines meal rows with their corresponding ingredients into Recipe objects.
     * @param csvDir the directory containing meals.csv and meal_ingredients.csv
     * @return list of Recipe objects parsed from the CSV files
     * @throws IOException if a file cannot be read
     */
    public static List<Recipe> readMeals(Path csvDir) throws IOException {
        Map<String, RecipeRow> recipeRows = readRecipeRows(csvDir.resolve("meals.csv"));
        Map<String, List<Ingredient>> ingredientsByMealId = readIngredientsByMealId(csvDir.resolve("meal_ingredients.csv"));

        List<Recipe> recipes = new ArrayList<>();

        for (RecipeRow row : recipeRows.values()) {
            List<Ingredient> ingredients = ingredientsByMealId.getOrDefault(row.id(), List.of());

            Cuisine cuisine = parseCuisine(row.area());

            Recipe recipe = new Recipe(
                    row.id(),
                    row.name(),
                    row.instructions(),
                    row.thumbnailUrl(),
                    ingredients,
                    cuisine
            );
            recipe.setDiet(parseDiet(row.category));
            recipes.add(recipe);
        }

        return recipes;
    }

    /**
     * Reads the meals CSV file and returns a map of meal ID to RecipeRow.
     * @param file path to the meals.csv file
     * @return map of meal ID to RecipeRow
     * @throws IOException if the file cannot be read
     */
    private static Map<String, RecipeRow> readRecipeRows(Path file) throws IOException {
        Map<String, RecipeRow> rows = new LinkedHashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                List<String> values = Csv.parseLine(line);

                if (values.size() < 12) continue;

                RecipeRow row = new RecipeRow(
                        values.get(0),
                        values.get(1),
                        values.get(2),
                        values.get(3),
                        values.get(4),
                        values.get(5),
                        values.get(6),
                        values.get(7),
                        values.get(8),
                        values.get(9),
                        values.get(10),
                        values.get(11)
                );

                rows.put(row.id(), row);
            }
        }

        return rows;
    }



    /**
     * Reads the meal ingredients CSV file and returns a map of meal ID to ingredient list.
     * @param file path to the meal_ingredients.csv file
     * @return map of meal ID to list of Ingredients
     * @throws IOException if the file cannot be read
     */
    private static Map<String, List<Ingredient>> readIngredientsByMealId(Path file) throws IOException {
        Map<String, List<Ingredient>> result = new LinkedHashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                List<String> values = Csv.parseLine(line);

                if (values.size() < 5) continue;

                String mealId = values.get(0);
                String ingredientName = values.get(2);
                String measure = values.get(3);

                if (ingredientName == null || ingredientName.isBlank()) continue;

                Ingredient ingredient = new Ingredient(ingredientName, measure);
                result.computeIfAbsent(mealId, ignored -> new ArrayList<>()).add(ingredient);
            }
        }

        return result;
    }

    /**
     * Parses a category string into a Diet enum value.
     * Returns null if the category does not match a known diet.
     * @param category the category string from the CSV
     * @return the matching Diet, or null if no match is found
     */
    private static Diet parseDiet(String category) {
        if (category == null || category.isBlank()) return null;
        return switch (category.trim()) {
            case "Vegan" -> Diet.VEGAN;
            case "Vegetarian" -> Diet.VEGETARIAN;
            default -> null;
        };
    }

    /**
     * Parses an area string into a Cuisine enum value.
     * The comparison is case-insensitive. Returns null if no match is found.
     * @param area the area string from the CSV
     * @return the matching Cuisine, or null if no match is found
     */
    private static Cuisine parseCuisine(String area) {
        if (area == null || area.isBlank()) return null;
        try {
            for (Cuisine cuisine : Cuisine.values()) {
                if (cuisine.name().equalsIgnoreCase(area.trim().replace(" ", ""))) {
                    return cuisine;
                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Represents a recipe record containing all recipe related data taken from the
     * data source. Each instance is imutable and stores information about a recipe.
     *
     * @param id the unique identifier of the recipe
     * @param name the name of the recipe
     * @param category the recipe category
     * @param area the cuisine associated with the recipe
     * @param instructions the coocking instructions
     * @param thumbnailUrl the URL of the recipe image
     * @param tags the tags associated eith the recipe
     * @param youtubeUrl the url of a related Yputube video
     * @param sourceUrl the URL of the original recipe source
     * @param imageSourceUrl the URL of the image source
     * @param creativeCommonsConfirmed indicates if the recipe cobntet is under Creative Commons license
     * @param dateModified the date the recipe was last modified
     */
    private record RecipeRow(
            String id,
            String name,
            String category,
            String area,
            String instructions,
            String thumbnailUrl,
            String tags,
            String youtubeUrl,
            String sourceUrl,
            String imageSourceUrl,
            String creativeCommonsConfirmed,
            String dateModified) {
    }
}

