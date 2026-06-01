package Model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a single item stored in a user's pantry.
 * Tracks the ingredient name, its owner profile and its expiry date.
 * @author Racil
 */
public class PantryItem {

    private int id;
    private int profileId;
    private String name;
    private LocalDate expiryDate;

    /**
     * Constructs a PantryItem with all fields.
     * @param id the unique identifier of this pantry item
     * @param profileId the ID of the user profile this item belongs to
     * @param name the name of the ingredient or food item
     * @param expiryDate the date on which the item expires
     */
    public PantryItem(int id, int profileId, String name, LocalDate expiryDate) {
        this.id = id;
        this.profileId = profileId;
        this.name = name;
        this.expiryDate = expiryDate;
    }

    /**
     * Returns the unique identifier of this pantry item.
     * @return the pantry item ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the ID of the user profile this pantry item belongs to.
     * @return the profile ID
     */
    public int getProfileId() {
        return profileId;
    }

    /**
     * Returns the name of the ingredient or food item.
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the expiry date of this pantry item.
     * @return the expiry date as a {@link LocalDate}
     */
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}
