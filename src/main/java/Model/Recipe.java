package Model;

import java.util.List;

/**
 * Represents a recipe in the application domain.
 * Contains all information about a recipe including its ingredients,
 * cuisine type, dietary classification, and match percentage used for ranking.
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
     * Constructs a Recipe object with the core fields required for display.
     * Total servings, estimated time and diet default to 0/null and can be set separately.
     * @param id unique identifier of the recipe
     * @param name name of the recipe
     * @param instructions step-by-step cooking instructions
     * @param imageUrl URL pointing to the recipe image
     * @param ingredients list of ingredients required by the recipe
     * @param cuisine the cuisine type this recipe belongs to
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
     * Returns the unique identifier of the recipe.
     * @return the recipe ID
     */

    public String getId() {
        return id;
    }

    /**
     * Returns the name of the recipe.
     * @return the recipe name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the cooking instructions for the recipe.
     * @return the instructions as a plain text string
     */

    public String getInstructions() {
        return instructions;
    }

    /**
     * Returns the URL of the recipe's image.
     * @return the image URL, or null if none is set
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Returns the list of ingredients required by this recipe.
     * @return a list of Ingredient objects
     */
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * Returns the cuisine type of this recipe.
     * @return the {@link Cuisine} enum value
     */
    public Cuisine getCuisine() {
        return cuisine;
    }

    /**
     * Returns the dietary classification of this recipe.
     * @return the {@link Diet} enum value, or null if not set
     */
    public Diet getDiet() {
        return diet;
    }

    /**
     * Sets the dietary classification of this recipe.
     * @param diet the {@link Diet} enum value to assign
     */
    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    /**
     * Sets the cuisine type of this recipe.
     * @param cuisine the {@link Cuisine} enum value to assign
     */
    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    /**
     * Sets the match percentage used for ranking recipes against the user's pantry.
     * The value should be between 0.0 and 1.0, where 1.0 represents a full match.
     * @param matchPercentage the match percentage to assign
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
