package View;

import App.Fridge2ForkApp;
import Model.Ingredient;
import Model.Recipe;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import java.util.List;

public class RecipeView extends StackPane {

    public RecipeView(Recipe recipe){

        //grundinställningar för vyn
        this.setPadding(new Insets(40));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: white;");

        //vbox som innehåller knappar texter och listor i en vertikal stapel
        VBox content = new VBox(20);
        content.setMaxWidth(800);
        content.setAlignment(Pos.TOP_LEFT);

        //Tillbaka knapp från en vald recept
        Button backButton = new Button ("← Back to Results");
        backButton.setStyle("-fx-background-color: darkseagreen; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setCursor(javafx.scene.Cursor.HAND);

        //Rubkrik: receptets namn och kommer från recipe objektet
        Label title = new Label(recipe.getName());
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: darkseagreen;");
        title.setWrapText(true);//radbyte ok om namnet är långt

        //ingrediens sektion
        VBox ingredientsBox = new VBox(10);
        Label ingredientsTitle = new Label("Ingredients");
        ingredientsTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        ingredientsBox.getChildren().add(ingredientsTitle);

        //här loopar vi igenom listan med ingredienser som finns lagrad i receptet
        //för varje ingrediens skapas en ny label med punkt framför
        List<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients != null) {
            for (Ingredient ing : ingredients){
                //här kombineras mått och namn
                String ingredientText = ing.getMeasure() + " " + ing.getName();
                Label ingLabel = new Label ("• " + ingredientText);
                ingLabel.setStyle("-fx-font-size: 16px;");
                ingredientsBox.getChildren().add(ingLabel);
            }
        }

        //instruktion sektion
        VBox instructionsBox = new VBox(10);
        Label instructionsTitle = new Label("Instructions");
        instructionsTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        //här hämtas instruktionstexten wraptext behövs!!! så texten ej fortsätter utanför fönstret kant
        Label instructionsText = new Label(recipe.getInstructions());
        instructionsText.setStyle("-fx-font-size: 16px; -fx-line-spacing: 5px;");
        instructionsText.setWrapText(true);

        instructionsBox.getChildren().addAll(instructionsTitle, instructionsText);

        //Scrollpane eftersom recepettet kan vara långt lägger vi vår innehåll i sne scrollpane
        //så att användaren kan skrolla ner för att fortsätta läsa.
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        //lägger till alla delar i innehållslådan
        content.getChildren().addAll(backButton, title, ingredientsBox, instructionsBox);

        //till sist lägger vi scroll-vyn i vår stackpane så att den visas på skärmen
        this.getChildren().add(scrollPane);
    }
}
