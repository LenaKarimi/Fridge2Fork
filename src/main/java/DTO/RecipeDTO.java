package DTO;

import Controller.RecipeController;
import Model.Cuisine;
import Model.Ingredient;
import Model.Recipe;

import java.util.List;

/**
 * Data Transfer Object (DTO) used for transferring simplified recipe data
 * between layers of the application.
 * It contains the essential fields needed for display purposes.
 * @author Lena
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

    public List<Ingredient> getIngredients() {return ingredients;}

    public Cuisine getCuisine() { return cuisine;}

    public String getInstructions() {return instructions;}

    public double getMatchPercentage(){ return matchPercentage;}

    public String getMatchPercentageFormatted(){
        return (int) Math.round(matchPercentage * 100) + "%";
    }
}
