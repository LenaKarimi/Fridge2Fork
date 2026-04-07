package View;

import App.Fridge2ForkApp;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class DietView extends StackPane {

    public DietView(){
        Label dietTitle = new Label("Dietary preferences");
        dietTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label kitchenTitle = new Label("Cuisine type");
        kitchenTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Button findRecipesBtn = new Button("Find Recipes");
        findRecipesBtn.setStyle("-fx-font-size: 16px; -fx-padding: 12 40; -fx-background-color: darkseagreen; -fx-text-fill: white; -fx-font-weight: bold;");
        findRecipesBtn.setCursor(javafx.scene.Cursor.HAND);
        findRecipesBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new RecipeResultsView());
        });

        //Kryssrutor för kosten
        CheckBox vegetarian = new CheckBox("Vegetarian");
        CheckBox vegan = new CheckBox("Vegan");
        CheckBox glutenFree = new CheckBox("Gluten free");
        CheckBox lactoseFree = new CheckBox("Lactose free");
        CheckBox extraProtein = new CheckBox("Extra protein");
        CheckBox lowCarb = new CheckBox("Low carb");

        CheckBox swedish = new CheckBox("Swedish home cooking");
        CheckBox italian = new CheckBox("Italian");
        CheckBox asian = new CheckBox("Asian");
        CheckBox middleEastern = new CheckBox("Middle eastern");

        //Stilen på alla kryssrutor
        String checkStyle = "-fx-font-size: 16px;";
        vegetarian.setStyle(checkStyle);
        vegan.setStyle(checkStyle);
        glutenFree.setStyle(checkStyle);
        lactoseFree.setStyle(checkStyle);
        swedish.setStyle(checkStyle);
        italian.setStyle(checkStyle);
        asian.setStyle(checkStyle);
        middleEastern.setStyle(checkStyle);
        extraProtein.setStyle(checkStyle);
        lowCarb.setStyle(checkStyle);

        //Lägg allt i VBox
        VBox content = new VBox(15, dietTitle, vegetarian, vegan, glutenFree, lactoseFree, kitchenTitle, swedish,
                italian, asian, middleEastern, extraProtein, lowCarb, findRecipesBtn);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(40));

        this.getChildren().add(content);
    }
}
