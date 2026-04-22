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
        card.setStyle(
                "-fx-background-color: white;" + "-fx-padding: 15;" + "-fx-background-radius: 15;" + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        card.setPrefWidth(250);

        //Debug: skriv ut vad objektet innehåller så vi ser varför namn/bild saknas
        System.out.println("RecipeResultsView -> recipe debug: name=" + recipe.getName() + ", imageUrl=" + recipe.getImageUrl() + ", cuisine=" + recipe.getCuisine());

        //Namn, fallback om null/empty
        String nameText = recipe.getName() != null && !recipe.getName().isBlank() ? recipe.getName() : "Unnamed recipe";
        Label nameLabel = new Label(nameText);
        nameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        nameLabel.setWrapText(true);

        //Försök ladda bild om URL finns
        String imageUrl = recipe.getImageUrl();
        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                Image image = new Image(imageUrl, true);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(140);
                imageView.setPreserveRatio(true);
                card.getChildren().add(imageView);
            } catch (Exception e) {
                //om bilden inte kan laddas, visa inget men logga
                System.out.println("RecipeResultsView -> failed to load image for url=" + imageUrl + " : " + e.getMessage());
            }
        }

        String cuisineText = recipe.getCuisine() != null ? recipe.getCuisine().toString() : "Unknown cuisine";
        Label cuisineLabel = new Label(cuisineText);
        cuisineLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: darkseagreen;");

        card.getChildren().addAll(nameLabel, cuisineLabel);

        return card;

    }
}