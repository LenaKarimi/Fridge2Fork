package Controller;

import DTO.PantryItemDTO;
import Database.*;
import Model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller responsible for managing the user's pantry items.
 * Handles adding, retrieving and deleting ingredients with expiry dates.
 * @author Racil
 */

public class PantryController {

    private final PantryItemDAO pantryItemDAO = new PantryItemDAO();


    // lägger till produkt, returenerar null om lyckat, felmedelande om problem
    /**
     * Adds a new pantry item for a given user.
     * Validates that both name and expiry date are provided and that the date is not in the past.
     * @param profileId the ID of the user
     * @param name the name of the ingredient
     * @param expiryDate the expiry date as a string in ISO format (yyyy-MM-dd)
     * @return null if successful, or an error message string if validation fails
     */
    public String addItem(int profileId, String name, String expiryDate) {
        if(name == null || name.isBlank()) {
            return "Write a product name";
        }

        if(expiryDate == null || expiryDate.isBlank()) {
            return "Write a expiry date";
        }

        try {
            LocalDate date = LocalDate.parse(expiryDate);

            if(date.isBefore(LocalDate.now())) {
                return "Expiry date cannot be in the past";
            }

            pantryItemDAO.addItem(profileId, name, date);
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    //hämtar alla giltiga produkter och rensar det passerade
    /**
     * Returns all valid pantry items for a given user.
     * Expired items are automatically removed before returning the list.
     * @param profileId the ID of the user
     * @return list of pantry item DTOs, or an empty list if none found
     */
    public List<PantryItemDTO> getPantryItems(int profileId) {
        try {
            pantryItemDAO.deleteExpiredItems(profileId); // resnar utgångna produkter
            List<PantryItem> pantryItems = pantryItemDAO.getPantryItems(profileId);
            return mapToDTOList(pantryItems);

        } catch (SQLException e) {
           e.printStackTrace();
           return new ArrayList<>();
        }
    }

    //raderar en produkt från databasen
    /**
     * Deletes a specific pantry item from the database.
     * @param itemid the ID of the item to delete
     */
    public void deleteItem(int itemid) {
        try {
            pantryItemDAO.deletePantryItem(itemid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // mappar en item till en dto
    /**
     * Converts a PantryItem domain object to a PantryItemDTO for use in the GUI.
     * @param pantryItem the pantry item to convert
     * @return a PantryItemDTO with id, name and formatted expiry date
     */
    private PantryItemDTO mapToDTO(PantryItem pantryItem) {
        return new PantryItemDTO(
                pantryItem.getId(),
                pantryItem.getName(),
                pantryItem.getExpiryDate().toString() // snyggar till datumets formatering
        );
    }

    //mappar en lista av items till en lista av dtos, använder MapToDTO
    /**
     * Converts a list of PantryItem objects to a list of PantryItemDTOs.
     * @param pantryItems the list of pantry items to convert
     * @return list of PantryItemDTOs
     */
    private List<PantryItemDTO> mapToDTOList(List<PantryItem> pantryItems) {
        List<PantryItemDTO> pantryItemDTOSList = new ArrayList<>();
        for(PantryItem pantryItem : pantryItems) { // loopar listan
            pantryItemDTOSList.add(mapToDTO(pantryItem)); // anroppar ovanstående metod
        }
        return pantryItemDTOSList;
    }
}
