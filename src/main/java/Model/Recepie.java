package Model;

import java.util.List;

public class Recepie {

    private String id; //för att spara i vår egna databas
    private String name;
    private String instructions;
    private String imageUrl;
    private List<Ingredient> ingredients;
    private int totalServings;
    private int estimatedTime;
    private Diet diet; //frågan är om vi ska ha kvar detta? måste gruppera manuellt isf då det ej framgår i API?
    private Cuisine cuisine;

    public Recepie(String id, String name, String instructions, String imageUrl, List<Ingredient> ingredients, Cuisine cuisine){
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.imageUrl = imageUrl;
        this.ingredients = ingredients;
        this.cuisine = cuisine;
        this.totalServings = 0;
        this.estimatedTime = 0;
        this.diet = null;
    }
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getInstructions() {
        return instructions;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }
    public int getTotalServings() {
        return totalServings;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public Diet getDiet() {
        return diet;
    }


    //lägger bara set-metoder på det som vi själva behöver sätta på, det som ej sätts via API i denna konstruktor
    public void setTotalServings(int totalServings) {
        this.totalServings = totalServings;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }
}
