package view;

import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.geometry.Pos;

public class RecipeView extends StackPane {

    public RecipeView(){
        //Bara en rubrik för att se att vyn skiftat
        Label title = new Label("Här hittar du dina recept");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold");

        this.getChildren().add(title);
        this.setAlignment(Pos.CENTER);

        //Tillfällif  bakgrundsfärg för att se vyskiftet
        this.setStyle("-fx-background-color: violet;");
    }
}
