package Controller;

import Model.Diet;
import Model.Recipe;
import Model.Ingredient;
import Model.Cuisine;
import TheMealDbAPI.MealMapper;
import TheMealDbAPI.MealRepository;
import TheMealDbAPI.HttpTheMealDbClient;
import DTO.*;
import TheMealDbAPI.TheMealDbDTO;
import LocalData.CsvStore;

import java.nio.file.Path;
import java.util.*;

/**
 * Controller responsible for searching and filtering recipes.
 * Supports both online (API) and offline (CSV) modes.
 * @author Intisaar, Maya, Lena, Racil
 */
public class RecipeController {

    private static final boolean IS_ONLINE = false ;

    private final MealRepository mealRepository;
    private final MealMapper mealMapper;
    private final List<Recipe> localRecipes;


    /**
     * Constructs a RecipeController and initialises API clients and local recipe data.
     * @throws Exception if local CSV data cannot be read
     */

    public RecipeController() throws Exception{

        this.mealRepository = new MealRepository(new HttpTheMealDbClient());
        this.mealMapper = new MealMapper();
        this.localRecipes = CsvStore.readMeals(Path.of("data"));

    }

    /**
     * Huvudmetod för att hitta recept baserat på användarens val av ingredienser
     * Tar emot en map med kategorier och en lista med önskad kök
     * (Kolla upp med kost och om det ska ens finnas kvar!!!!)
     */


    /**
     * Searches for recipes based on the user's selected ingredients, cuisines and diets.
     * Validates that at least one ingredient has been selected per category before searching.
     * @param categoryMap map of category names to lists of selected ingredients
     * @param selectedCuisines list of cuisines to filter by, or empty for no filter
     * @param selectedDiets list of diets to filter by, or empty for no filter
     * @return list of matching recipes sorted by match percentage
     * @throws Exception if the search fails
     */
    public List<Recipe> searchRecipes(Map<String, List<String>> categoryMap, List<Cuisine> selectedCuisines, List <Diet> selectedDiets) throws Exception {

        validateAllCategories(categoryMap);

        List<String> userFridge = extractAllIngredients(categoryMap);

        if (IS_ONLINE) {
            return searchFromApi(userFridge, selectedCuisines, selectedDiets);
        } else {
            return searchFromCsv(userFridge, selectedCuisines, selectedDiets);
        }
    }

    /**
     * Searches for recipes via the TheMealDb API.
     * Collects recipes matching the user's ingredients, filters by cuisine and diet,
     * and returns recipes above 50% match. Falls back to suggestions if none are found.
     * @param userFridge list of ingredients the user has selected
     * @param selectedCuisines list of cuisines to filter by, or empty for no filter
     * @param selectedDiets list of diets to filter by, or empty for no filter
     * @return sorted list of matching recipes, or suggestions if no matches above 50%
     * @throws Exception if the API request fails
     */
    private List<Recipe> searchFromApi(List<String> userFridge, List<Cuisine> selectedCuisines, List <Diet> selectedDiets) throws Exception {

        Set<String> discoveredMealIds = new HashSet<>();
        List<Recipe> matchingRecipes = new ArrayList<>();

        List<Recipe> suggestions = new ArrayList<>();

        for (String ingredient : userFridge) {
            List<TheMealDbDTO> apiResponse = mealRepository.getMealsByIngredient(ingredient);

            if (apiResponse == null) continue;

            for (TheMealDbDTO mealSummary : apiResponse) {

                if (discoveredMealIds.contains(mealSummary.idMeal)) continue;
                discoveredMealIds.add(mealSummary.idMeal);

                try {

                    TheMealDbDTO detailedMeal = mealRepository.getMealById(mealSummary.idMeal);

                    if (detailedMeal == null) {
                        continue;
                    }

                    Recipe recipeObject = mealMapper.toDomain(detailedMeal);

                    if (recipeObject == null) {
                        continue;
                    }


                    double percentage = calculateMatchPercentage(recipeObject, userFridge);

                    recipeObject.setMatchPercentage(percentage);

                    System.out.println("Recept: " + recipeObject.getName() + " Matchning: " + (percentage * 100) + "%");


                    if (!isCorrectCuisine(recipeObject, selectedCuisines)) continue;

                    if (!isCorrectDiet(recipeObject, selectedDiets)) continue;

                    if (percentage >= 0.5) {
                        matchingRecipes.add(recipeObject);
                    } else if (percentage > 0.0){
                        suggestions.add(recipeObject);
                    }

                    if (matchingRecipes.size() >= 30) break;

                } catch (Exception e) {
                    System.out.println("debug: Hoppar över recept ID: " + mealSummary.idMeal + " pga fel: " + e.getMessage());
                }
            }
            if (matchingRecipes.size() >= 30) break;
        }


        if (!matchingRecipes.isEmpty()) {
            matchingRecipes.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
            return matchingRecipes;
        }

        suggestions.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
        return suggestions.subList(0, Math.min(30, suggestions.size()));

    }


