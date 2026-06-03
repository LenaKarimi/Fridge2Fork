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
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.scene.effect.DropShadow;

/**
 * View that displays all recipes the user has liked.
 * Shows recipe cards in a scrollable grid with an option unlike each recipe.
 * @author Maya
 */
public class LikedRecipesView extends VBox {
    private final UserController userController;

    /**
     * Constructs the LikedRecipeView and builds the UI.
     * @param likedRecipes list of recipes the user has liked
     * @param userController provides the current logged in user
     */
    public LikedRecipesView(List<Recipe> likedRecipes, UserController userController) {
        this.userController = userController;

        this.setPadding(new Insets(40));
        this.setSpacing(30);
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: white;");

        Label title = new Label("Your Liked Recipes");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #e67e22;");

        FlowPane recipeGrid = new FlowPane();
        recipeGrid.setHgap(25);
        recipeGrid.setVgap(25);
        recipeGrid.setPadding(new Insets(20));
        recipeGrid.setAlignment(Pos.TOP_LEFT);

        if (likedRecipes == null || likedRecipes.isEmpty()) {
            Label noResults = new Label("You haven't liked any recipes yet!");
            noResults.setStyle("-fx-font-size: 18px; -fx-text-fill: gray;");
            this.getChildren().addAll(title, noResults);
        }
        else {
            for (Recipe recipe : likedRecipes) {
                recipeGrid.getChildren().add(createRecipeCard(recipe));
            }
            ScrollPane scrollPane = new ScrollPane(recipeGrid);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

            this.getChildren().addAll(title, scrollPane);
        }
    }

    /**
     * Creates a recipe card with image, title and an unlike button.
     * @param recipe the recipe to display.
     * @return a styled VBox representing the recipe card
     */
    private VBox createRecipeCard(Recipe recipe) {
        System.out.println("skapar kort för;" + recipe.getName());
        VBox card = new VBox(10);

        DropShadow hoverShadow = new javafx.scene.effect.DropShadow();
        hoverShadow.setColor(javafx.scene.paint.Color.rgb(0, 0, 0, 0.15));
        hoverShadow.setRadius(14);
        hoverShadow.setOffsetY(6);

        card.setOnMouseEntered(e -> {
            card.setEffect(hoverShadow);
            card.setTranslateY(-6);
        });

        card.setOnMouseExited(e -> {
            card.setEffect(null);
            card.setTranslateY(0);
        });

        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefWidth(240);
        card.setCursor(Cursor.HAND);
        card.setOnMouseClicked(e -> {
            Fridge2ForkApp.root.setCenter(new RecipeView(recipe, this, userController));
        });

        ImageView imageView = new ImageView();
        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isBlank()) {
            try {
                imageView.setImage(new Image(recipe.getImageUrl(), true));
                imageView.setFitWidth(210);
                imageView.setFitHeight(140);
                imageView.setPreserveRatio(true);
            }
            catch (Exception e) {
            }
        }

        String titleText = (recipe.getName() != null && !recipe.getName().isBlank()) ? recipe.getName() :
                "Unnamed Recipe";
        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(210);

        Button unlikeBtn = new Button("\u2665");
        unlikeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 20px;");
        unlikeBtn.setCursor(Cursor.HAND);
        unlikeBtn.setOnAction(e -> {
            if (userController.getCurrentUser() != null) {
                LikedRecipeController likedRecipeController = new LikedRecipeController();
                int profileId = userController.getCurrentUser().getId();
                likedRecipeController.unlikeRecipe(profileId, recipe.getId());
                card.setVisible(false);
                card.setManaged(false);
            }

        });

        VBox footer = new VBox(5, titleLabel, unlikeBtn);
        footer.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(imageView, footer);
        return card;
    }
}
