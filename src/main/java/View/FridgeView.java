package View;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import App.Fridge2ForkApp;

public class FridgeView extends StackPane {

    public FridgeView(){
        Label title = new Label("Vad har du hemma?");
        title.setStyle("-fx-font-sixe: 28px; -fx-font-weight-bold;");

        //Kryssrutorna
        CheckBox kolhydrater = new CheckBox("Kolhydrater");
        CheckBox protein = new CheckBox("Protein");
        CheckBox gronsaker = new CheckBox("Grönsaker");
        CheckBox mejeriprodukter = new CheckBox("Mejeriprodukter");
        CheckBox frukt = new CheckBox("Frukt");
        CheckBox skafferi = new CheckBox("Skafferi");

        //Stil på rutorna
        String checkStyle = "-fx-font-size: 16px;";
        kolhydrater.setStyle(checkStyle);
        protein.setStyle(checkStyle);
        gronsaker.setStyle(checkStyle);
        mejeriprodukter.setStyle(checkStyle);
        frukt.setStyle(checkStyle);
        skafferi.setStyle(checkStyle);

        //Nästa-knapp
        Button nextButton = new Button("Nästa");
        nextButton.setStyle("-fx-font-size: 14px; -fx-padding: 10 20 10 20;");
        nextButton.setCursor(javafx.scene.Cursor.HAND);
        nextButton.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new DietView());
        });

        //Lägg allt i VBox
        VBox content = new VBox(20, title, kolhydrater, protein, gronsaker, mejeriprodukter, frukt, skafferi,
                nextButton);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(40));

        this.getChildren().add(content);

    }
}
