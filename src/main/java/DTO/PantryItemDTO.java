package DTO;

/**
 * Data Transfer Object for a pantry item.
 * Used to pass pantry data between the database layer and the view.
 * @author Racil
 */
public class PantryItemDTO {
    private int id;
    private String name;
    private String expiryDate;

    /**
     * Conxtructs a PantryItemDTO.
     * @param id the unique id of the pantry
     * @param name the name of the ingredient
     * @param expiryDate the expiry date as a formatted string
     */
    public PantryItemDTO(int id, String name, String expiryDate) {
        this.id = id;
        this.name = name;
        this.expiryDate = expiryDate;
    }

    /**
     * Returns the unique ID of this pantry item.
     * @return the pantry item ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the ingredient or food item.
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the expiry date of this pantry item as a formatted string.
     * @return the expiry date string
     */
    public String getExpiryDate() {
        return expiryDate;
    }
}
