package View;

import App.Fridge2ForkApp;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;

public class HomeView extends StackPane {

    public HomeView(){
        Label welcome = new Label("Välkommen xx!");
        welcome.setStyle("-fx-font-size: 20px");

        Label subText = new Label("Upptäck recept baserade på vad du redan har hemma");
        subText.setStyle("-fx-font-size: 16px;");

        //Knappen
        Button BSubText = new Button("Vad har du hemma?");
        BSubText.setStyle("-fx-font-size: 14px; -fx-padding: 10 20 10 20;");
        BSubText.setCursor(javafx.scene.Cursor.HAND);
        BSubText.setOnAction(e ->{
            Fridge2ForkApp.root.setCenter(new FridgeView());
        });

        //Allt i   en VBox så det staplas på höjden
        VBox content = new VBox(20, welcome, subText, BSubText);
        content.setAlignment(Pos.CENTER);

        this.getChildren().add(content);
        this.setAlignment(Pos.CENTER);
    }
}
