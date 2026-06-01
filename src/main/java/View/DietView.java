package View;

import App.Fridge2ForkApp;
import Controller.RecipeController;
import Controller.UserController;
import Model.Cuisine;
import Model.CuisineGroup;
import Model.Diet;
import Model.Recipe;
import javafx.concurrent.Task;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * View for selecting cuisine type and dietary preferences before searching for recipes.
 * Runs the recipe search in a background thread to keep the UI responsive.
 */
public class DietView extends StackPane {

    private final Map<String, List<String>> selectedIngredients;
    private final RecipeController controller;
    private final Label errorLabel;
    private final Button findRecipesBtn;
    private final UserController userController;
    private final List<CheckBox> cuisineCheckBoxes = new ArrayList<>();
    private final List<CheckBox> dietCheckBoxes = new ArrayList<>();
    private final Node previousView;
    private final VBox loadingOverlay;
    private final VBox mainForm;

    /**
     * Constructs the DietView and builds the UI.
     * @param selectedIngredients ingredients selected by the user in the previous step
     * @param controller handles the logic of recipe search
     * @param userController provides the current logged in user
     * @param previousView the view to return to after the results
     */
    public DietView(Map<String, List<String>> selectedIngredients, RecipeController controller,
                    UserController userController, Node previousView) {
        this.selectedIngredients = selectedIngredients;
        this.controller = controller;
        this.userController = userController;
        this.previousView = previousView;


        mainForm = new VBox(15);
        mainForm.setAlignment(Pos.CENTER_LEFT);
        mainForm.setPadding(new Insets(40));

        Label kitchenTitle = new Label("Cuisine type");
        kitchenTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        findRecipesBtn = new Button("Find Recipes");
        findRecipesBtn.setStyle("-fx-font-size: 16px; -fx-padding: 12 40; -fx-background-color: darkseagreen;" +
                " -fx-text-fill: white; -fx-font-weight: bold;");
        findRecipesBtn.setCursor(javafx.scene.Cursor.HAND);

        errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
        errorLabel.setVisible(false);

        findRecipesBtn.setOnAction(e -> fetchRecipesInBackground());

        String checkStyle = "-fx-font-size: 16px;";
        String[] cuisines = {"American", "Asian", "European", "Latin American", "Middle Eastern", "Any Cuisine"};
        for (String c : cuisines) {
            CheckBox cb = new CheckBox(c);
            cb.setStyle(checkStyle);
            cuisineCheckBoxes.add(cb);
        }

        Label dietTitle = new Label("Dietary preferences");
        dietTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-padding: 15 0 5 0;");

        String[] diets = {"Vegetarian", "Vegan", "Any diet"};
        for (String d : diets) {
            CheckBox cb = new CheckBox(d);
            cb.setStyle(checkStyle);
            dietCheckBoxes.add(cb);
        }

        mainForm.getChildren().add(kitchenTitle);
        mainForm.getChildren().addAll(cuisineCheckBoxes);
        mainForm.getChildren().add(dietTitle);
        mainForm.getChildren().addAll(dietCheckBoxes);
        mainForm.getChildren().addAll(findRecipesBtn, errorLabel);

        loadingOverlay = new VBox(20);
        loadingOverlay.setAlignment(Pos.CENTER);
        loadingOverlay.setVisible(false);

        ProgressIndicator progress = new ProgressIndicator();
        progress.setStyle("-fx-progress-color: darkseagreen;");
        progress.setPrefSize(60, 60);

        Label loadingText = new Label("Mixing ingredients and finding recipes...");
        loadingText.setStyle("-fx-font-size: 18px; -fx-font-style: italic; -fx-text-fill: gray;");

        loadingOverlay.getChildren().addAll(progress, loadingText);

        this.getChildren().addAll(mainForm, loadingOverlay);
    }

    /**
     * Kicks off the search in a background thread and shows a loading UI.
     */
    private void fetchRecipesInBackground() {
        List<Cuisine> chosenCuisines = getSelectedCuisines();
        List<Diet> chosenDiets = getSelectedDiets();

        if (chosenCuisines.isEmpty() && !isAnySelectionSelected(cuisineCheckBoxes, "Any Cuisine")) {
            showError("Please select at least one cuisine type or 'Any cuisine'.");
            return;
        }

        if (chosenDiets.isEmpty() && !isAnySelectionSelected(dietCheckBoxes, "Any diet")) {
            showError("Please select at least one diet or 'Any diet'.");
            return;
        }

        errorLabel.setVisible(false);
        mainForm.setOpacity(0.3);
        mainForm.setDisable(true);
        loadingOverlay.setVisible(true);

        findRecipesBtn.setDisable(true);
        findRecipesBtn.setText("Searching...");

        Task<List<Recipe>> task = new Task<>() {
            @Override
            protected List<Recipe> call() throws Exception {
                return controller.searchRecipes(selectedIngredients, chosenCuisines, chosenDiets);
            }
        };

        task.setOnSucceeded(evt -> {
            List<Recipe> recipes = task.getValue();
            Platform.runLater(() -> Fridge2ForkApp.root.setCenter(new RecipeResultsView(recipes, previousView,
                    userController)));
        });

        task.setOnFailed(evt -> {
            Platform.runLater(() -> {
                loadingOverlay.setVisible(false);
                mainForm.setOpacity(1.0);
                mainForm.setDisable(false);
                findRecipesBtn.setDisable(false);
                findRecipesBtn.setText("Find Recipes");

                Throwable ex = task.getException();
                String message = ex != null && ex.getMessage() != null ? ex.getMessage() : "Unknown error";
                showError("Could not fetch recipes: " + message);
            });
            task.getException().printStackTrace();
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }


    private void showError(String msg) {
        errorLabel.setText(msg);
        errorLabel.setVisible(true);
    }

    private boolean isAnySelectionSelected(List<CheckBox> boxes, String text) {
        return boxes.stream().anyMatch(cb -> cb.getText().equals(text) && cb.isSelected());
    }

    private List<Cuisine> getSelectedCuisines() {
        List<Cuisine> selected = new ArrayList<>();
        for (CheckBox cb : cuisineCheckBoxes) {
            if (cb.isSelected()) {
                if (cb.getText().equals("Any Cuisine")) return new ArrayList<>();
                String groupName = cb.getText().toLowerCase().replace(" ", "_");
                try {
                    CuisineGroup group = CuisineGroup.valueOf(groupName);
                    for (Cuisine c : Cuisine.values()) {
                        if (c.getCuisineGroup() == group) selected.add(c);
                    }
                } catch (Exception ignored) {}
            }
        }
        return selected;
    }

    private List<Diet> getSelectedDiets() {
        List<Diet> selected = new ArrayList<>();
        for (CheckBox cb : dietCheckBoxes) {
            if (cb.isSelected()) {
                if (cb.getText().equals("Any diet")) return new ArrayList<>();
                try {
                    selected.add(Diet.valueOf(cb.getText().toUpperCase()));
                } catch (Exception ignored) {}
            }
        }
        return selected;
    }
}