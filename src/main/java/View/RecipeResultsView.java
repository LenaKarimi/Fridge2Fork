package View;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class RecipeResultsView extends VBox {

    public RecipeResultsView(){
    this.setPadding(new Insets(40));
    this.setSpacing(30);
    this.setAlignment(Pos.TOP_CENTER);

    Label title = new Label("Matching recipes");
    title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: darkkhaki;");
    this.getChildren().add(title);

    //HBox så de syns bredvid varandra
    HBox recipeContainer = new HBox(20);
    recipeContainer.setAlignment(Pos.CENTER);

    //4st placeholder-recept
    recipeContainer.getChildren().addAll(createRecipeCard("Paella", "A classic Spanish saffron rice" +
                    " dish with seafood and spices...", "recept1.jpg"), createRecipeCard(
                            "Halloumi Pasta", "Golden grilled halloumi with zesty and creamy pasta...",
            "recept4.jpg"), createRecipeCard("Pasta Alfredo", "Rich and creamy parmesan" +
                    " sauce on perfectly cooked pasta...", "recept2.jpg"), createRecipeCard(
                            "Poke bowl", "Fresh and colorful Hawaiian bowl with marinated protein and" +
                            "veggies...", "recept3.jpg"));



    ScrollPane scrollPane = new ScrollPane(recipeContainer);
    scrollPane.setFitToHeight(true);
    scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

    this.getChildren().add(scrollPane);
    }

    private VBox createRecipeCard(String name, String description, String imageName){
        VBox card = new VBox(10);
        card.setStyle(
           "-fx-background-color: white;" +
           "-fx-padding: 15;" +
           "-fx-background-radius: 15;" +
           "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        card.setPrefWidth(250);

        try {
            Image img = new Image(getClass().getResourceAsStream("/images/" + imageName));
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(220);
            imgView.setPreserveRatio(true);
            card.getChildren().add(imgView);
        } catch (Exception e) {
            //bara grå bild om bild ej hittas
            Label placeHolder = new Label("Image not found");
            placeHolder.setPrefSize(220, 150);
            placeHolder.setStyle("-fx-background-color: lightgrey;");

            card.getChildren().add(placeHolder);
        }

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: grey;");

        card.getChildren().addAll(nameLabel, descLabel);
        return card;
    }
}


