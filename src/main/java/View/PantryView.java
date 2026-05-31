package View;

import Controller.PantryController;
import Controller.UserController;
import DTO.PantryItemDTO;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javafx.scene.control.*;
import javafx.geometry.Insets;
import java.time.LocalDate;

/**
 * View for managing pantry ingredients.
 * Displays an input panel for adding ingredients a list of saved ingredients.
 * @author lena
 */
public class PantryView extends BorderPane {
    private PantryController pantryController;
    private UserController userController;
    private TextField nameField;
    private DatePicker datePicker;
    private Label informationLabel;
    private ListView<PantryItemDTO> list;

    /**
     * Constructs the PantryView and builds the UI.
     * @param pantryController handles pantry logic and database operations
     * @param userController provides the current logged in user
     */
    public PantryView(PantryController pantryController, UserController userController){
        this.pantryController = pantryController;
        this.userController = userController;

        this.setPadding(new Insets(20));

        nameField = new TextField();
        nameField.setPromptText("Ingredient name: ");

        datePicker = new DatePicker(LocalDate.now().plusDays(7));

        informationLabel = new Label();

        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e-> handleSave());

        VBox inputPanel = new VBox(10, new Label("Add ingredient: "),
                nameField, new Label("Expire date"), datePicker, saveBtn, informationLabel);

        inputPanel.setPadding(new javafx.geometry.Insets(10));
        inputPanel.setPrefWidth(250);

        nameField.setMaxWidth(Double.MAX_VALUE);
        datePicker.setMaxWidth(Double.MAX_VALUE);

        list = new ListView<>();
        list.setCellFactory(list-> new ListCell<>(){
            protected void updateItem (PantryItemDTO item, boolean empty){
                super.updateItem(item, empty);
                if (empty || item == null){
                    setText(null);
                }
                else {
                    setText(item.getName() + " - " + item.getExpiryDate());
                }
            }
        });

        Button deletBtn = new Button("Delet seletec item");
        deletBtn.setOnAction(e-> handleDelete());

        VBox panelList = new VBox(10, new Label("Pantry"), list, deletBtn);
        panelList.setPadding(new Insets(10));
        VBox.setVgrow(list, Priority.ALWAYS);

        HBox allContent = new HBox(20, inputPanel, panelList);
        HBox.setHgrow(panelList, Priority.ALWAYS);
        this.setCenter(allContent);
        refreshList();

    }

    /**
     * Handles saving a new ingredient for the pantry.
     * Validates input via the controller and updates the list on success.
     */
    private void handleSave(){
        int profilID = userController.getCurrentUser().getId();
        LocalDate localDate = datePicker.getValue();
        String date = null;
        if (localDate != null){
            date = localDate.toString();
        }
        try {
            String error = pantryController.addItem(profilID, nameField.getText(), date);
            if (error != null){
                informationLabel.setText(error);
            }
            else {
                informationLabel.setText("Saved!");
                nameField.clear();
                refreshList();
            }
        } catch (Exception e) {
            informationLabel.setText("Somethong went wrong");
        }
    }

    /**
     * Handles deleting the selected inredient from the pantry.
     * Does nothing if no item is selected.
     */
    private void handleDelete(){
        PantryItemDTO selected = list.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        pantryController.deleteItem(selected.getId());
        refreshList();
    }

    /**
     * Fetches all the pantry items form the database and updates the list view.
     */
    private void refreshList(){
        int profilID = userController.getCurrentUser().getId();
        list.getItems().setAll(pantryController.getPantryItems(profilID));
    }
}
