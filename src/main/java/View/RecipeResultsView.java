package View;

import App.Fridge2ForkApp;
import Controller.LikedRecipeController;
import Model.Recipe;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import Controller.UserController;
import Controller.RecipeController;

import java.util.List;

public class RecipeResultsView extends VBox {
    private final UserController userController;

    public RecipeResultsView(List<Recipe> recipes, UserController userController, RecipeController recipeController) {
        this.setPadding(new Insets(40));
        this.setSpacing(30);
        this.setAlignment(Pos.TOP_CENTER);
        this.userController = userController;

        Label title = new Label("Matching recipes");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: darkkhaki;");
        this.getChildren().add(title);

        if(recipeController.shouldShowWarning(recipes)){
            Label warningLabel = new Label ("Tyvärr fanns inte 6 recept över 50% matchning, men här är de närmsta vi hittade: ");
            warningLabel.setStyle("-fx-text-fill: #856404; -fx-background-color: #fff3cd; -fx-padding: 10; -fx-background-radius: 5; -fx-font-weight: bold;");
            this.getChildren().add(warningLabel);
        }

        Button newSearchBtn = new Button("← New search");
        newSearchBtn.setStyle("-fx-background-color: transparent; -fx-border-color: darkseagreen; -fx-border-radius: 5; -fx-text-fill: darkseagreen;");
        newSearchBtn.setCursor(Cursor.HAND);
        newSearchBtn.setOnAction(e -> Fridge2ForkApp.root.setCenter(new FridgeView(userController)));
        this.getChildren().add(newSearchBtn);

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
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        card.setPrefWidth(250);

        //gör så att muspekare ser ut som en hand när man hovrar över receptkortet
        card.setCursor(Cursor.HAND);

        //logiken för vad som ska hända när man klikcat på en recept
        card.setOnMouseClicked(e ->{
            Fridge2ForkApp.root.setCenter(new RecipeView(recipe, this, userController));
        });

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

        //NYTT skapa label för matchningsprocent
        //här hämtas värdet vi sparade i recipe-objektet via controllern
        int displayPercent = (int) (recipe.getMatchPercentage() * 100);

        Label matchLabel = new Label(displayPercent +  "% Match");
        matchLabel.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-padding: 3 8; " +

                "-fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 12px;");
        //Behållare för all text
        VBox textContainer = new VBox(5);
        //Nytt här läggs macthLabel till
        textContainer.getChildren().addAll(titleLabel,cuisineLabel, matchLabel);


        //Bild
        String imageUrl = recipe.getImageUrl();
        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                Image image = new Image(imageUrl, true);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(220);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                card.getChildren().add(imageView);
            } catch (Exception e) {
                System.out.println("Kunde inte ladda bild");
            }
        }
        //Text
        //card.getChildren().addAll(titleLabel, cuisineLabel);
        //NYTT eftersom titlelabel och cusuinelabel läggs till i samband med matchLabel litte längre upp
        card.getChildren().add(textContainer);

        // Hjärta - visas bara om användaren är inloggad
        /** Orginal metod som maya skapa
        if (userController.getCurrentUser() != null) {
            Button heartBtn = new Button("\u2661");
            heartBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 20px;");
            heartBtn.setCursor(Cursor.HAND);

            heartBtn.setOnMouseClicked(e -> {
                e.consume();
                if (heartBtn.getText().equals("\u2661")) {
                    heartBtn.setText("\u2665");
                } else {
                    heartBtn.setText("\u2661");
                }
            });

            card.getChildren().add(heartBtn);
        }

        return card;
         */

        if (userController.getCurrentUser() != null) {
            LikedRecipeController likedRecipeController = new LikedRecipeController();
            int profileId = userController.getCurrentUser().getId();

            //kolla om redan gillat
            boolean liked = likedRecipeController.isLiked(profileId, recipe.getId());
            Button heartBtn = new Button(liked ? "\u2665" : "\u2661");
            heartBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 20px;");
            heartBtn.setCursor(Cursor.HAND);

            heartBtn.setOnMouseClicked(e -> {
                e.consume();
                if (heartBtn.getText().equals("\u2661")) {
                    heartBtn.setText("\u2665");
                    likedRecipeController.likeRecipe(profileId, recipe); // sparar i db

                } else  {
                    heartBtn.setText("\u2661");
                    likedRecipeController.unlikeRecipe(profileId, recipe.getId()); // tar bort från db
                }
            });

            card.getChildren().add(heartBtn);
        }
        return card;
    }
}