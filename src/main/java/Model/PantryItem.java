package Model;

import java.time.LocalTime;

public class PantryItem {

    private int id;
    private int profileId;
    private String name;
    private LocalTime expiryDate;

    public PantryItem(int id, int profileId, String name, LocalTime expiryDate) {
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

    public LocalTime getExpiryDate() {
        return expiryDate;
    }
}
