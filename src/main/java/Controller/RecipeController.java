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

    //offline=false då körs csv
    //online=true då körs api
    private static final boolean IS_ONLINE = false ;

    //De olika klasserna som används från Api:et
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;
   //listan med recept som laddas in från csv när programmet är offline
    private final List<Recipe> localRecipes;


    /**
     * Constructs a RecipeController and initialises API clients and local recipe data.
     * @throws Exception if local CSV data cannot be read
     */

    public RecipeController() throws Exception{

        //initierar de klasser som behövs för att prata med api:et
        this.mealRepository = new MealRepository(new HttpTheMealDbClient());
        this.mealMapper = new MealMapper();
        //läser in alla recept från CSV-filerna vid start
        this.localRecipes = CsvStore.readMeals(Path.of("data"));

    }

    /**
     * Huvudmetod för att hitta recept baserat på användarens val av ingredienser
     * Tar emot en map med kategorier och en lista med önskad kök
     * (Kolla upp med kost och om det ska ens finnas kvar!!!!)
     */


    //Funktion för att ska en arraylist av de ingredienser som finns baserat på det primära ingrediensen
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

        //Kontrollera att varje kategori i Gui har fått minst ett val none inkluderad.
        validateAllCategories(categoryMap);

        //samla alla valda ingredienser och rensa bort none valen inna vi skickar vidar
        List<String> userFridge = extractAllIngredients(categoryMap);

        //beroende på IS_ONLINE söker vi antingen via API eller CSV
        if (IS_ONLINE) {
            return searchFromApi(userFridge, selectedCuisines, selectedDiets);
        } else {
            return searchFromCsv(userFridge, selectedCuisines, selectedDiets);
        }
    }

    //söker recept via API, samma logik som tidigare
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

        //Sökningen sker här för möjliga recept baserad på valda ingredienser
        Set<String> discoveredMealIds = new HashSet<>();
        List<Recipe> matchingRecipes = new ArrayList<>();

        //samlar recept under 50% som förslag om inga över 50% hittas
        List<Recipe> suggestions = new ArrayList<>();

        for (String ingredient : userFridge) {
            //här sker förfrågningen om specifik ingrediens
            List<TheMealDbDTO> apiResponse = mealRepository.getMealsByIngredient(ingredient);
            //om inte ingrediens finns ska programmet fortsätta till nästa ingrediens och inte krascha
            if (apiResponse == null) continue;

            for (TheMealDbDTO mealSummary : apiResponse) {
                //sparar inte recept som redan är hämtade.
                if (discoveredMealIds.contains(mealSummary.idMeal)) continue;
                discoveredMealIds.add(mealSummary.idMeal);

                try {
                    //hämta fullständiga detaljer dvs ingredienser, isntruktioner osv.
                    TheMealDbDTO detailedMeal = mealRepository.getMealById(mealSummary.idMeal);

                    if (detailedMeal == null) {
                        continue;
                    }

                    Recipe recipeObject = mealMapper.toDomain(detailedMeal);

                    if (recipeObject == null) {
                        continue;
                    }

                    //detta beräknar procentmatchning
                    double percentage = calculateMatchPercentage(recipeObject, userFridge);

                    recipeObject.setMatchPercentage(percentage);
                    //detta syns i terminalen för att se hur mycket match plus om det ens beräknar procent.
                    System.out.println("Recept: " + recipeObject.getName() + " Matchning: " + (percentage * 100) + "%");

                    //filterring baserad på kök
                    if (!isCorrectCuisine(recipeObject, selectedCuisines)) continue;

                    if (!isCorrectDiet(recipeObject, selectedDiets)) continue;
                    //beräkna 50% matchningen
                    if (percentage >= 0.5) {
                        matchingRecipes.add(recipeObject);
                    } else if (percentage > 0.0){
                        suggestions.add(recipeObject);
                    }
                    //samla 30 recept
                    if (matchingRecipes.size() >= 30) break;

                } catch (Exception e) {
                    System.out.println("debug: Hoppar över recept ID: " + mealSummary.idMeal + " pga fel: " + e.getMessage());
                }
            }
            if (matchingRecipes.size() >= 30) break;
        }

        //om vi hittade recept över 50% returneras de
        if (!matchingRecipes.isEmpty()) {
            matchingRecipes.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
            return matchingRecipes;
        }
        //annars returneras förslag under 50%
        suggestions.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
        return suggestions.subList(0, Math.min(30, suggestions.size()));

    }

    //söker recept lokalt från CSV-listan istället för API
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
            //filterring baserad på kök
            if (!isCorrectCuisine(recipe, selectedCuisines)) continue;

            if (!isCorrectDiet(recipe, selectedDiets)) continue;

            //detta beräknar procentmatchning
            double percentage = calculateMatchPercentage(recipe, userFridge);
            recipe.setMatchPercentage(percentage);

            //beräkna 50% matchningen
            if (percentage >= 0.5) {
                matchingRecipes.add(recipe);
            } else if (percentage > 0.0) {
                suggestions.add(recipe);
            }

            System.out.println("Suggestion: " + recipe.getName() + " " + percentage);
            //samla 30 recept
            if (matchingRecipes.size() >= 30) break;
        }


        if (!matchingRecipes.isEmpty()) {
            matchingRecipes.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
            return matchingRecipes;
        }


        //inga recept över 50%, returnera förslag sorterade
        suggestions.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
        return suggestions.subList(0, Math.min(30, suggestions.size()));

    }


    //här kontrolleras att minst ett val gjorts per kategori.
    /**
     * Validates that every category in the map contains at least one selected ingredient.
     * @param categoryMap map of category names to lists of selected ingredients
     * @throws IllegalArgumentException if any category has no selection
     */
    private void validateAllCategories(Map<String, List<String>> categoryMap){
        for (String categoryName : categoryMap.keySet()){
            List<String> selections = categoryMap.get(categoryName);

            //om en sak inte valts per alla kategorier skickas felmeddelande.
            if (selections == null || selections.isEmpty()){
                throw new IllegalArgumentException("Du saknar val i kategorin : " + categoryName);
            }
        }
    }

    //här skapas lista med valda ingredienser och none exkluderas.
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

                    //HÄR görs ingredienser om till små bokstäver för att matcha api
                    //kolla senare om detta med understreck
                    allIngredients.add(ingredient.toLowerCase().trim());
                }
            }
        }
        return allIngredients;
    }

    //kontroll om receptets ursprung matchar användarens val av kök
    /**
     * Checks whether a recipe matches the user's selected cuisines.
     * Returns true if no cuisines are selected (no filter applied).
     * @param recipe the recipe to check
     * @param selectedCuisines list of cuisines to filter by
     * @return true if the recipe matches, false otherwise
     */
    private boolean isCorrectCuisine(Recipe recipe, List<Cuisine> selectedCuisines){
        //om användaren ej val kök godkänns alla recept
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

    //här sker beräkningen av receptets ingredienser som matchar anvädnarens val
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

            //kolla om något av användarens val finns i ingrediensnamnet
            for (String fridgeItem : userFridge){
                if (name.contains(fridgeItem) || fridgeItem.contains(name)){
                    matchCount++;

                    break;
                }
            }
        }
        return matchCount / recipeIngredients.size();
    }



    //recipe
    //gör om om objektet från ett modelobjekt till ett DTO-objekt
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

    //Mappar en hel lista av Recipe till en lista av DTO
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
