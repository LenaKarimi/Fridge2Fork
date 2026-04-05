package Model;

import java.util.List;

public class Recipe {

    private String name;
    private List<Ingredient> ingredients;
    private int totalServings;
    private int estimatedTime;
    private Diet diet;
    private Cuisine cuisine;

    public Recipe(String name, List<Ingredient> ingredients, int totalServings, int estimatedTime, Cuisine cuisine, Diet diet) {
        this.name = name;
        this.ingredients = ingredients;
        this.totalServings = totalServings;
        this.estimatedTime = estimatedTime;
        this.diet = diet;
        this.cuisine = cuisine;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public int getTotalServings() {
        return totalServings;
    }

    public void setTotalServings(int totalServings) {
        this.totalServings = totalServings;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }
}
