package View;

import App.Fridge2ForkApp;
import Controller.RecipeController;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FridgeView extends StackPane {

    private Label errorLabel;

    //Nytt vi har tagt bort den gamla listan med checkboxar och skapat denna lista
    //med vboxar istället för att kunna dela upp ingredienserna i kategorier
    private List<VBox> categorySections = new ArrayList<>();

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
        //mainContent.getChildren().add(errorLabel);
        //mainContent.getChildren().add(errorLabel);

        //NYTT: vi spara nu varje kategori i categorysection direkt när de skapas
        //detta behvös ändras så att vi kan kolla vilka boxar som är ikryssade i alla kategorier

        //Kolhydrater
        categorySections.add(createCategorySection("Carbohydrates",  "Rice","Jasmine Rice", "Basmati Rice", "Brown Rice", "Pasta", "Spaghetti", "Macaroni",
                "Noodles", "Udon Noodles", "Egg Noodles", "Bread", "White Bread", "Ciabatta", "Baguette", "Flour", "Whole Wheat Flour", "Corn Flour", "Tortilla", "Wraps", "Couscous", "Quinoa", "Oats", "None"));

        //protein
        categorySections.add(createCategorySection("Protein", "Chicken", "Chicken Breast", "Chicken Thigh", "Beef", "Beef Brisket", "Minced Beef", "Steak", "Pork", "Pork Chops", "Pork Belly", "Lamb", "Lamb Shoulder", "Lamb Mince", "Turkey", "Duck", "Goat", "Bacon", "Ham", "Sausage",
                "Salmon", "Tuna", "Cod", "Haddock", "Sardines", "Anchovies", "Shrimp", "Prawns", "Crab", "Lobster", "Mussels", "Clams", "Squid", "Octopus", "Fish",
                "Tofu", "Tempeh", "Beans", "Lentils", "Chickpeas", "Quinoa",
                "Lentils", "Red Lentils", "Green Lentils", "Chickpeas", "Black Beans", "Kidney Beans", "White Beans", "Butter Beans", "Peas", "Split Peas", "None"));

        //Grönsaker
        categorySections.add(createCategorySection("Vegetables", "Onion", "Garlic", "Tomato", "Cherry Tomatoes", "Baby Plum Tomatoes", "Potato",
                "Sweet Potato", "Carrot", "Cabbage", "Red Cabbage",
                "Spinach", "Lettuce", "Broccoli", "Cauliflower", "Zucchini", "Eggplant", "Bell Pepper",
                "Green Pepper", "Red Pepper", "Chili", "Cucumber", "Leek", "Spring Onion", "Mushroom",
                "Pumpkin", "Squash", "Corn", "Peas", "Green Beans", "Okra", "Radish", "None"));

        //Frukter
        categorySections.add(createCategorySection("Fruits", "Apple", "Banana", "Orange", "Lemon", "Lime", "Mango", "Pineapple", "Coconut",
                "Strawberry", "Blueberry", "Raspberry", "Pear", "Peach", "Plum", "Apricot", "Fig", "Dates", "Avocado", "None"));

        //mejeri
        categorySections.add(createCategorySection("Dairy", "Milk", "Butter", "Cheese", "Cheddar", "Mozzarella",
                "Parmesan", "Feta", "Cream", "Double Cream", "Sour Cream", "Yogurt", "Greek Yogurt", "Custard", "Paneer", "Ricotta", "Mascarpone",
                "Ghee", "Creme Fraiche", "None"));



        //skafferi
        categorySections.add(createCategorySection("Pantry", "Olive oil", "Garlic", "Canned tomatoes",
                "Chickpeas", "Lentils", "Nuts", "None"));



        //örter och kryddor
        categorySections.add(createCategorySection("Spices & herbs", "Salt", "Black Pepper", "White Pepper", "Paprika", "Smoked Paprika",
                "Cumin", "Turmeric", "Curry Powder", "Chili Powder", "Cinnamon", "Cardamom", "Cloves", "Nutmeg", "Oregano", "Basil", "Parsley", "Thyme", "Rosemary",
                "Coriander", "Bay Leaves", "None"));



        //sås
        categorySections.add(createCategorySection("Sauce", "Soy Sauce", "Fish Sauce", "Oyster Sauce", "Tomato Sauce", "Ketchup", "Mayonnaise", "Mustard",
                "Vinegar", "Balsamic Vinegar", "Olive Oil", "Vegetable Oil", "Sesame Oil", "Hot Sauce", "Chili Sauce", "None"));


        categorySections.add(createCategorySection("Liquid", "Water", "Stock", "Chicken Stock",
                "Beef Stock", "Vegetable Stock", "Wine", "White Wine", "Red Wine", "Beer", "Coconut Milk", "None"));


        categorySections.add(createCategorySection("Nuts and seeds", "Almonds", "Cashews", "Peanuts",
                "Walnuts", "Hazelnuts", "Pistachios", "Sesame Seeds", "Sunflower Seeds", "Pumpkin Seeds", "None"));


        categorySections.add(createCategorySection("Other", "Eggs", "Breadcrumbs", "Gelatin",
                "Yeast", "Pasta Sheets", "Dough", "Pickles", "Olives", "None"));


        //NYTT vi lägger till hela listan av kategorier i vår huvudlayout
        mainContent.getChildren().addAll(categorySections);


        //Nästa-knappen
        Button nextButton = new Button("Next step");
        nextButton.setStyle("-fx-font-size: 16px; -fx-padding: 12 40; -fx-background-color: darkseagreen;" +
                "fx-text-fill: white; -fx-font-weight: bold;");
        nextButton.setCursor(javafx.scene.Cursor.HAND);
        nextButton.setOnAction(e -> handleNextStep());

        mainContent.getChildren().addAll(nextButton, errorLabel);

        //ScrollPane om listan blir lite för lång
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        this.getChildren().add(scrollPane);
    }

    //NYTT denna metod går in i en specifik kategori lista och hämtar
    //namnen på de ingredienser användaren valt

    private List<String> getSelectedFromSection(VBox section){
        List<String> selected = new ArrayList<>();

        //Nytt här hämtas flowpane där alla checkboxar är som ligger inuti kategorin
        FlowPane flow = (FlowPane) section.getChildren().get(1);

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


    private void handleNextStep(){
        //NYTT vi skapar en map , de nya formatet som controller kräver
        Map<String, List<String>> categoryMap = new HashMap<>();

        //vi loopar genom varje kategori en efter en
        for (VBox section : categorySections) {
            //hämta namnet på kategorin från rubriken label
            Label catLabel = (Label) section.getChildren().get(0);
            String categoryName = catLabel.getText();

            //Här hämtar vi ALLA valda rutor för att kontrollera kravet (även "None")
            List<String> allSelected = new ArrayList<>();
            FlowPane flow = (FlowPane) section.getChildren().get(1);

            for (javafx.scene.Node node : flow.getChildren()) {
                if (node instanceof CheckBox) {
                    CheckBox cb = (CheckBox) node;
                    if (cb.isSelected()) {
                        allSelected.add(cb.getText());
                    }
                }
            }
            //Här sker den obligatoriska kontrollen att minst ett val gjorts per kategori
            if (allSelected.isEmpty()) {
                showErrorMessage("Please select at least one option in: " + categoryName + " (choose 'None' if empty)");
                return;
            }

            //Här skapar vi en lista för de faktiska ingredienserna utan "None"
            List<String> realIngredients = new ArrayList<>();
            for (String item : allSelected) {
                if (!item.equalsIgnoreCase("None")) {
                    realIngredients.add(item);
                }
            }
            //Om det fanns riktiga ingredienser sparar vi dem i vår map
            if (!realIngredients.isEmpty()) {
                categoryMap.put(categoryName, realIngredients);
            }
        }
        clearErrorMessage();
        try {
            RecipeController controller = new RecipeController();
            //Vi skickar mappen (categoryMap)
            Fridge2ForkApp.root.setCenter(new DietView(categoryMap, controller));
        } catch (Exception ex) {
            showErrorMessage("Something went wrong when searching for recipes. Please try again!");
            ex.printStackTrace();
        }
    }

    private VBox createCategorySection(String categoryName, String... ingredients){
        VBox section = new VBox(10);
        Label catLabel = new Label(categoryName);
        catLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: darkseagreen;");

        FlowPane ingredientFlow = new FlowPane(15, 10);

        for(String ingredient : ingredients){
            CheckBox cb = new CheckBox(ingredient);
            cb.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
            ingredientFlow.getChildren().add(cb);
        }
        section.getChildren().addAll(catLabel, ingredientFlow);
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
