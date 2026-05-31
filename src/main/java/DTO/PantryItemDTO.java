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
     *
     * @return
     */
    public int getId() {
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
    public String getExpiryDate() {
        return expiryDate;
    }
}
