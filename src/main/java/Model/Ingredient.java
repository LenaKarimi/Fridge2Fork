package Model;

/**
 * Represents an ingredient used in a recipe.
 * Contains name, measure and default category.
 * @author Intisaar
 * @author Lena
 * @author Racil
 */
public class Ingredient {
    private String name;
    private String measure;
    private Category category;

    /**
     * Default constructor.
     */
    public Ingredient() {
    }

    /**
     * Constructs an ingredient with the name and measurement.
     * The default category is set to Category.OTHER.
     * @param name the name of the ingredient
     * @param measure the amount or measurement of the ingredient
     */
    public Ingredient(String name, String measure){
        this.name = name;
        this.measure = measure;
        this.category = Category.OTHER; //API definerar inte kategori
    }

    /**
     * Returns the name of this ingredient.
     * @return the ingredient name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the measurement amount for this ingredient.
     * @return the measurement string
     */
    public String getMeasure() {
        return measure;
    }

    /**
     * Returns the food category this ingredient belongs to.
     * @return the Category enum value
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the food category for this ingredient.
     * Use this to override the default category after construction.
     * @param category the Category enum value to assign
     */
    public void setCategory(Category category) {
        this.category = category;
    }
}
