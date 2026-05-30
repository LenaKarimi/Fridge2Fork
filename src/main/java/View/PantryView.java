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

public class PantryView extends BorderPane {
    private PantryController pantryController;
    private UserController userController;
    private TextField nameField;
    private DatePicker datePicker;
    private Label informationLabel;
    private ListView<PantryItemDTO> list;


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
                nameField, new Label("Expire date", datePicker, saveBtn, informationLabel);

        inputPanel.setPadding(new javafx.geometry.Insets(10));
        inputPanel.setPrefWidth(250);

        list = new ListView<>();
        list.setCellFactory(list-> new ListCell<>(){
            protected void updateItem (PantryItemDTO item, boolean empty){
                super.updateItem(item, empty);
                setText("");
            }
        });

        Button deletBtn = new Button("Delet seletec item");
        deletBtn.setOnAction(e-> handleDelete());

        VBox panelList = new VBox(10, new Label("Pantry"), list, deletBtn);
        panelList.setPadding(new Insets(10));
        VBox setVgrow(list, Priority.ALWAYS);

        HBox allContent = new HBox(20, inputPanel, list);
        HBox.setHgrow(panelList, Priority.ALWAYS);
        this.setCenter(content);

    }
}
