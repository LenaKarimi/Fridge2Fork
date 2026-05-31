package View;

import App.Fridge2ForkApp;
import Controller.RecipeController;
import Controller.UserController;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import java.util.Arrays;
import java.util.Collections;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import java.util.*;

/**
 * View for selecting ingredients from categorixed lists.
 * The user must select at least one item per caregory before proceeding to recipe search.
 * @author Maya and Intisaar
 */
public class FridgeView extends StackPane {
    private Label errorLabel;
    private final UserController userController;
    private List<VBox> categorySections = new ArrayList<>();

    /**
     * Constructs the fRIDGEview and builds the UI.
     * @param userController provides the currently logged in user
     */
    public FridgeView(UserController userController){
        this.userController = userController;
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(40));
        mainContent.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("What's in your fridge?");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: black;");
        Label infoIcon = new Label("i");
        infoIcon.setStyle(
                "-fx-background-color: darkseagreen; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Serif'; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-shape: 'M 10 0 A 10 10 0 1 0 10 20 A 10 10 0 1 0 10 0'; " + //gör den rund
                        "-fx-min-width: 20px; " +
                        "-fx-min-height: 20px; " +
                        "-fx-alignment: center; " +
                        "-fx-cursor: help;"
        );
        Tooltip tooltip = new Tooltip("Select all the ingredients you have at home.\nChoose 'None'" +
                " if a category is empty.");
        tooltip.setShowDelay(Duration.millis(100)); // Visas snabbt
        tooltip.setStyle("-fx-font-size: 14px;");
        Tooltip.install(infoIcon, tooltip);

        infoIcon.setOnMouseClicked(e -> {
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("How to use");
            infoAlert.setHeaderText("Instructions for Fridge2Fork");
            infoAlert.setContentText(
                    "1. Browse through the categories.\n" +
                            "2. Check the ingredients you have at home.\n" +
                            "3. If you don't have anything in a category, check 'None'.\n" +
                            "4. Click 'Next step' when you're ready!"
            );
            infoAlert.showAndWait();
        });

        HBox titleBox = new HBox(15, title, infoIcon);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        mainContent.getChildren().add(titleBox);

        errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
        errorLabel.setVisible(false);

        categorySections.add(createCategorySection("Carbohydrates",  "Rice","Jasmine Rice", "Basmati Rice", "Brown Rice", "Pasta", "Spaghetti", "Macaroni",
                "Noodles", "Udon Noodles", "Egg Noodles", "Bread", "White Bread", "Ciabatta", "Baguette", "Flour", "Whole Wheat Flour", "Corn Flour", "Tortilla", "Wraps", "Couscous", "Quinoa", "Oats", "None"));

        categorySections.add(createCategorySection("Protein", "Chicken", "Chicken Breast", "Chicken Thigh", "Beef", "Beef Brisket", "Minced Beef", "Steak", "Pork", "Pork Chops", "Pork Belly", "Lamb", "Lamb Shoulder", "Lamb Mince", "Turkey", "Duck", "Goat", "Bacon", "Ham", "Sausage",
                "Salmon", "Tuna", "Cod", "Haddock", "Sardines", "Anchovies", "Shrimp", "Prawns", "Crab", "Lobster", "Mussels", "Clams", "Squid", "Octopus", "Fish",
                "Tofu", "Tempeh", "Beans", "Lentils", "Chickpeas", "Quinoa",
                "Lentils", "Red Lentils", "Green Lentils", "Chickpeas", "Black Beans", "Kidney Beans", "White Beans", "Butter Beans", "Peas", "Split Peas", "None"));

        categorySections.add(createCategorySection("Vegetables", "Onion", "Garlic", "Tomato", "Cherry Tomatoes", "Baby Plum Tomatoes", "Potato",
                "Sweet Potato", "Carrot", "Cabbage", "Red Cabbage",
                "Spinach", "Lettuce", "Broccoli", "Cauliflower", "Zucchini", "Eggplant", "Bell Pepper",
                "Green Pepper", "Red Pepper", "Chili", "Cucumber", "Leek", "Spring Onion", "Mushroom",
                "Pumpkin", "Squash", "Corn", "Peas", "Green Beans", "Okra", "Radish", "None"));

