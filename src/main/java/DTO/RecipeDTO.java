package DTO;

import Controller.RecipeController;
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
    private RecipeController controller;
    private String name;
    private String instructions;
    private String imageUrl;
    private List<Ingredient> ingredients;

    /**
     * Constructs a RecipeDTO with minimal display information.
     * @param name the name of the recipe
     * @param imageUrl the URL of the recipe image
     */
    public RecipeDTO(String name, String imageUrl ){
        this.name = name;
        this.imageUrl = imageUrl;
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
}
