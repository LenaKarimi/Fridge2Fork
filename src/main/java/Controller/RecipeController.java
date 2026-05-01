package Controller;

import Model.Recipe;
import Model.Ingredient;
import Model.Cuisine;
import TheMealDbAPI.MealMapper;
import TheMealDbAPI.MealRepository;
import TheMealDbAPI.HttpTheMealDbClient;
import DTO.*;
import TheMealDbAPI.TheMealDbDTO;

import java.util.*;

public class RecipeController {
    //hanterar logik kring recept, 70% gräns osv

    //De olika klasserna som används från Api:et
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;

    public RecipeController() {

        //initierar de klasser som behövs för att prata med api:et
        this.mealRepository = new MealRepository(new HttpTheMealDbClient());
        this.mealMapper = new MealMapper();

    }

    /**
     * Huvudmetod för att hitta recept baserat på användarens val av ingredienser
     * Tar emot en map med kategorier och en lista med önskad kök
     * (Kolla upp med kost och om det ska ens finnas kvar!!!!)
     */

    //Funktion för att ska en arraylist av de ingredienser som finns baserat på det primära ingrediensen
    public List<Recipe> searchRecipes(Map<String, List<String>> categoryMap, List <Cuisine> selectedCuisines) throws Exception {

        //Kontrollera att varje kategor i Gui har fått minst ett val none inkluderad.

        validateAllCategories(categoryMap);

        //samla alla valda ingredienser och rensa bort none valen inna vi skickar vidar
        List <String> userFridge = extractAllingredients(categoryMap);

        //Sökningen sker här för möjliga recept baserad på valda ingredienser
        Set<String> discoveredMealIds = new HashSet<>();
        List<Recipe> matchingRecipes = new ArrayList<>();

        for(String ingredient : userFridge){
            //här sker förfrågningen om specifik ingrediens
            List<TheMealDbDTO> apiResponse = mealRepisotory.getMealsByIngredient(ingredient);
             //om inte ingrediens finns ska programmet fortsätta till nästa ignrediens och inte krascha
            if (apiRespone == null) continue;

            for (TheMealDbDTO mealSummary : apiRespone){
                //sparar inte recept som redan är hämtade.
                if (discoveredMealIds.contains(mealSummary.idMeal)) continue;
                discoveredMealIds.add(mealSummary.idMeal);

                //hämta fullständiga detaljer dvs ingredienser, isntruktioner osv.
                TheMealDbDTO detailedMeal = mealRepository.getMealById(mealSummary.idMeal);
                Recipe recipeObject = mealMapper.toDomain(detailedMeal);

                //filterring baserad på kök
                if (!isCorrectCuisine(recipeObject, selectedCuisines)) continue;

                //beräkna 50% matchningen
                if (calculateMatchPercentage(recipeObject, userFridge) >= 0.5){
                    matchingRecipes.add(recipeObject);
                }
                if (matchingRecipes.size() >= 4 return matchingRecipes);


            }
        }
        return matchingRecipes;
    }

    //här kontrolleras att minst ett val gjorts per kategor.
    private void validateAllCategories(Map<String, List<String>> categoryMap){
        for (String categoryName : categoryMap.keySet()){
            List<String> selections = categoryMap.get(categoryName);

            //om en sak inte valts per alla kategorier skickas felmeddelande.
            if (selections == null || selection.isEmpty()){
                throw new IllegalArgumentException("Du saknar val i kategorin : " + categoryname);
            }
        }
    }

    //här skapas lista med valda ingredienser och none exkluderas.
    private List<String> extractAllIngredients(Map<String, List<String>> categoryMap){
        List <String> allIngredients = new ArrayList<>();
        for ( List<String> ingredientList : categorMap.valus()){
            for (String ingredient : ingredientList){
                if (!ingredient.equalsIgnoreCase("none")){

                    //HÄR görs ingredienser om till små bokstäver för att matcha api
                    //kolla senare om detta med understreck
                    allIngredients.add(ingredient.toLowerCase());
                }
            }
        }
        return allIngredients;
    }

    //kontroll om receptets ursprung matchar användarens val av kök
    private boolean isCorrectCuisine(Recipe recipe, List<Cuisine> selectedCuisines){
        //om användaren ej val kök godkänns alla recept
        //ska man ha ett val för none kanske??
        if (selectedCuisines == null || selectedCusines.isEmpty()) return true;
        return selectedCuisines.contains(recipe.getCuisine());
    }

    //här sker beräkningen av receptets ingredienser som matchar anvädnarens val
    private double calculateMatchPercentage(Recipe recipe, List<String> userFride){
        double matchCount = 0;
        List<Ingredient> recipeIngredients = recipe.getIngredients();

        for (Ingredient ing : recipeIngredients){
            String name = ing.getName().toLowerCase();

            if (userFridge.contains(name)){
                matchCount++;
            }
        }
        return matchCount / recipeIngredients.size();
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

    public List<RecipeDTO> getFilteredRecipes (List<String> selectedIngredients) throws Exception{
        //Om listan är tom som visas i guit
        if (selectedIngredients == null || selectedIngredients.isEmpty()){
            throw new IllegalArgumentException("Vänligen välj minst en ingrediens innan du går vidare!");
        }

        //hämta domänobjekt
        List<Recipe> allFoundRecipes = searchRecipesByIngredients(selectedIngredients);

        //kontrollera om api:et hittade något
        if (allFoundRecipes == null || allFoundRecipes.isEmpty()){
            return new ArrayList<>();
        }

        //konvertera domänobjekt till dtoer som guit kan hanter
        return getRecipeDTOList(allFoundRecipes);

    }








}