        categorySections.add(createCategorySection("Fruits", "Apple", "Banana", "Orange", "Lemon", "Lime", "Mango", "Pineapple", "Coconut",
                "Strawberry", "Blueberry", "Raspberry", "Pear", "Peach", "Plum", "Apricot", "Fig", "Dates", "Avocado", "None"));

        categorySections.add(createCategorySection("Dairy", "Milk", "Butter", "Cheese", "Cheddar", "Mozzarella",
                "Parmesan", "Feta", "Cream", "Double Cream", "Sour Cream", "Yogurt", "Greek Yogurt", "Custard", "Paneer", "Ricotta", "Mascarpone",
                "Ghee", "Creme Fraiche", "None"));

        categorySections.add(createCategorySection("Pantry", "Olive oil", "Garlic", "Canned tomatoes",
                "Chickpeas", "Lentils", "Nuts", "None"));

        categorySections.add(createCategorySection("Spices & herbs", "Salt", "Black Pepper", "White Pepper", "Paprika", "Smoked Paprika",
                "Cumin", "Turmeric", "Curry Powder", "Chili Powder", "Cinnamon", "Cardamom", "Cloves", "Nutmeg", "Oregano", "Basil", "Parsley", "Thyme", "Rosemary",
                "Coriander", "Bay Leaves", "None"));

        categorySections.add(createCategorySection("Sauce", "Soy Sauce", "Fish Sauce", "Oyster Sauce", "Tomato Sauce", "Ketchup", "Mayonnaise", "Mustard",
                "Vinegar", "Balsamic Vinegar", "Olive Oil", "Vegetable Oil", "Sesame Oil", "Hot Sauce", "Chili Sauce", "None"));

        categorySections.add(createCategorySection("Liquid", "Water", "Stock", "Chicken Stock",
                "Beef Stock", "Vegetable Stock", "Wine", "White Wine", "Red Wine", "Beer", "Coconut Milk", "None"));

        categorySections.add(createCategorySection("Nuts and seeds", "Almonds", "Cashews", "Peanuts",
                "Walnuts", "Hazelnuts", "Pistachios", "Sesame Seeds", "Sunflower Seeds", "Pumpkin Seeds", "None"));

        categorySections.add(createCategorySection("Other", "Eggs", "Breadcrumbs", "Gelatin",
                "Yeast", "Pasta Sheets", "Dough", "Pickles", "Olives", "None"));

        mainContent.getChildren().addAll(categorySections);

        Button nextButton = new Button("Next step");
        nextButton.setStyle("-fx-font-size: 16px; -fx-padding: 12 40; -fx-background-color: darkseagreen;" +
                "fx-text-fill: white; -fx-font-weight: bold;");
        nextButton.setCursor(javafx.scene.Cursor.HAND);
        nextButton.setOnAction(e -> handleNextStep());

