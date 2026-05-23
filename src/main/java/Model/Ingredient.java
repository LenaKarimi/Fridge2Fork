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
    public String getMeasure() {
        return measure;
    }

    /**
     *
     * @return
     */
    public Category getCategory() {
        return category;
    }

    /**
     *
     * @param category
     */
    public void setCategory(Category category) {
        this.category = category;
    }
}
