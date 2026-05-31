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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * View for selecting cuisine type and dietary preferences before searching for recipes.
 * Runs the recipe search in a background thread to keep the UI responsive.
 * @author Maya, Lena and Intisaar
 */
public class DietView extends StackPane {

    private final Map<String, List<String>> selectedIngredients;
    private final RecipeController controller;
    private final Label errorLabel;
    private final Button findRecipesBtn;
    private final UserController userController;
    private final List<CheckBox> cuisineCheckBoxes = new ArrayList<>();
    private final List<CheckBox> dietCheckBoxes = new ArrayList<>();

    /**
     * Constructs the DietView and builds the UI.
     * @param selectedIngredients ingredients selected by the user in the previous step
     * @param controller handles the logic of recipe search
     * @param userController provides the current logged in user
     */
    public DietView(Map<String, List<String>> selectedIngredients, RecipeController controller,
                    UserController userController) {
        this.selectedIngredients = selectedIngredients;
        this.controller = controller;
        this.userController = userController;

        Label kitchenTitle = new Label("Cuisine type");
        kitchenTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        findRecipesBtn = new Button("Find Recipes");
        findRecipesBtn.setStyle("-fx-font-size: 16px; -fx-padding: 12 40; -fx-background-color: darkseagreen; -fx-text-fill: white; -fx-font-weight: bold;");
        findRecipesBtn.setCursor(javafx.scene.Cursor.HAND);

        errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
        errorLabel.setVisible(false);

        findRecipesBtn.setOnAction(e -> fetchRecipesInBackground());
        String checkStyle = "-fx-font-size: 16px;";

        CheckBox american = new CheckBox("American");
        CheckBox asian = new CheckBox("Asian");
        CheckBox european = new CheckBox("European");
        CheckBox latinAmerican = new CheckBox("Latin American");
        CheckBox middleEastern = new CheckBox("Middle Eastern");
        CheckBox anyCuisine = new CheckBox("Any Cuisine");

        cuisineCheckBoxes.add(american);
        cuisineCheckBoxes.add(asian);
        cuisineCheckBoxes.add(european);
        cuisineCheckBoxes.add(latinAmerican);
        cuisineCheckBoxes.add(middleEastern);
        cuisineCheckBoxes.add(anyCuisine);

        Label dietTitle = new Label("Dietary preferences");
        dietTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        CheckBox vegetarian = new CheckBox("Vegetarian");
        CheckBox vegan = new CheckBox("Vegan");
        CheckBox anyDiet = new CheckBox("Any diet");

        vegetarian.setStyle(checkStyle);
        vegan.setStyle(checkStyle);
        anyDiet.setStyle(checkStyle);

        dietCheckBoxes.add(vegetarian);
        dietCheckBoxes.add(vegan);
        dietCheckBoxes.add(anyDiet);

        asian.setStyle(checkStyle);
        middleEastern.setStyle(checkStyle);
        european.setStyle(checkStyle);
        american.setStyle(checkStyle);
        latinAmerican.setStyle(checkStyle);
        anyCuisine.setStyle(checkStyle);

        VBox content = new VBox(15);
        content.getChildren().add(kitchenTitle);
        content.getChildren().addAll(cuisineCheckBoxes);
        content.getChildren().addAll(dietTitle);
        content.getChildren().addAll(dietCheckBoxes);
        content.getChildren().addAll(findRecipesBtn, errorLabel);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(40));

        this.getChildren().add(content);
    }

    /**
     * Returns the list of cuisines selected by the user.
     * Returns an empty list if "Any Cuisine" is selected
     * @return list of selected cuisines
     */
    private List<Cuisine> getSelectedCuisines() {
        List<Cuisine> selected = new ArrayList<>();

        for (CheckBox cb : cuisineCheckBoxes) {
            if (cb.isSelected()) {
                if (cb.getText().equals("Any Cuisine"))
                    return new ArrayList<>();

                String groupName = cb.getText().toLowerCase().replace(" ", "_");
                try {
                    CuisineGroup selectedGroup = CuisineGroup.valueOf(groupName);
                    for (Cuisine c : Cuisine.values()){
                        if (c.getCuisineGroup() == selectedGroup){
                            selected.add(c);
                        }
                    }
                }
                catch (IllegalArgumentException e) {
                }
            }
        }
        return selected;
    }

    /**
     * Returns the list of diets selected by the user.
     * Returns an empty list if "Any diet" is selected.
     * @return list of selectd diets
     */
    private List<Diet> getSelectedDiets(){
        List<Diet> selected = new ArrayList<>();
        for (CheckBox cb : dietCheckBoxes) {
            if (cb.isSelected()) {
                if (cb.getText().equals("Any diet"))
                    return new ArrayList<>();
                try {
                    Diet diet = Diet.valueOf(cb.getText().toUpperCase());
                    selected.add(diet);
                }
                catch (IllegalArgumentException e) {
                }
            }
        }
        return selected;
    }

    /**
     * Validates selections, then returns recipe search in a background thread.
     * Shows an error message if validation fails or the search throws an exeption.
     */
    private void fetchRecipesInBackground() {
        List<Cuisine> chosenCuisines = getSelectedCuisines();
        List<Diet> chosenDiets = getSelectedDiets();

        if (chosenCuisines.isEmpty() && !isAnyCuisineSelected()) {
            errorLabel.setText("Please select at least one cuisine type or 'Any cuisine'.");
            errorLabel.setVisible(true);
            return;
        }

        if (chosenDiets.isEmpty() && !isAnyDietSelected()){
            errorLabel.setText("Please select at least one diet or any diet");
            errorLabel.setVisible(true);
            return;
        }

        errorLabel.setVisible(false);

        findRecipesBtn.setDisable(true);
        findRecipesBtn.setText("Searching...");

        Task<List<Recipe>> task = new Task<>() {
            @Override
            protected List<Recipe> call() throws Exception {
                return controller.searchRecipes(selectedIngredients, chosenCuisines,chosenDiets ); // chosenDiets behöver läggas till som en sista parameter, men möjligt när controller hanterat
            }
        };

        task.setOnSucceeded(evt -> {
            List<Recipe> recipes = task.getValue();
            findRecipesBtn.setDisable(false);
            findRecipesBtn.setText("Find Recipes");
            Platform.runLater(() -> Fridge2ForkApp.root.setCenter(new RecipeResultsView(recipes,
                    userController)));
        });

        task.setOnFailed(evt -> {
            Throwable ex = task.getException();
            String message = ex != null && ex.getMessage() != null ? ex.getMessage() : "Unknown error";
            errorLabel.setText("Could not fetch recipes: " + message);
            errorLabel.setVisible(true);

            findRecipesBtn.setDisable(false);
            findRecipesBtn.setText("Find Recipes");

            ex.printStackTrace();
        });

        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }

    /**
     * Checks if the "Any cuisine" checkbox is selected
     * @return true if and cuisine is selected, otherwise false
     */
    private boolean isAnyCuisineSelected() {
        for (CheckBox cb : cuisineCheckBoxes) {
            if (cb.getText().equals("Any Cuisine") && cb.isSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the "Any diet" checkbox is selected.
     * @return true if any diet is selected, otherwise false
     */
    private boolean isAnyDietSelected(){
        for (CheckBox cb : dietCheckBoxes) {
            if (cb.getText().equals("Any diet") && cb.isSelected()) {
                return true;
            }
        }
        return false;
    }
}