        mainContent.getChildren().addAll(nextButton, errorLabel);
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        this.getChildren().add(scrollPane);
    }

    /**
     * Returns a list of selected ingredient name from a category section.
     * @param section the VBox representing a category
     * @return list of selected ingredient names, excluding "None"
     */
    private List<String> getSelectedFromSection(VBox section){
        List<String> selected = new ArrayList<>();
        FlowPane flow = (FlowPane) section.getChildren().get(2);

        for (javafx.scene.Node node : flow.getChildren()){
            if (node instanceof CheckBox){
                CheckBox cb = (CheckBox) node;
                if (cb.isSelected() && !cb.getText().equalsIgnoreCase("None")){
                    selected.add(cb.getText());
                }
            }
        }
        return selected;
    }

    /**
     * Validates that all categories have at least one selection, then navigates to the DietView with the selected ingredients.
     */
    private void handleNextStep(){
        Map<String, List<String>> categoryMap = new HashMap<>();
        List<String> missingCategories = new ArrayList<>();

        for (VBox section : categorySections) {
            Label catLabel = (Label) section.getChildren().get(0);
            String categoryName = catLabel.getText();

            List<String> allSelected = new ArrayList<>();
            FlowPane flow = (FlowPane) section.getChildren().get(2);

            for (javafx.scene.Node node : flow.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox cb = (CheckBox) node;
                    if (cb.isSelected()) {
                        allSelected.add(cb.getText());
                    }
                }
            }
            if (allSelected.isEmpty()) {
                missingCategories.add(categoryName);
            }
            else {
            }
            List<String> realIngredients = new ArrayList<>();
            for (String item : allSelected) {
                if (!item.equalsIgnoreCase("None")) {
                    realIngredients.add(item);
                }
            }
            if (!realIngredients.isEmpty()) {
                categoryMap.put(categoryName, realIngredients);
            }
        }

        if (!missingCategories.isEmpty()){
            showErrorMessage("Missing selection in: " + String.join(", ", missingCategories));
            return;
        }
        clearErrorMessage();
        try {
            RecipeController controller = new RecipeController();
            Fridge2ForkApp.root.setCenter(new DietView(categoryMap, controller, userController));
        }
        catch (Exception ex) {
            showErrorMessage("Something went wrong when searching for recipes. Please try again!");
            ex.printStackTrace();
        }
    }

    /**
     * Creates a categorized section with checkboxes from each ingredient.
     * @param categoryName the name of the category
     * @param ingredients the ingredients to display as checkboxed
     * @return a styled VBox containing the category label, buttons anc checkboxes
     */
    private VBox createCategorySection(String categoryName, String... ingredients){
        VBox section = new VBox(10);
        section.setStyle("-fx-background-color: #f2ede4; -fx-background-radius: 10; -fx-padding: 15;");
        Label catLabel = new Label(categoryName);
        catLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: darkseagreen;");
        FlowPane ingredientFlow = new FlowPane(15, 10);

        Button selectAll = new Button("Select all");
        Button clearAll = new Button("Clear all");

        selectAll.setStyle( "-fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 4 12;" +
                "-fx-background-color: darkseagreen; -fx-text-fill: white;" +
                "-fx-background-radius: 5; -fx-cursor: hand;");
        clearAll.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-padding: 4 12;" +
                "-fx-background-color: white; -fx-text-fill: #c0392b;" +
                "-fx-border-color: #c0392b; -fx-border-radius: 5; -fx-cursor: hand;");
        selectAll.setCursor(javafx.scene.Cursor.HAND);
        clearAll.setCursor(javafx.scene.Cursor.HAND);

        selectAll.setOnAction(e -> {
            for (javafx.scene.Node node : ingredientFlow.getChildren()) {
                if (node instanceof CheckBox cb && !cb.getText().equalsIgnoreCase("None")) {
                    cb.setSelected(true);
                }
            }
        });

        clearAll.setOnAction(e -> {
            for (javafx.scene.Node node : ingredientFlow.getChildren()) {
                if (node instanceof CheckBox cb) {
                    cb.setSelected(false);
                }
            }
        });

        List<String> sortedList = new ArrayList<>(Arrays.asList(ingredients));

        sortedList.remove("None");
        Collections.sort(sortedList);
        sortedList.add("None");

        for(String ingredient : sortedList){
            CheckBox cb = new CheckBox(ingredient);

            if (ingredient.equalsIgnoreCase("None")) {
                cb.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-style: bold;" +
                        "-fx-font-style: italic; -fx-border-color: black; -fx-border-radius: 5;" +
                        " -fx-padding: 2 6;");

                cb.setOnAction(e -> {
                    if (cb.isSelected()){
                        for (javafx.scene.Node node: ingredientFlow.getChildren()){
                            if (node instanceof CheckBox other && !other.getText().equalsIgnoreCase("None")){
                                other.setSelected(false);
                            }
                        }
                    }
                });
            }
            else {
                cb.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                //om en ingrediens klickas, avkryssa "none"
                cb.setOnAction(e -> {
                    if (cb.isSelected()){
                        for (javafx.scene.Node node : ingredientFlow.getChildren()){
                            if (node instanceof CheckBox other && other.getText().equalsIgnoreCase("None")){
                                other.setSelected(false);
                            }
                        }
                    }
                });
            }
            ingredientFlow.getChildren().add(cb);
        }
        HBox buttons = new HBox(10, selectAll, clearAll);
        section.getChildren().addAll(catLabel, buttons, ingredientFlow);
        return section;
    }

    /**
     * Shows error message below the next button.
     * @param message the message
     */
    public void showErrorMessage(String message){
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Hides the error message
      */
    public void clearErrorMessage(){
      errorLabel.setVisible(false);
    }
}
