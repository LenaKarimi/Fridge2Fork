package View;

import App.Fridge2ForkApp;
import Controller.RecipeController;
import Model.Cuisine;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DietView extends StackPane {

    //Nytt vi behöver map istället för list pga controller
    private final Map<String, List<String>> selectedIngredients;
    private final RecipeController controller;
    //Behåll dina gamla kommentarer som du hade
    //Label dietTitle = new Label("Dietary preferences");
    //dietTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

    //Ny label för att visa felmeddelanden i UI
    private final Label errorLabel;
    private final Button findRecipesBtn;

    //NYTT en lista för att hålla ordning på checkboxarna för kök
    private final List<CheckBox> cuisineCheckBoxes = new ArrayList<>();

    public DietView(Map<String, List<String>> selectedIngredients, RecipeController controller) {
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
        CheckBox middleEastern = new CheckBox("Middle Eastern");
        CheckBox european = new CheckBox("European");
        CheckBox american = new CheckBox("American");
        CheckBox latinAmerican = new CheckBox("Latin American");
        CheckBox anyCuisine = new CheckBox("Any Cuisine (None)");

        //lägger till dem i en lista så de ska kunna gå att loopa igenom dem
        cuisineCheckBoxes.add(asian);
        cuisineCheckBoxes.add(middleEastern);
        cuisineCheckBoxes.add(european);
        cuisineCheckBoxes.add(american);
        cuisineCheckBoxes.add(latinAmerican);
        cuisineCheckBoxes.add(anyCuisine);

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
        //NYTT hämtar alla namn samtidigt istyället för att skriva ut dem en och en

        VBox content = new VBox(15);
        content.getChildren().add(kitchenTitle);

        //NYTT här lägg checkboxarna till
        content.getChildren().addAll(cuisineCheckBoxes);
        //knapp och felmeddelande
        content.getChildren().addAll(findRecipesBtn, errorLabel);

        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(40));

        this.getChildren().add(content);
    }

    //Nytt en metod för att se vilka kök användaren valt
    private List<Cuisine> getSelectedCuisines() {
        List<Cuisine> selected = new ArrayList<>();

        //om any cuisine är vald skickar vi en tom lista, då visas alla kök
        for (CheckBox cb : cuisineCheckBoxes) {
            if (cb.isSelected()) {
                if (cb.getText().equals("Any Cuisine (None)")) {
                    return new ArrayList<>();
                }
                try {
                    //här görs texten om till en cuisine enum
                    String enumName = cb.getText().toUpperCase().replace(" ", "_");
                    selected.add(Cuisine.valueOf(enumName));
                } catch (IllegalArgumentException e) {
                    //om namnet inte matchar enumet hoppar vi över det
                }
            }
        }
        return selected;
    }

    //Kör sökningen i bakgrundstråd, visa fel i errorLabel vid failure
    private void fetchRecipesInBackground() {
        //Nytt hämtar de valda köken innan sökning av recept startar
        List<Cuisine> chosenCuisines = getSelectedCuisines();

        //NYTT användaren måste välja minst ett kök

        if (chosenCuisines.isEmpty() && !isAnyCuisineSelected()) {
            errorLabel.setText("Please select at least one cuisine type or 'Any cuisine'.");
            errorLabel.setVisible(true);
            return;
        }

        // Rensa tidigare fel
        errorLabel.setVisible(false);

        //Inaktivera knapp medan sökning pågår
        findRecipesBtn.setDisable(true);
        findRecipesBtn.setText("Searching...");

        Task<List<Recipe>> task = new Task<>() {
            @Override
            protected List<Recipe> call() throws Exception {
                //Kör sökningen i bakgrunden (kan kasta Exception)
                return controller.searchRecipes(selectedIngredients, chosenCuisines);
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

    //NYTT metod för att se om any cuisine är vald
    private boolean isAnyCuisineSelected() {
        for (CheckBox cb : cuisineCheckBoxes) {
            if (cb.getText().equals("Any Cuisine (None)") && cb.isSelected()) {
                return true;
            }
        }
        return false;
    }
}