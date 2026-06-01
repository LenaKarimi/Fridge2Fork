package View;

import App.Fridge2ForkApp;
import Controller.LikedRecipeController;
import Model.Recipe;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import Controller.UserController;
import javafx.scene.Node;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * View that displays a grid of recipe cards matching the users ingredients.
 * Allows the user to shuffle the displayed recipes and like/unlike individual recipes.
 * @author Intisaar, Maya and Racil
 */
public class RecipeResultsView extends VBox {
    private final UserController userController;
    private final List<Recipe> allFetchedRecipes;
    private HBox recipeContainer;
    private final Node previousView;

    /**
     * Constructs the RecipeResultView and builds the UI.
     * @param recipes list of matching recipes to display
     * @param previousView the view to return to when clicking "Edit ingredients"
     * @param userController provides the currently logged in user
     */
    public RecipeResultsView(List<Recipe> recipes, Node previousView, UserController userController) {
        this.setPadding(new Insets(40));
        this.setSpacing(30);
        this.setAlignment(Pos.TOP_CENTER);
        this.userController = userController;
        this.allFetchedRecipes = recipes;
        this.previousView = previousView;

        Label title = new Label("Matching recipes");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: darkkhaki;");
        this.getChildren().add(title);

        //back-/edit-knapp
        Button backBtn = new Button("← Edit ingredients");
        backBtn.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #2e7d32; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #2e7d32; " +
                        "-fx-border-radius: 20; " +
                        "-fx-font-size: 14px;"
        );
        backBtn.setCursor(Cursor.HAND);
        //hover-effekt
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(
                "-fx-background-color: #f1f8e9; " +
                        "-fx-text-fill: #1b5e20; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #1b5e20; " +
                        "-fx-border-radius: 20; " +
                        "-fx-font-size: 14px;"
        ));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(
                "-fx-background-color: white; " +
                        "-fx-text-fill: #2e7d32; " +
                        "-fx-font-weight: bold; " +
                        "-fx-padding: 8 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-border-color: #2e7d32; " +
                        "-fx-border-radius: 20; " +
                        "-fx-font-size: 14px;"
        ));

        backBtn.setOnAction(e -> {
            if (previousView != null){
                Fridge2ForkApp.root.setCenter(previousView);
            } else {
                Fridge2ForkApp.root.setCenter(new HomeView(userController));
            }
        });

        //lägger knappen till vänster och titeln i mitten
        StackPane headerStack = new StackPane();
        headerStack.getChildren().addAll(title, backBtn);
        StackPane.setAlignment(backBtn, Pos.CENTER_LEFT);
        StackPane.setAlignment(title, Pos.CENTER);
        this.getChildren().add(headerStack);

        HBox topBar = new HBox(10, backBtn, title);
        topBar.setAlignment(Pos.CENTER);
        this.getChildren().add(topBar);

        boolean isSuggestions = recipes != null && !recipes.isEmpty() && recipes.stream().allMatch(r -> r.getMatchPercentage() < 0.5);
        if (isSuggestions) {
            Label suggestionLabel = new Label("We could not find any recipes over 50% match, but here are some suggestions:");
            suggestionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray; -fx-font-style: italic;");
            this.getChildren().add(suggestionLabel);
        }

        if (recipes == null || recipes.isEmpty()) {
            Label noResults = new Label("No recipes found for your ingredients try selecting more!");
            noResults.setStyle("-fx-font-size: 16px; -fx-text-fill: grey;");
            this.getChildren().add(noResults);
            return;
        }

        this.recipeContainer = new HBox(20);
        recipeContainer.setAlignment(Pos.CENTER_LEFT);
        recipeContainer.setPadding(new Insets(10));

        for (Recipe recipe : recipes) {
            recipeContainer.getChildren().add(createRecipeCard(recipe));
        }

        ScrollPane scrollPane = new ScrollPane(recipeContainer);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        this.getChildren().add(scrollPane);

        Label shuffleHint = new Label ("Not what you were looking for?");
        shuffleHint.setStyle("-fx-font-size: 14px; " +
                "-fx-text-fill: gray; " +
                "-fx-font-style: italic;");

        Button randomizeBtn = new Button ("Shuffle recipes");
        randomizeBtn.setStyle("-fx-font-size: 15px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 12 40; " +
                "-fx-background-color: #2e7d32; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 8;"
        );

        randomizeBtn.setCursor(Cursor.HAND);
        randomizeBtn.setOnAction(e -> showRandomRecipes());
        VBox shuffleSection = new VBox(10, shuffleHint, randomizeBtn);
        shuffleSection.setAlignment(Pos.CENTER);
        shuffleSection.setPadding(new Insets (20, 0, 10, 0));
        this.getChildren().add(shuffleSection);
        showRandomRecipes();
    }

    /**
     * Displays a random selection of up to 6 recipes from the full list.
     */
    private void showRandomRecipes(){
        if ( allFetchedRecipes == null || allFetchedRecipes.isEmpty()) return;
        recipeContainer.getChildren().clear();

        List<Recipe> shuffleList = new ArrayList<>(allFetchedRecipes);
        Collections.shuffle(shuffleList);

        int limit = Math.min(6, shuffleList.size());
        List<Recipe> selectedSix = new ArrayList<>();
        for (int i = 0; i < limit; i++){
            selectedSix.add(shuffleList.get(i));
        }
        selectedSix.sort((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()));
        for (Recipe recipe : selectedSix){
            recipeContainer.getChildren().add(createRecipeCard(recipe));
        }
    }

    /**
     * Creates a recipe card with image, title, cuisine, match percentage and a like button.
     * @param recipe the recipe to display
     * @return a styled VBox representing the recipe card
     */
    private VBox createRecipeCard(Recipe recipe) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefWidth(250);
        card.setCursor(Cursor.HAND);
        card.setOnMouseClicked(e ->{
            Fridge2ForkApp.root.setCenter(new RecipeView(recipe, this, userController));
        });
        System.out.println("RecipeResultsView debug: name=" + recipe.getName() + ", cuisine=" + recipe.getCuisine());

        String nameText = (recipe.getName() != null && !recipe.getName().isBlank()) ? recipe.getName() : "Unnamed Recipe";
        Label titleLabel = new Label(nameText);
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal; -fx-text-fill: black;");
        titleLabel.setWrapText(true);

        String cuisineText = (recipe.getCuisine() != null) ? "Cuisine: " + recipe.getCuisine().toString() : "Unknown Cuisine";
        Label cuisineLabel = new Label(cuisineText);
        cuisineLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");

        int displayPercent = (int) (recipe.getMatchPercentage() * 100);

        Label matchLabel = new Label(displayPercent +  "% Match");
        matchLabel.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-padding: 3 8; " +

                "-fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 12px;");
        VBox textContainer = new VBox(5);
        textContainer.getChildren().addAll(titleLabel,cuisineLabel, matchLabel);

        String imageUrl = recipe.getImageUrl();
        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                Image image = new Image(imageUrl, true);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(220);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                card.getChildren().add(imageView);
            }
            catch (Exception e) {
                System.out.println("Kunde inte ladda bild");
            }
        }
        card.getChildren().add(textContainer);

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

                } else  {
                    heartBtn.setText("\u2661");
                    likedRecipeController.unlikeRecipe(profileId, recipe.getId());
                }
            });

            card.getChildren().add(heartBtn);
        }
        return card;
    }
}