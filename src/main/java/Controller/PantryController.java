package Controller;

import Database.*;
import Model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PantryController {

    private final PantryItemDAO pantryItemDAO = new PantryItemDAO();


    // lägger till produkt, returenerar null om lyckat, felmedelande om problem
    public String addItem(int profileId, String name, String expiryDate) throws SQLException {
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
}
