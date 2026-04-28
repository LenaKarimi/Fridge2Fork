package Controller;

import Model.Recipe;
import TheMealDbAPI.MealMapper;
import TheMealDbAPI.MealRepository;
import TheMealDbAPI.HttpTheMealDbClient;
import DTO.*;
import TheMealDbAPI.TheMealDbDTO;

import java.util.ArrayList;
import java.util.List;

public class RecipeController {
    //hanterar logik kring recept, 70% gräns osv

    //De olika klasserna som används från Api:et
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    public RecipeController() {
        this.mealRepository = new MealRepository(new HttpTheMealDbClient());
        this.mealMapper = new MealMapper();

    }

    //Funktion för att ska en arraylist av de ingredienser som finns baserat på det primära ingrediensen
    public List<Recipe> searchRecipes(List<String> proteins) throws Exception {

        List<Recipe> recipes = new ArrayList<>();

        if (proteins == null || proteins.isEmpty()) {
            return recipes;
        }

        final int MAX_MEALS_PER_INGREDIENT = 5; // begränsa antal detaljanrop per ingrediens för snabbare respons

        for (String protein : proteins) {
            if (protein == null || protein.isBlank()) {
                continue;
            }
            System.out.println("RecipeController: fetching meals for protein = " + protein);
            List<TheMealDbDTO> meals = mealRepository.getMealsByIngredient(protein);

            if (meals != null) {
                int count = 0;
                for (TheMealDbDTO meal : meals) {
                    if (meal == null || meal.idMeal == null) continue;
                    if (count >= MAX_MEALS_PER_INGREDIENT) break;
                    count++;

                    System.out.println("RecipeController: fetching detail for id = " + meal.idMeal);
                    TheMealDbDTO detailedMeal = mealRepository.getMealById(meal.idMeal);
                    if (detailedMeal != null) {
                        Recipe recipe = mealMapper.toDomain(detailedMeal);
                        System.out.println("Recipe name after mapping: " + recipe.getName());
                        recipes.add(recipe);
                    }
                }
            } else {
                System.out.println("RecipeController: no meals returned for " + protein);
            }
        }
        return recipes;
    }

    //Funktion för att ska en arraylist av de ingredienser som finns baserat på det primära ingrediensen
    public List<Recipe> searchRecipes(String mainIngredient) throws Exception {

        if (mainIngredient == null || mainIngredient.isBlank()) {
            return new ArrayList<>();
        }

        List<Recipe> recipes = new ArrayList<>();

        List<TheMealDbDTO> meals = mealRepository.getMealsByIngredient(mainIngredient);

        for (TheMealDbDTO meal : meals) {
            TheMealDbDTO detailedMeal = mealRepository.getMealById(meal.idMeal);

            Recipe recipe = mealMapper.toDomain(detailedMeal);


            recipes.add(recipe);
        }
        return recipes;
    }

    public List<Recipe> searchRecipesByIngredients(List<String> ingredients) throws Exception {
        if (ingredients == null || ingredients.isEmpty()) {
            throw new IllegalArgumentException("You have to choose at leats one alternative in all categories to continue!");
        }

        List<Recipe> allRecipes = new ArrayList<>();

        for (String ingredient : ingredients) {

            if (ingredient == null || ingredient.isBlank() || ingredient.equalsIgnoreCase("none")) {
                continue;
            }


            List<TheMealDbDTO> meals = mealRepository.getMealsByIngredient(ingredient);

            for (TheMealDbDTO meal : meals) {
                TheMealDbDTO detailedMeal = mealRepository.getMealById(meal.idMeal);
                Recipe recipe = mealMapper.toDomain(detailedMeal);
                allRecipes.add(recipe);


            }

        }
        return allRecipes;
    }

    //Gör om objekt till strängar detta ska användas i gui sen.
    public List<String> searchRecipeNamesByIngredients(List<String> ingredients) throws Exception {
        List<Recipe> recipes = searchRecipesByIngredients(ingredients);

        List<String> names = new ArrayList<>();

        for (Recipe recipe : recipes) {
            names.add(recipe.getName());


        }
        return names;
    }

    //recipe
    //gör om om objektet från ett modelobjekt till ett DTO-objekt
    public RecipeDTO getRecipeDTO(Recipe Recipe) {
        return new RecipeDTO(Recipe.getName(), Recipe.getImageUrl());
    }

    //Mappar en hel lista av Recipe till en lista av DTO
    public List<RecipeDTO> getRecipeDTOList(List<Recipe> recipes) {
        List<RecipeDTO> dtos = new ArrayList<>();
        for (Recipe r : recipes) {
            dtos.add(getRecipeDTO(r));
        }
        return dtos;
    }
}
