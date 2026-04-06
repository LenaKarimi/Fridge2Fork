package View;

import App.Fridge2ForkApp;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;


public class FridgeView extends StackPane {

    public FridgeView(){
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(40));
        mainContent.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("What's in your fridge?");
        title.setStyle("-fx-font-sixe: 18px; -fx-font-weight-bold; -fx-text-fill: black;");
        mainContent.getChildren().add(title);

        //Kolhydrater
        mainContent.getChildren().add(createCategorySection("Carbohydrates", "Pasta", "Rice", "Potato", "Sweet potato",
                "Bread", "Couscous", "Bulgur", "None"));


        //Protein
        mainContent.getChildren().add(createCategorySection("Protein", "Chicken", "Beef", "Pork", "Eggs", "Tofu",
                "Salmon", "Canned tuna", "Shrimp", "None"));


        //Grönsaker
        mainContent.getChildren().add(createCategorySection("Vegetables", "Broccoli", "Carrot", "Spinach", "Tomato",
                "Onion", "Bell pepper", "Lettuce", "Corn", "Cucumber", "None"));


        //Mejeri
        mainContent.getChildren().add(createCategorySection("Dairy", "Milk", "Cheese", "Butter", "Sour cream",
                "Creme Fraîche", "None"));


        //Skafferi
        mainContent.getChildren().add(createCategorySection("Pantry", "Olive oil", "Garlic", "Canned tomatoes",
                "Chickpeas", "Lentils", "Nuts", "None"));


        //Nästa-knappen
        Button nextButton = new Button("Next step");
        nextButton.setStyle("-fx-font-sixe; 16px -fx-padding: 12 40; -fx-background-color: darkseagreen;" +
                "fx-text-fill: white; -fx-font-weight: bold;");
        nextButton.setCursor(javafx.scene.Cursor.HAND);
        nextButton.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new DietView());
        });

        mainContent.getChildren().add(nextButton);

        //ScrollPane om listan blir lite för lång
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        this.getChildren().add(scrollPane);
    }
        //"..." betyder att metoden tar emot hur många strängar som helst
    private VBox createCategorySection(String categoryName, String...ingredients) {
        VBox section = new VBox(10);
        Label catLabel = new Label(categoryName);
        catLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: darkseagreen");
        FlowPane ingredientFlow = new FlowPane(15, 10);

        for (String ingredient : ingredients) {
            CheckBox cb = new CheckBox(ingredient);
            cb.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

            ingredientFlow.getChildren().add(cb);
        }

        section.getChildren().addAll(catLabel, ingredientFlow);
        section.setPadding(new Insets(0, 0, 20, 0));
        return section;
    }
}
