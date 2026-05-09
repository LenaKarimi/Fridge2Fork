package View;

import App.Fridge2ForkApp;
import Model.Ingredient;
import Model.Recipe;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Controller.UserController;
import DTO.ProfileDTO;
import java.util.List;


public class RecipeView extends StackPane {

    //NYTT vi sparar en referens till resultatlistan så vi kan hoppa tillbaka till den
    private final RecipeResultsView previousView;
    //sparar userController för att kolla inloggningsstatus
    private final UserController userController;

    public RecipeView(Recipe recipe, RecipeResultsView previousView, UserController userController) {
        this.previousView = previousView;
        this.userController = userController;

        //grundinställningar för vyn
        this.setPadding(new Insets(40));
        this.setAlignment(Pos.TOP_CENTER);
        this.setStyle("-fx-background-color: white;");

        //vbox som innehåller knappar texter och listor i en vertikal stapel
        VBox content = new VBox(20);
        content.setMaxWidth(800);
        content.setAlignment(Pos.TOP_LEFT);

        //Tillbaka knapp från en vald recept
        Button backButton = new Button("← Back to Results");
        backButton.setStyle("-fx-background-color: darkseagreen; -fx-text-fill: white; -fx-font-weight: bold;");
        backButton.setCursor(javafx.scene.Cursor.HAND);

        //detta är logiken för att kunna gå tillbaka
        backButton.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(previousView);
        });

        //skapa Like-knappen med unicode hjärta
        Button likeButton = new Button("\u2661 Like"); //\u2661 = tomt hjärta, \u2665 = fyllt hjärta
        likeButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-border-color: red;" +
                " -fx-border-radius: 5; -fx-text-fill: red;");
        likeButton.setCursor(javafx.scene.Cursor.HAND);

        //visa bara knappen om användaren är inloggad
        ProfileDTO currentUser = userController.getCurrentUser();
        if (currentUser == null) {
            likeButton.setVisible(false); // Dölj om inte inloggad
        }

        //vid like-tryck (Förberett för Racils DAO sen)
        likeButton.setOnAction(e -> {
            if (likeButton.getText().equals("Liked")) {
                likeButton.setText("Liked");
                likeButton.setStyle("-fx-font-size: 16px; -fx-background-color: red; -fx-text-fill: white;" +
                        " -fx-background-radius: 5;");
                System.out.println("Sparar recept " + recipe.getName() + " för användare " + currentUser.getUsername());
                //här anropas DAO:n av Racil
            } else {
                likeButton.setText("Like");
                likeButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-border-color: red;" +
                        " -fx-border-radius: 5; -fx-text-fill: red;");
                System.out.println("Tar bort recept " + recipe.getName());
            }
        });

        //Detta är logken för att kunna gå tillbaka
        backButton.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(previousView);
        });

        //Rubkrik: receptets namn och kommer från recipe objektet
        Label title = new Label(recipe.getName());
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: darkseagreen;");
        title.setWrapText(true);//radbyte ok om namnet är långt

        //BILD
        //här ska det synas hur rätten ser ut och bilden hämtas frånr ecpeptobjekte
        ImageView recipeImageView = new ImageView();
        String imageUrl = recipe.getImageUrl();

        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                Image image = new Image(imageUrl, true);
                recipeImageView.setImage(image);
                recipeImageView.setFitWidth(500);
                //NYTT anpassa bildstorlek
                recipeImageView.setFitHeight(300);
                recipeImageView.setPreserveRatio(true);
                recipeImageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
            } catch (Exception e) {
                System.out.println("Kunde inte ladda receptbilden: " + e.getMessage());
            }
        }


        //ingrediens sektion
        VBox ingredientsBox = new VBox(10);

        //tvinga boxen att synas med en minsta bredd
        ingredientsBox.setMinWidth(400);
        Label ingredientsTitle = new Label("Ingredients");
        ingredientsTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        ingredientsBox.getChildren().add(ingredientsTitle);

        //här loopar vi igenom listan med ingredienser som finns lagrad i receptet
        //för varje ingrediens skapas en ny label med punkt framför
        System.out.println("DEBUG: Antal ingredienser i receptobjektet: " + (recipe.getIngredients() != null ? recipe.getIngredients().size() : "NULL"));
        List<Ingredient> ingredients = recipe.getIngredients();
        if (ingredients != null) {
            for (Ingredient ing : ingredients) {
                //här kombineras mått och namn
                String ingredientText = ing.getMeasure() + " " + ing.getName();
                Label ingLabel = new Label("• " + ingredientText);
                ingLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
                ingredientsBox.getChildren().add(ingLabel);
            }
        }

        //instruktionssektion
        VBox instructionsBox = new VBox(10);
        Label instructionsTitle = new Label("Instructions");
        instructionsTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        //Här hämntas texten och kollas om det faktiskt finns
        String instrText = recipe.getInstructions();
        if (instrText == null || instrText.isBlank()) {
            instrText = " No instruction found for this recipe. Please try another one!";
        }

        //Vi använder instrText istället för direktanropet och sätter svart färg
        //här hämtas instruktionstexten wraptext behövs!!! så texten ej fortsätter utanför fönstret kant
        Text instructionsText = new Text(instrText);
        instructionsText.setStyle("-fx-font-size: 16px; -fx-fill: black;");

        //här tvingas radbrytning vid fönsterkanten
        instructionsText.setWrappingWidth(750);

        instructionsBox.getChildren().addAll(instructionsTitle, instructionsText);

        //Bild + ingredienser bredvid varandra
        HBox imageAndIngredients = new HBox(40, recipeImageView, ingredientsBox);
        imageAndIngredients.setAlignment(Pos.TOP_LEFT);

        content.getChildren().addAll(backButton, title, imageAndIngredients, instructionsBox);


        //Scrollpane eftersom recepettet kan vara långt lägger vi vår innehåll i scrollpane
        //så att användaren kan skrolla ner för att fortsätta läsa.
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: white; -fx-background: white;");


        //till sist lägger vi scroll-vyn i vår stackpane så att den visas på skärmen
        this.getChildren().add(scrollPane);

        //DETTA SYNS O KONSOLEN
        System.out.println("Visar recept: " + recipe.getName());
        //?= är frågan sann, om ja skrivs ja ut on falsk skrivs nej (null
        //DETTA MÅSTE VARA KVAR!!, för att säkerställa i konsolen att mealmapper och api gör sitt jobb annars vet
        //vi att det behöver hanteras i view
        System.out.println("Instruktioner i objektet: " + (recipe.getInstructions() != null ? "JA" : "NEJ (NULL)"));
    }
}
