package TheMealDbAPI;

import Model.*;
import Model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps API data from TheMealDbDTO into domain objects
 * It is responsible for transforming raw data from the API into
 * the application models (Recipe, ingredient, Cuisine, Diet).
 * @author Lena
 */
public class MealMapper {

    /**
     * Converts a TheMealDbDTO oject into a Recipe object.
     * It extracts ingredients and their measurements,
     * builds a list of ingredient objects
     * and maos cuisine and diet values to enums
     * @param theMealDbDTO the API data transfer object
     * @return a fully conctructed Recioe object
     */
    public Recipe toDomain(TheMealDbDTO theMealDbDTO){
        List<Ingredient> recipeIngredients = new ArrayList<>();
        for (int i = 1; i<=20; i++){

            String ingredientName = theMealDbDTO.getIngredient(i);
            String ingredientMeasure = theMealDbDTO.getMeasure(i);

            if (ingredientName == null || ingredientName.isBlank()){
                continue;
            }
            Ingredient ingredient = new Ingredient(ingredientName, ingredientMeasure);
            recipeIngredients.add(ingredient);
        }
        Cuisine cuisine = mapCuisin(theMealDbDTO.strArea);
        Diet diet = mapDiet(theMealDbDTO.strCategory);

        Recipe recipe = new Recipe(theMealDbDTO.idMeal, theMealDbDTO.strMeal, theMealDbDTO.strInstructions,theMealDbDTO.strMealThumb, recipeIngredients, cuisine);
        recipe.setDiet(diet);
        return recipe;
    }

    /**
     * Maps a category string from the API to a Diet enum.
     * @param strCategory the category string from the API
     * @return the corresponding Diet or null if not applicable
     */
    private Diet mapDiet(String strCategory){
        if (strCategory == null){
            return null;
        }
        return switch (strCategory){
            case "Vegan" -> Diet.VEGAN;
            case "Vegetarian" -> Diet.VEGETARIAN;
            default -> null;
        };
    }

    /**
     * Maps an area string from the API to a Cuisine enum.
     * @param strArea the area string from the API
     * @return the corresponding Cuisine or null if no much is found
     */
    private Cuisine mapCuisin(String strArea){
        if (strArea == null){
            return null;
        }

        return switch (strArea){
            case "Algerian"-> Cuisine.Algerian;
            case "American" -> Cuisine.American;
            case "Argentinian" -> Cuisine.Argentinian;
            case "British" -> Cuisine.British;
            case "Canadian" -> Cuisine.Canadian;
            case "Chinese" -> Cuisine.Chinese;
            case "Croatian" -> Cuisine.Croatian;
            case "Dutch" -> Cuisine.Dutch;
            case "Egyptian" -> Cuisine.Egyptian;
            case "Filipino" -> Cuisine.Filipino;
            case "French" -> Cuisine.French;
            case "Greek" -> Cuisine.Greek;
            case "Indian" -> Cuisine.Indian;
            case "Irish" -> Cuisine.Irish;
            case "Italian" -> Cuisine.Italian;
            case "Jamaican" -> Cuisine.Jamaican;
            case "Japanese" -> Cuisine.Japanese;
            case "Kenyan" -> Cuisine.Kenyan;
            case "Malaysian" -> Cuisine.Malaysian;
            case "Mexican" -> Cuisine.Mexican;
            case "Moroccan" -> Cuisine.Moroccan;
            case "Norwegian" -> Cuisine.Norwegian;
            case "Polish" -> Cuisine.Polish;
            case "Portuguese" -> Cuisine.Portuguese;
            case "Russian" -> Cuisine.Russian;
            case "SaudiArabian" -> Cuisine.SaudiArabian;
            case "Slovakian" -> Cuisine.Slovakian;
            case "Spanish" -> Cuisine.Spanish;
            case "Syrian" -> Cuisine.Syrian;
            case "Thai"-> Cuisine.Thai;
            case "Tunisian" -> Cuisine.Tunisian;
            case "Turkish" -> Cuisine.Turkish;
            case "Ukrainian" -> Cuisine.Ukrainian;
            case "Uruguayan" -> Cuisine.Uruguayan;
            case "Venezuelan" -> Cuisine.Venezuelan;
            case "Vietnamese" -> Cuisine.Vietnamese;
            default -> null;
        };
    }
}
