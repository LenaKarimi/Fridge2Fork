package Controller;

import DTO.PantryItemDTO;
import Database.*;
import Model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PantryController {

    private final PantryItemDAO pantryItemDAO = new PantryItemDAO();


    // lägger till produkt, returenerar null om lyckat, felmedelande om problem
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
    public void deleteItem(int itemid) {
        try {
            pantryItemDAO.deletePantryItem(itemid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // mappar en item till en dto
    private PantryItemDTO mapToDTO(PantryItem pantryItem) {
        return new PantryItemDTO(
                pantryItem.getId(),
                pantryItem.getName(),
                pantryItem.getExpiryDate().toString() // snyggar till datumets formatering
        );
    }

    //mappar en lista av items till en lista av dtos, använder MapToDTO
    private List<PantryItemDTO> mapToDTOList(List<PantryItem> pantryItems) {
        List<PantryItemDTO> pantryItemDTOSList = new ArrayList<>();
        for(PantryItem pantryItem : pantryItems) { // loopar listan
            pantryItemDTOSList.add(mapToDTO(pantryItem)); // anroppar ovanstående metod
        }
        return pantryItemDTOSList;
    }
}
