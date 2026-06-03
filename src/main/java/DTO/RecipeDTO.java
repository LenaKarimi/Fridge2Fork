package DTO;

import Model.Cuisine;
import Model.Ingredient;


import java.util.List;

/**
 * Data Transfer Object (DTO) used for transferring simplified recipe data
 * between layers of the application.
 * It contains the essential fields needed for display purposes.
 * @author Lena and Intisaar
 */
public class RecipeDTO {
    private String name;
    private String instructions;
    private String imageUrl;
    private List<Ingredient> ingredients;
    private Cuisine cuisine;
    private double matchPercentage;

    /**
     * Constructs a RecipeDTO with minimal display information.
     * @param name the name of the recipe
     * @param imageUrl the URL of the recipe image
     * @param instructions cooking instructions
     * @param ingredients list of ingredients with measures
     * @param matchPercentage how well this recipe matched the users fridge
     */
    public RecipeDTO(String name, String imageUrl, String instructions, List<Ingredient> ingredients, Cuisine cuisine, double matchPercentage ){
        this.name = name;
        this.imageUrl = imageUrl;
        this.instructions = instructions;
        this.cuisine = cuisine;
        this.matchPercentage = matchPercentage;
    }

    /**
     * Returns the recipe name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the image URL of the recipe.
     * @return the image URL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Returns the list of ingredients required by this recipe.
     * @return a list of Ingredient objects
     */
    public List<Ingredient> getIngredients() {return ingredients;}

    /**
     * Returns the cuisine type of this recipe.
     * @return the Cuisine enum value
     */
    public Cuisine getCuisine() { return cuisine;}

    /**
     * Returns the cooking instructions for this recipe.
     * @return the instructions as a plain text string
     */
    public String getInstructions() {return instructions;}

    /**
     * Returns the raw match percentage indicating how well this recipe matches the user's pantry.
     * @return the match percentage
     */
    public double getMatchPercentage(){ return matchPercentage;}

    /**
     * Returns the match percentage formatted as a whole-number percentage string for display in the UI.
     * For example, a match percentage of 0.75 returns "75%".
     * @return the formatted match percentage string
     */
    public String getMatchPercentageFormatted(){
        return (int) Math.round(matchPercentage * 100) + "%";
    }
}