    /**
     * Searches for recipes locally from the preloaded CSV data.
     * Filters by cuisine and diet, calculates match percentage and returns
     * recipes above 50% match. Falls back to suggestions if none are found.
     * @param userFridge list of ingredients the user has selected
     * @param selectedCuisines list of cuisines to filter by, or empty for no filter
     * @param selectedDiets list of diets to filter by, or empty for no filter
     * @return sorted list of matching recipes, or suggestions if no matches above 50%
     */
    private List<Recipe> searchFromCsv(List<String> userFridge, List<Cuisine> selectedCuisines, List <Diet> selectedDiets) {
        List<Recipe> matchingRecipes = new ArrayList<>();
        List<Recipe> suggestions = new ArrayList<>();

        for (Recipe recipe : localRecipes) {

            if (!isCorrectCuisine(recipe, selectedCuisines)) continue;

            if (!isCorrectDiet(recipe, selectedDiets)) continue;


            double percentage = calculateMatchPercentage(recipe, userFridge);
            recipe.setMatchPercentage(percentage);

            if (percentage >= 0.5) {
                matchingRecipes.add(recipe);
            } else if (percentage > 0.0) {
                suggestions.add(recipe);
            }

            System.out.println("Suggestion: " + recipe.getName() + " " + percentage);

            if (matchingRecipes.size() >= 30) break;
        }


        if (!matchingRecipes.isEmpty()) {
            matchingRecipes.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
            return matchingRecipes;
        }


        suggestions.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
        return suggestions.subList(0, Math.min(30, suggestions.size()));

    }



    /**
     * Validates that every category in the map contains at least one selected ingredient.
     * @param categoryMap map of category names to lists of selected ingredients
     * @throws IllegalArgumentException if any category has no selection
     */
    private void validateAllCategories(Map<String, List<String>> categoryMap){
        for (String categoryName : categoryMap.keySet()){
            List<String> selections = categoryMap.get(categoryName);


            if (selections == null || selections.isEmpty()){
                throw new IllegalArgumentException("Du saknar val i kategorin : " + categoryName);
            }
        }
    }


    /**
     * Extracts all selected ingredients from the category map, excluding "none" values.
     * Converts all ingredient names to lowercase for consistent API matching.
     * @param categoryMap map of category names to lists of selected ingredients
     * @return flat list of selected ingredient names in lowercase
     */
    private List<String> extractAllIngredients(Map<String, List<String>> categoryMap){
        List <String> allIngredients = new ArrayList<>();
        for ( List<String> ingredientList : categoryMap.values()){
            for (String ingredient : ingredientList){
                if (!ingredient.equalsIgnoreCase("none")){

                    allIngredients.add(ingredient.toLowerCase().trim());
                }
            }
        }
        return allIngredients;
    }


    /**
     * Checks whether a recipe matches the user's selected cuisines.
     * Returns true if no cuisines are selected (no filter applied).
     * @param recipe the recipe to check
     * @param selectedCuisines list of cuisines to filter by
     * @return true if the recipe matches, false otherwise
     */
    private boolean isCorrectCuisine(Recipe recipe, List<Cuisine> selectedCuisines){

        if (selectedCuisines == null || selectedCuisines.isEmpty()) return true;
        return selectedCuisines.contains(recipe.getCuisine());
    }


    /**
     * Checks whether a recipe matches the user's selected diets.
     * Returns true if no diets are selected (no filter applied).
     * @param recipe the recipe to check
     * @param selectedDiets list of diets to filter by
     * @return true if the recipe matches, false otherwise
     */
    private boolean isCorrectDiet(Recipe recipe, List<Diet> selectedDiets){
        if (selectedDiets == null || selectedDiets.isEmpty()) return true;
        return selectedDiets.contains(recipe.getDiet());
    }


    /**
     * Calculates how well a recipe matches the user's available ingredients.
     * Uses partial string matching to handle ingredient name variations.
     * @param recipe the recipe to evaluate
     * @param userFridge list of ingredients the user has selected
     * @return a value between 0.0 and 1.0 representing the match percentage
     */
    private double calculateMatchPercentage(Recipe recipe, List<String> userFridge){
        double matchCount = 0;
        List<Ingredient> recipeIngredients = recipe.getIngredients();

        for (Ingredient ing : recipeIngredients){
            String name = ing.getName().toLowerCase();

            for (String fridgeItem : userFridge){
                if (name.contains(fridgeItem) || fridgeItem.contains(name)){
                    matchCount++;

                    break;
                }
            }
        }
        return matchCount / recipeIngredients.size();
    }


    /**
     * Converts a Recipe domain object to a RecipeDTO for use in the GUI.
     * @param recipe the recipe to convert
     * @return a RecipeDTO with all display fields populated
     */
    public RecipeDTO getRecipeDTO(Recipe recipe) {
        return new RecipeDTO(
                recipe.getName(),
                recipe.getImageUrl(),
                recipe.getInstructions(),
                recipe.getIngredients(),
                recipe.getCuisine(),
                recipe.getMatchPercentage()

        );
    }

    /**
     * Converts a list of Recipe objects to a list of RecipeDTOs.
     * @param recipes the list of recipes to convert
     * @return list of RecipeDTOs
     */
    public List<RecipeDTO> getRecipeDTOList(List<Recipe> recipes) {
        List<RecipeDTO> dtos = new ArrayList<>();
        for (Recipe r : recipes) {
            dtos.add(getRecipeDTO(r));
        }
        return dtos;
    }


}
