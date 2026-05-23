package Model;

import java.util.List;

/**
 * Represents a recipe in the application domain.
 * Contains information about recipe.
 * @author Intisaar
 * @author Lena
 */
public class Recipe{
    private String id;
    private String name;
    private String instructions;
    private String imageUrl;
    private List<Ingredient> ingredients;
    private int totalServings;
    private int estimatedTime;
    private Diet diet;
    private Cuisine cuisine;
    private double matchPercentage;

    /**
     * Constructs a Recipe object.
     * @param id unique identifier od the recipe
     * @param name name of the recipe
     * @param instructions cooking instructions
     * @param imageUrl URL to the recipe image
     * @param ingredients list of ingredients
     * @param cuisine cuisine type
     */
    public Recipe(String id, String name, String instructions, String imageUrl, List<Ingredient> ingredients, Cuisine cuisine){
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

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     *
     * @return
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     *
     * @return
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     *
     * @return
     */
    public Cuisine getCuisine() {
        return cuisine;
    }

    /**
     *
     * @return
     */
    public Diet getDiet() {
        return diet;
    }

    /**
     *
     * @param diet
     */
    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    /**
     *
     * @param cuisine
     */
    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    /**
     * Sets the match percentage used for ranking recipes
     * @param matchPercentage the match percentage (0-100)
     */
    public void setMatchPercentage(double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    /**
     * Returns the match percentage used for ranking.
     * @return the match percentage
     */
    public double getMatchPercentage() {
        return matchPercentage;
    }
}
