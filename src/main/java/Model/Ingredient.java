package Model;

public class Ingredient {

    private String name;
    private String measure;
    private Category category;

    public Ingredient(String name, String measure){
        this.name = name;
        this.measure = measure;
        this.category = Category.OTHER; //API definerar inte kategori
    }
    public String getName() {
        return name;
    }
    public String getMeasure() {
        return measure;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
}
