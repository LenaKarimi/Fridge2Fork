package TheMealDbAPI;

import Model.Cuisine;
import Model.Ingredient;
import Model.Recipe;

import java.util.ArrayList;
import java.util.List;

// denna klass tar emot rå API datai form av TheMealDbDTO, gör om det till ett recept objekt
// loopar igenom alla ingredienser,lägger till den i en lista - gjord av ingrediens objekt, och ändrar land till cuisine-enum
public class MealMapper {

    public Recipe toDomain(TheMealDbDTO theMealDbDTO){

        //ingredienslistan
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

        return new Recipe(theMealDbDTO.idMeal, theMealDbDTO.strMeal, theMealDbDTO.strInstructions,theMealDbDTO.strMealThumb, recipeIngredients, cuisine);
    }

    private Cuisine mapCuisin(String strArea){

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
            case "Venezulan" -> Cuisine.Venezulan;
            case "Vietnamese" -> Cuisine.Vietnamese;
            default -> null;
        };
    }
}
