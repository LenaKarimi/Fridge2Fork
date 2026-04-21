package View;

import App.Fridge2ForkApp;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;


public class FridgeView extends StackPane {

    private Label errorLabel;

    public FridgeView(){
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(40));
        mainContent.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("What's in your fridge?");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: black;");
        mainContent.getChildren().add(title);

        errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
        errorLabel.setVisible(false);
        mainContent.getChildren().add(errorLabel);

        //Kolhydrater
        mainContent.getChildren().add(createCategorySection("Carbohydrates", "Rice","Jasmine Rice", "Basmati Rice", "Brown Rice", "Pasta", "Spaghetti", "Macaroni",
                "Noodles", "Udon Noodles", "Egg Noodles", "Bread", "White Bread", "Ciabatta", "Baguette", "Flour", "Whole Wheat Flour", "Corn Flour", "Tortilla", "Wraps", "Couscous", "Quinoa", "Oats", "none"));

        //Protein
        mainContent.getChildren().add(createCategorySection("Protein", "Chicken", "Chicken Breast", "Chicken Thigh", "Beef", "Beef Brisket", "Minced Beef", "Steak", "Pork", "Pork Chops", "Pork Belly", "Lamb", "Lamb Shoulder", "Lamb Mince", "Turkey", "Duck", "Goat", "Bacon", "Ham", "Sausage",
                "Salmon", "Tuna", "Cod", "Haddock", "Sardines", "Anchovies", "Shrimp", "Prawns", "Crab", "Lobster", "Mussels", "Clams", "Squid", "Octopus", "Fish",
                "Tofu", "Tempeh", "Beans", "Lentils", "Chickpeas", "Quinoa",
                "Lentils", "Red Lentils", "Green Lentils", "Chickpeas", "Black Beans", "Kidney Beans", "White Beans", "Butter Beans", "Peas", "Split Peas", "none"));


        //Grönsaker
        mainContent.getChildren().add(createCategorySection("Vegetables", "Onion", "Garlic", "Tomato", "Cherry Tomatoes", "Baby Plum Tomatoes", "Potato", "Sweet Potato", "Carrot", "Cabbage", "Red Cabbage", "Spinach", "Lettuce", "Broccoli", "Cauliflower", "Zucchini", "Eggplant", "Bell Pepper",
                "Green Pepper", "Red Pepper", "Chili", "Cucumber", "Leek", "Spring Onion", "Mushroom", "Pumpkin", "Squash", "Corn", "Peas", "Green Beans", "Okra", "Radish", "none"));

        //Frukter:
        mainContent.getChildren().add(createCategorySection("Fruits", "Apple", "Banana", "Orange", "Lemon", "Lime", "Mango", "Pineapple", "Coconut", "Strawberry", "Blueberry", "Raspberry", "Pear", "Peach", "Plum", "Apricot", "Fig", "Dates", "Avocado", "none"));


        //Mejeri
        mainContent.getChildren().add(createCategorySection("Dairy", "Milk", "Butter", "Cheese", "Cheddar", "Mozzarella", "Parmesan", "Feta", "Cream", "Double Cream", "Sour Cream", "Yogurt", "Greek Yogurt", "Custard", "Paneer", "Ricotta", "Mascarpone", "Ghee", "Creme Fraiche", "none"));

        //Skafferi
        mainContent.getChildren().add(createCategorySection("Pantry", "Olive oil", "Garlic", "Canned tomatoes",
                "Chickpeas", "Lentils", "Nuts", "None"));

        //örter & kryddor
        mainContent.getChildren().add(createCategorySection("Spices & herbs", "Salt", "Black Pepper", "White Pepper", "Paprika", "Smoked Paprika", "Cumin", "Turmeric", "Curry Powder", "Chili Powder", "Cinnamon", "Cardamom", "Cloves", "Nutmeg", "Oregano", "Basil", "Parsley", "Thyme", "Rosemary", "Coriander", "Bay Leaves", "None"));

        mainContent.getChildren().add(createCategorySection("Sause", "Soy Sauce", "Fish Sauce", "Oyster Sauce", "Tomato Sauce", "Ketchup", "Mayonnaise", "Mustard", "Vinegar", "Balsamic Vinegar", "Olive Oil", "Vegetable Oil", "Sesame Oil", "Hot Sauce", "Chili Sauce", "None"));

        mainContent.getChildren().add(createCategorySection("Liquid", "Water", "Stock", "Chicken Stock", "Beef Stock", "Vegetable Stock", "Wine", "White Wine", "Red Wine", "Beer", "Coconut Milk", "None"));

        mainContent.getChildren().add(createCategorySection("Nuts and seeds", "Almonds", "Cashews", "Peanuts", "Walnuts", "Hazelnuts", "Pistachios", "Sesame Seeds", "Sunflower Seeds", "Pumpkin Seeds", "None"));

        mainContent.getChildren().add(createCategorySection("Other", "Eggs", "Breadcrumbs", "Gelatin", "Yeast", "Pasta Sheets", "Dough", "Pickles", "Olives", "None"));



        //Nästa-knappen
        Button nextButton = new Button("Next step");
        nextButton.setStyle("-fx-font-sixe; 16px -fx-padding: 12 40; -fx-background-color: darkseagreen;" +
                "fx-text-fill: white; -fx-font-weight: bold;");
        nextButton.setCursor(javafx.scene.Cursor.HAND);
        nextButton.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new DietView());
        });

        mainContent.getChildren().add(nextButton);

        //ScrollPane om listan blir lite för lång
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        this.getChildren().add(scrollPane);
    }
        //"..." betyder att metoden tar emot hur många strängar som helst
    private VBox createCategorySection(String categoryName, String...ingredients) {
        VBox section = new VBox(10);
        Label catLabel = new Label(categoryName);
        catLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: darkseagreen");
        FlowPane ingredientFlow = new FlowPane(15, 10);

        for (String ingredient : ingredients) {
            CheckBox cb = new CheckBox(ingredient);
            cb.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

            ingredientFlow.getChildren().add(cb);
        }

        section.getChildren().addAll(catLabel, ingredientFlow);
        section.setPadding(new Insets(0, 0, 20, 0));
        return section;

    }

    //Tänkt att använda för att felhantera om val saknas
    public void showErrorMessage(String message){
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    //Tömma felmeddelandet
    public void clearErrorMessage(){
      errorLabel.setVisible(false);
    }
}
