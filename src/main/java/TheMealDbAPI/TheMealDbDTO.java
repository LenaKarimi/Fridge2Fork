package TheMealDbAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Data transfer object (DTO) representing a meal from TheMealDB API.
 * It maps the JSON structure returned by the API into java objects.
 * Each field corresponds directly to a property in the API response.
 * It provides helper methods for accessing ingredients and their corresponding measurements by index.
 * @author Lena
 */
@JsonIgnoreProperties (ignoreUnknown = true) //ignores unknown JSON fields
public final class TheMealDbDTO {
    public String idMeal;
    public String strMeal;
    public String strCategory;
    public String strArea;
    public String strInstructions;
    public String strMealThumb;
    public String strYoutube;
    public String strSource;

    public String strIngredient1;
    public String strIngredient2;
    public String strIngredient3;
    public String strIngredient4;
    public String strIngredient5;
    public String strIngredient6;
    public String strIngredient7;
    public String strIngredient8;
    public String strIngredient9;
    public String strIngredient10;
    public String strIngredient11;
    public String strIngredient12;
    public String strIngredient13;
    public String strIngredient14;
    public String strIngredient15;
    public String strIngredient16;
    public String strIngredient17;
    public String strIngredient18;
    public String strIngredient19;
    public String strIngredient20;

    public String strMeasure1;
    public String strMeasure2;
    public String strMeasure3;
    public String strMeasure4;
    public String strMeasure5;
    public String strMeasure6;
    public String strMeasure7;
    public String strMeasure8;
    public String strMeasure9;
    public String strMeasure10;
    public String strMeasure11;
    public String strMeasure12;
    public String strMeasure13;
    public String strMeasure14;
    public String strMeasure15;
    public String strMeasure16;
    public String strMeasure17;
    public String strMeasure18;
    public String strMeasure19;
    public String strMeasure20;

    /**
     * Returns the ingredient index
     * @param index the ingredient index (between 1-20)
     * @return the ingredient at the given index, or null if not present
     */
    public String getIngredient (int index){
        return switch (index){
            case 1 -> strIngredient1;
            case 2 -> strIngredient2;
            case 3 -> strIngredient3;
            case 4 -> strIngredient4;
            case 5 -> strIngredient5;
            case 6 -> strIngredient6;
            case 7 -> strIngredient7;
            case 8 -> strIngredient8;
            case 9 -> strIngredient9;
            case 10 -> strIngredient10;
            case 11 -> strIngredient11;
            case 12 -> strIngredient12;
            case 13 -> strIngredient13;
            case 14 -> strIngredient14;
            case 15 -> strIngredient15;
            case 16 -> strIngredient16;
            case 17 -> strIngredient17;
            case 18 -> strIngredient18;
            case 19 -> strIngredient19;
            case 20 -> strIngredient20;
            default -> throw new IllegalArgumentException("Index must be in a range 1-20");
        };
    }

    /**
     * Returns the measurement corresponding to the given ingredient index
     * @param index the measurement index (between 1-20)
     * @return the measurment at the given index, or null if not present
     */
    public String getMeasure (int index){
        return switch (index){
            case 1 -> strMeasure1;
            case 2 -> strMeasure2;
            case 3 -> strMeasure3;
            case 4 -> strMeasure4;
            case 5 -> strMeasure5;
            case 6 -> strMeasure6;
            case 7 -> strMeasure7;
            case 8 -> strMeasure8;
            case 9 -> strMeasure9;
            case 10 -> strMeasure10;
            case 11 -> strMeasure11;
            case 12 -> strMeasure12;
            case 13 -> strMeasure13;
            case 14 -> strMeasure14;
            case 15 -> strMeasure15;
            case 16 -> strMeasure16;
            case 17 -> strMeasure17;
            case 18 -> strMeasure18;
            case 19 -> strMeasure19;
            case 20 -> strMeasure20;
            default -> throw new IllegalArgumentException("Index must be in a range 1-20");
        };
    }
}