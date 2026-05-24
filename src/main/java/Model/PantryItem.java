package Model;

import java.time.LocalDate;
import java.time.LocalTime;

public class PantryItem {

    private int id;
    private int profileId;
    private String name;
    private LocalDate expiryDate;

    public PantryItem(int id, int profileId, String name, LocalDate expiryDate) {
        this.id = id;
        this.profileId = profileId;
        this.name = name;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public int getProfileId() {
        return profileId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }
}
