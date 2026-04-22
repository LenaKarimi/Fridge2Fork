package View;

import App.Fridge2ForkApp;
import Controller.RecipeController;
import Model.Recipe;
import javafx.concurrent.Task;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.util.List;

public class DietView extends StackPane {

    private final List<String> selectedIngredients;
    private final RecipeController controller;
    //Behåll dina gamla kommentarer som du hade
    //Label dietTitle = new Label("Dietary preferences");
    //dietTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

    //Ny label för att visa felmeddelanden i UI
    private final Label errorLabel;
    private final Button findRecipesBtn;

    public DietView(List<String> selectedIngredients, RecipeController controller){
        this.selectedIngredients = selectedIngredients;
        this.controller = controller;

        Label kitchenTitle = new Label("Cuisine type");
        kitchenTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        findRecipesBtn = new Button("Find Recipes");
        findRecipesBtn.setStyle("-fx-font-size: 16px; -fx-padding: 12 40; -fx-background-color: darkseagreen; -fx-text-fill: white; -fx-font-weight: bold;");
        findRecipesBtn.setCursor(javafx.scene.Cursor.HAND);

        //Ny errorLabel för att visa fel till användaren (synlig endast vid fel)
        errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
        errorLabel.setVisible(false);

        //När användare klickar, anropa controllern med valda ingredienser (i bakgrundstråd)
        findRecipesBtn.setOnAction(e -> fetchRecipesInBackground());

        //Kryssrutor för kosten
        //CheckBox vegetarian = new CheckBox("Vegetarian");
        //CheckBox vegan = new CheckBox("Vegan");
        //CheckBox glutenFree = new CheckBox("Gluten free");
        //CheckBox lactoseFree = new CheckBox("Lactose free");
        //CheckBox extraProtein = new CheckBox("Extra protein");
        //CheckBox lowCarb = new CheckBox("Low carb");

        CheckBox asian = new CheckBox("Asian");
        CheckBox middleEastern = new CheckBox("Middle eastern");
        CheckBox european = new CheckBox("European");
        CheckBox american = new CheckBox("American");
        CheckBox latinAmerican = new CheckBox("Latin america");

        //Stilen på alla kryssrutor
        String checkStyle = "-fx-font-size: 16px;";
        //vegetarian.setStyle(checkStyle);
        //vegan.setStyle(checkStyle);
        //glutenFree.setStyle(checkStyle);
        //lactoseFree.setStyle(checkStyle);

        asian.setStyle(checkStyle);
        middleEastern.setStyle(checkStyle);
        european.setStyle(checkStyle);
        american.setStyle(checkStyle);
        latinAmerican.setStyle(checkStyle);

        //extraProtein.setStyle(checkStyle);
        //lowCarb.setStyle(checkStyle);

        //Lägg allt i VBox
        VBox content = new VBox(15, kitchenTitle, asian,
                middleEastern, european, american, latinAmerican, findRecipesBtn, errorLabel);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(40));

        this.getChildren().add(content);
    }

    //Kör sökningen i bakgrundstråd, visa fel i errorLabel vid failure
    private void fetchRecipesInBackground() {
        // Rensa tidigare fel
        errorLabel.setVisible(false);
        errorLabel.setText("");

        //Inaktivera knapp medan sökning pågår
        findRecipesBtn.setDisable(true);
        findRecipesBtn.setText("Searching...");

        Task<List<Recipe>> task = new Task<>() {
            @Override
            protected List<Recipe> call() throws Exception {
                //Kör sökningen i bakgrunden (kan kasta Exception)
                return controller.searchRecipes(selectedIngredients);
            }
        };

        task.setOnSucceeded(evt -> {
            List<Recipe> recipes = task.getValue();
            //Återställ knapp
            findRecipesBtn.setDisable(false);
            findRecipesBtn.setText("Find Recipes");

            //Navigera till resultatsidan (på FX-thread)
            Platform.runLater(() -> Fridge2ForkApp.root.setCenter(new RecipeResultsView(recipes)));
        });

        task.setOnFailed(evt -> {
            Throwable ex = task.getException();
            String message = ex != null && ex.getMessage() != null ? ex.getMessage() : "Unknown error";
            errorLabel.setText("Could not fetch recipes: " + message);
            errorLabel.setVisible(true);

            //Återställ knapp
            findRecipesBtn.setDisable(false);
            findRecipesBtn.setText("Find Recipes");

            ex.printStackTrace();
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
}