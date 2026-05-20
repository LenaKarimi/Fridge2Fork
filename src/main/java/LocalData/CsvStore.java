package LocalData;

import Model.Cuisine;
import Model.Ingredient;
import Model.Recipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class CsvStore {

    private CsvStore() {
    }

    public static boolean hasMealExport(Path csvDir) {
        return Files.exists(csvDir.resolve("meals.csv"))
                && Files.exists(csvDir.resolve("meal_ingredients.csv"));
    }

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

            recipes.add(recipe);
        }

        return recipes;
    }

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

    private static Cuisine parseCuisine(String area) {
        if (area == null || area.isBlank()) return null;
        try {
            for (Cuisine cuisine : Cuisine.values()) {
                if (cuisine.name().equalsIgnoreCase(area.trim())) {
                    return cuisine;
                }
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }


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

