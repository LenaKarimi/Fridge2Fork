package Model;

import java.util.List;

public class Recipe {

    private String name;
    private List<Ingredient> ingredients;
    private int totalServings;
    private int estimatedTime;

    public Recipe(String name, List<Ingredient> ingredients, int totalServings, int estimatedTime) {
        this.name = name;
        this.ingredients = ingredients;
        this.totalServings = totalServings;
        this.estimatedTime = estimatedTime;
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
}
