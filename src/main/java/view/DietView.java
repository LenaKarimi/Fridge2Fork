package view;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class DietView extends StackPane {

    public DietView(){
        Label dietTitle = new Label("Önskad kost");
        dietTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label kitchenTitle = new Label("Önskat kök");
        kitchenTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        //Kryssrutor för  kosten
        CheckBox vegetarisk = new CheckBox("Vegetarisk");
        CheckBox vegan = new CheckBox("Vegan");
        CheckBox glutenfri = new CheckBox("Glutenfri");
        CheckBox laktosfri = new CheckBox("Laktosfri");

        CheckBox svenskt = new CheckBox("Svensk husman");
        CheckBox italienskt = new CheckBox("Italienskt");
        CheckBox asiatiskt = new CheckBox("Asiatiskt");
        CheckBox mellanOstern = new CheckBox("Orientaliskt");

        //Stilen på alla kryssrutor
        String checkStyle = "-fx-font-size: 16px;";
        vegetarisk.setStyle(checkStyle);
        vegan.setStyle(checkStyle);
        glutenfri.setStyle(checkStyle);
        laktosfri.setStyle(checkStyle);
        svenskt.setStyle(checkStyle);
        italienskt.setStyle(checkStyle);
        asiatiskt.setStyle(checkStyle);
        mellanOstern.setStyle(checkStyle);

        //Lägg allt i VBox
        VBox content = new VBox(15, dietTitle, vegetarisk, vegan, glutenfri, laktosfri, kitchenTitle, svenskt, italienskt, asiatiskt, mellanOstern);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(40));

        this.getChildren().add(content);
    }
}
