package View;

import App.Fridge2ForkApp;
import Controller.LikedRecipeController;
import Model.Ingredient;
import Model.Recipe;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Controller.UserController;
import DTO.ProfileDTO;
import java.util.List;
import javafx.scene.Node;

/**
 * View that displays the full details of a single recipe.
 * Shows the recipe image, ingredients, and instructions.
 * Includes a like button for logged in users and a back button to return to results.
 * @author Intisaar and Maya
 */
public class RecipeView extends StackPane {
    private final Node previousView;
    private final UserController userController;

    /**
     * Constructs the RecipeView and builds the UI.
     * @param recipe the recipe to display
     * @param previousView the results view to return to, or null
     * @param userController provides the currently logged in user
     */
    public RecipeView(Recipe recipe, Node previousView, UserController userController) {
        this.previousView = previousView;
        this.userController = userController;

        this.setPadding(new Insets(40));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: white;");

        VBox content = new VBox(20);
        content.setMaxWidth(800);
        content.setAlignment(Pos.TOP_LEFT);

        String backText = "← Back";
        if (previousView instanceof LikedRecipesView){
            backText = "← Back to Liked Recipes";
        } else if (previousView instanceof RecipeResultsView){
            backText = "← Back to Results";
        }

        Button backButton = new Button(backText);
        backButton.setStyle("-fx-background-color: darkseagreen; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setCursor(javafx.scene.Cursor.HAND);

        backButton.setOnAction(e -> {
            if (previousView != null){
                Fridge2ForkApp.root.setCenter(previousView);
            } else {
                Fridge2ForkApp.root.setCenter(new HomeView(userController));
            }
        });

        Button likeButton = new Button("\u2661 Like"); //\u2661 = tomt hjärta, \u2665 = fyllt hjärta
        likeButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-border-color: red;" +
                " -fx-border-radius: 5; -fx-text-fill: red;");
        likeButton.setCursor(javafx.scene.Cursor.HAND);

        ProfileDTO currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            likeButton.setVisible(false);
        }

        Label title = new Label(recipe.getName());
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: darkseagreen;");
        title.setWrapText(true);

        if (userController.getCurrentUser() != null) {
            LikedRecipeController likedRecipeController = new LikedRecipeController();
            int profileId = userController.getCurrentUser().getId();

            boolean liked = likedRecipeController.isLiked(profileId, recipe.getId());
            Button heartBtn = new Button(liked ? "\u2665" : "\u2661");
            heartBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 20px;");
            heartBtn.setCursor(Cursor.HAND);

            heartBtn.setOnMouseClicked(e -> {
                e.consume();
                if (heartBtn.getText().equals("\u2661")) {
                    heartBtn.setText("\u2665");
                    likedRecipeController.likeRecipe(profileId, recipe);

                } else {
                    heartBtn.setText("\u2661");
                    likedRecipeController.unlikeRecipe(profileId, recipe.getId());
                }
            });
            content.getChildren().addAll(backButton, title, heartBtn);
        }
        else {
            content.getChildren().addAll(backButton, title);
        }

        ImageView recipeImageView = new ImageView();
        String imageUrl = recipe.getImageUrl();

        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                Image image = new Image(imageUrl, true);
                recipeImageView.setImage(image);
                recipeImageView.setFitWidth(500);
                //NYTT anpassa bildstorlek
                recipeImageView.setFitHeight(300);
                recipeImageView.setPreserveRatio(true);
                recipeImageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
            } catch (Exception e) {
                System.out.println("Kunde inte ladda receptbilden: " + e.getMessage());
            }
        }

        VBox ingredientsBox = new VBox(10);
        ingredientsBox.setMinWidth(400);
        Label ingredientsTitle = new Label("Ingredients");
        ingredientsTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        ingredientsBox.getChildren().add(ingredientsTitle);

        System.out.println("DEBUG: Antal ingredienser i receptobjektet: " + (recipe.getIngredients() != null ? recipe.getIngredients().size() : "NULL"));
        List<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients != null) {
            for (Ingredient ing : ingredients) {
                //här kombineras mått och namn
                String ingredientText = ing.getMeasure() + " " + ing.getName();
                Label ingLabel = new Label("• " + ingredientText);
                ingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
                ingredientsBox.getChildren().add(ingLabel);
            }
        }

        VBox instructionsBox = new VBox(10);
        Label instructionsTitle = new Label("Instructions");
        instructionsTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        String instrText = recipe.getInstructions();
        if (instrText == null || instrText.isBlank()) {
            instrText = " No instruction found for this recipe. Please try another one!";
        }

        Text instructionsText = new Text(instrText);
        instructionsText.setStyle("-fx-font-size: 16px; -fx-fill: black;");
        instructionsText.setWrappingWidth(750);
        instructionsBox.getChildren().addAll(instructionsTitle, instructionsText);

        HBox imageAndIngredients = new HBox(40, recipeImageView, ingredientsBox);
        imageAndIngredients.setAlignment(Pos.TOP_LEFT);

        content.getChildren().addAll(imageAndIngredients, instructionsBox);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white; -fx-background: white;");

        this.getChildren().add(scrollPane);
        System.out.println("Visar recept: " + recipe.getName());
        System.out.println("Instruktioner i objektet: " + (recipe.getInstructions() != null ? "JA" : "NEJ (NULL)"));
    }
}
