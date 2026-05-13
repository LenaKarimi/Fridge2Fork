package View;

import App.Fridge2ForkApp;
import Controller.LikedRecipeController;
import Controller.UserController;
import Model.Recipe;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;

import java.util.List;

public class LikedRecipesView extends VBox {

    private final UserController userController;

    public LikedRecipesView(List<Recipe> likedRecipes, UserController userController) {
        this.userController = userController;

        this.setPadding(new Insets(40));
        this.setSpacing(30);
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: white;");

        //rubrik
        Label title = new Label("Your Liked Recipes");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");

        //flowpane så korten hamnar på samma rader
        FlowPane recipeGrid = new FlowPane();
        recipeGrid.setHgap(25);
        recipeGrid.setVgap(25);
        recipeGrid.setPadding(new Insets(20));
        recipeGrid.setAlignment(Pos.TOP_LEFT);

        if (likedRecipes == null || likedRecipes.isEmpty()) {
            Label noResults = new Label("You haven't liked any recipes yet!");
            noResults.setStyle("-fx-font-size: 18px; -fx-text-fill: gray;");
            this.getChildren().addAll(title, noResults);
        } else {
            for (Recipe recipe : likedRecipes) {
                recipeGrid.getChildren().add(createRecipeCard(recipe));
            }

            //scrollpane ifall man har jättemånga gillade recept
            ScrollPane scrollPane = new ScrollPane(recipeGrid);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            this.getChildren().addAll(title, scrollPane);
        }
    }

    private VBox createRecipeCard(Recipe recipe) {
        System.out.println("skapar kort för;" + recipe.getName());
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefWidth(240);
        card.setCursor(Cursor.HAND);

        //klicka på kortet för att se receptet
        card.setOnMouseClicked(e -> {
            //skickar  null som previousView för att markera att man kommer från liked
            Fridge2ForkApp.root.setCenter(new RecipeView(recipe, null, userController));
        });

        //BILD
        ImageView imageView = new ImageView();
        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isBlank()) {
            try {
                imageView.setImage(new Image(recipe.getImageUrl(), true));
                imageView.setFitWidth(210);
                imageView.setFitHeight(140);
                imageView.setPreserveRatio(true);
            } catch (Exception e) { //ignorera laddningsfel
            }
        }

        //TEXT rad
        Label titleLabel = new Label(recipe.getName());
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(160); // kollar om detta lägger till namnet i liked recipe view

        //LIKE-knapp i hörnet (fyllt hjärta \u2665)
        Button unlikeBtn = new Button("\u2665");
        unlikeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 20px;");
        unlikeBtn.setCursor(Cursor.HAND);



        //Action för att ta bort (Racils del sen)
        unlikeBtn.setOnAction(e -> {
            if (userController.getCurrentUser() != null) {
                LikedRecipeController likedRecipeController = new LikedRecipeController();
                int profileId = userController.getCurrentUser().getId();
                likedRecipeController.unlikeRecipe(profileId, recipe.getId()); // tar bort från db
                card.setVisible(false);
                card.setManaged(false);
            }

        });

        HBox footer = new HBox(10, titleLabel, unlikeBtn);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.setMaxWidth(210); // kollar om detta adderar namnet på liked recipe view
        HBox.setHgrow(titleLabel, javafx.scene.layout.Priority.ALWAYS);

        card.getChildren().addAll(imageView, footer);
        return card;
    }
}
