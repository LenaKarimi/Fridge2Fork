package DTO;

public class PantryItemDTO {
    private int id;
    private String name;
    private String expiryDate;

    public PantryItemDTO(int id, String name, String expiryDate) {
        this.id = id;
        this.name = name;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
