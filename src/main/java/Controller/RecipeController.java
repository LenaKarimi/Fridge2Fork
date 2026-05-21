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

public class RecipeController {

    //offline=false då körs csv
    //online=true då körs api
    private static final boolean IS_ONLINE = true ;

    //De olika klasserna som används från Api:et
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;
   //listan med recept som laddas in från csv när programmet är offline
    private final List<Recipe> localRecipes;

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
    private List<Recipe> searchFromApi(List<String> userFridge, List<Cuisine> selectedCuisines, List <Diet> selectedDiets) throws Exception {

        //Sökningen sker här för möjliga recept baserad på valda ingredienser
        Set<String> discoveredMealIds = new HashSet<>();
        List<Recipe> matchingRecipes = new ArrayList<>();

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
                    }
                    //samla 30 recept
                    if (matchingRecipes.size() >= 30) break;

                } catch (Exception e) {
                    System.out.println("debug: Hoppar över recept ID: " + mealSummary.idMeal + " pga fel: " + e.getMessage());
                }
            }
            if (matchingRecipes.size() >= 30) break;
        }

        matchingRecipes.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
        return matchingRecipes;
    }

    //söker recept lokalt från CSV-listan istället för API
    private List<Recipe> searchFromCsv(List<String> userFridge, List<Cuisine> selectedCuisines, List <Diet> selectedDiets) {
        List<Recipe> matchingRecipes = new ArrayList<>();

        for (Recipe recipe : localRecipes) {
            //filterring baserad på kök
            if (!isCorrectCuisine(recipe, selectedCuisines)) continue;

            if (!isCorrectDiet(recipe, selectedDiets)) continue;

            //detta beräknar procentmatchning
            double percentage = calculateMatchPercentage(recipe, userFridge);

            //beräkna 50% matchningen
            if (percentage < 0.5) continue;



            recipe.setMatchPercentage(percentage);
            matchingRecipes.add(recipe);

            //samla 30 recept
            if (matchingRecipes.size() >= 30) break;
        }

        matchingRecipes.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
        return matchingRecipes;
    }


    //här kontrolleras att minst ett val gjorts per kategor.
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
    private boolean isCorrectCuisine(Recipe recipe, List<Cuisine> selectedCuisines){
        //om användaren ej val kök godkänns alla recept
        if (selectedCuisines == null || selectedCuisines.isEmpty()) return true;
        return selectedCuisines.contains(recipe.getCuisine());
    }

    private boolean isCorrectDiet(Recipe recipe, List<Diet> selectedDiets){
        if (selectedDiets == null || selectedDiets.isEmpty()) return true;
        return selectedDiets.contains(recipe.getDiet());
    }

    //här sker beräkningen av receptets ingredienser som matchar anvädnarens val
    private double calculateMatchPercentage(Recipe recipe, List<String> userFridge){
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
