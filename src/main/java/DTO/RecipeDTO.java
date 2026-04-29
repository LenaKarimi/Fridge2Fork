package DTO;

import Controller.RecipeController;
import Model.Ingredient;
import Model.Recipe;

import java.util.List;

public class RecipeDTO {
    private RecipeController controller;
    private String name;
    private String instructions;
    private String imageUrl;
    private List<Ingredient> ingredients;

    public RecipeDTO(String name, String imageUrl ){
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
