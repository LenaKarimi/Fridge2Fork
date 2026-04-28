package View;

import Model.Recipe;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.util.List;

public class RecipeResultsView extends VBox {

    public RecipeResultsView(List<Recipe> recipes) {
        this.setPadding(new Insets(40));
        this.setSpacing(30);
        this.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Matching recipes");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: darkkhaki;");
        this.getChildren().add(title);

        if (recipes == null || recipes.isEmpty()) {
            Label noResults = new Label("No recipes found for your ingredients try selecting more!");
            noResults.setStyle("-fx-font-size: 16px; -fx-text-fill: grey;");
            this.getChildren().add(noResults);
            return;
        }


        //HBox så de syns bredvid varandra
        HBox recipeContainer = new HBox(20);
        recipeContainer.setAlignment(Pos.CENTER_LEFT);
        recipeContainer.setPadding(new Insets(10));

        for (Recipe recipe : recipes) {
            recipeContainer.getChildren().add(createRecipeCard(recipe));
        }


        ScrollPane scrollPane = new ScrollPane(recipeContainer);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        this.getChildren().add(scrollPane);

    }


    private VBox createRecipeCard(Recipe recipe) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefWidth(250);

        //Debug
        System.out.println("RecipeResultsView debug: name=" + recipe.getName() + ", cuisine=" + recipe.getCuisine());

        //Skapa titel-label
        String nameText = (recipe.getName() != null && !recipe.getName().isBlank()) ? recipe.getName() : "Unnamed Recipe";
        Label titleLabel = new Label(nameText);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal; -fx-text-fill: black;");
        titleLabel.setWrapText(true);

        //Skapa cuisine-label
        String cuisineText = (recipe.getCuisine() != null) ? "Cuisine: " + recipe.getCuisine().toString() : "Unknown Cuisine";
        Label cuisineLabel = new Label(cuisineText);
        cuisineLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        //Behållare för all text
        VBox textContainer = new VBox(5);
        textContainer.getChildren().addAll(titleLabel, cuisineLabel);

        //Bild
        String imageUrl = recipe.getImageUrl();
        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                Image image = new Image(imageUrl, true);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(220);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                card.getChildren().addAll(imageView, textContainer);
            } catch (Exception e) {
                card.getChildren().add(textContainer);
            }
        } else {
            card.getChildren().add(textContainer);
        }

        return card;
    }
}