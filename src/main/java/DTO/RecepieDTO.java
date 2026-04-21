package DTO;

import Controller.RecipeController;
import Model.Ingredient;
import Model.Recepie;

import java.util.List;

public class RecepieDTO {
    private RecipeController controller;
    private String name;
    private String instructions;
    private String imageUrl;
    private List<Ingredient> ingredients;

    public RecepieDTO(String name, String imageUrl ){
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
