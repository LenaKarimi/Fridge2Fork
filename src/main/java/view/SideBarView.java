package view;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import app.Fridge2ForkApp;
import view.HomeView;
import view.RecipeView;


public class SideBarView extends VBox {

    public SideBarView(){
        //1. Inställningar för själva sidebaren (VBox)
        this.setSpacing(10); //Mellanrum mellan raderna
        this.setPadding(new Insets(10)); //Marginal från kanten
        this.setStyle("-fx-background-color: khaki;");

        //2.  Översta raden med två kvadrater (HBox)
        HBox topRow = new HBox(10);
        StackPane leftSquare = createBox("powderblue", 70, 70, "Profil");
        StackPane rightSquare = createBox("lightpink", 70, 70, "Likes");
        topRow.getChildren().addAll(leftSquare, rightSquare);

        //Fyra rektanglar som fyller ut bredden automatiskt
        StackPane rect1 = createBox("white", 0, 80, "Hem");
        StackPane rect2 = createBox("white", 0, 80, "Recept");
        StackPane rect3 = createBox("white", 0, 80, "Ingredienser");
        StackPane rect4 = createBox("white", 0, 80, "Inställningar");

        //Gör rektanglarna lika breda som sidebaren
        rect1.setMaxWidth(Double.MAX_VALUE);
        rect2.setMaxWidth(Double.MAX_VALUE);
        rect3.setMaxWidth(Double.MAX_VALUE);
        rect4.setMaxWidth(Double.MAX_VALUE);

        this.getChildren().addAll(topRow, rect1, rect2, rect3, rect4);
    }

    private StackPane createBox(String color, double width, double height, String text){
        StackPane box = new StackPane();
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5;");
        box.setPrefSize(width, height);

        //Knapptrycken för vänsterpanelen
        box.setCursor(javafx.scene.Cursor.HAND);

        //Det som händer vid klick
        box.setOnMouseClicked(e -> {
            if(text.equals("Hem")){
                Fridge2ForkApp.root.setCenter(new HomeView());
            } else if (text.equals("Recept")){
                Fridge2ForkApp.root.setCenter(new RecipeView());
            }
            System.out.println("Du klickade på: " + text);
        });

        Label label = new Label(text);
        box.getChildren().add(label);

        return box;
    }
}
