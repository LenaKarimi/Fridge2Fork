package View;

import App.Fridge2ForkApp;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class HomeView extends BorderPane {

    private Label welcomeLabel;

    public HomeView(){
        this.setPadding(new Insets(20));

        //Inlogg och registrering
        HBox authButtons = new HBox(10);
        authButtons.setAlignment(Pos.TOP_RIGHT);
        Button loginBtn = new Button("Logga in");
        Button registerBtn = new Button("Skapa konto");
        //transparent färg för aesthetics lol
        loginBtn.setStyle("-fx-background-color: transparent; -fx-border-color: tan; -fx-border-radius: 5;");
        registerBtn.setStyle("-fx-background-color: tan; -fx-text-fill: white; -fx-background-radius: 5;");

        //Musen
        loginBtn.setCursor(javafx.scene.Cursor.HAND);
        registerBtn.setCursor(javafx.scene.Cursor.HAND);
        authButtons.getChildren().addAll(loginBtn, registerBtn);
        this.setTop(authButtons); //så raden läggs längst upp

        //Välkomstmeddelande och huvudknapp
        welcomeLabel = new Label("Välkommen till Fridge2Fork!");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");

        Label subText = new Label("Upptäck recept baserade på vad du redan har hemma");
        subText.setStyle("-fx-font-size: 16px;");

        //Knappen
        Button BSubText = new Button("Vad har du hemma?");
        BSubText.setStyle("-fx-font-size: 14px; -fx-padding: 10 20 10 20;");
        BSubText.setCursor(javafx.scene.Cursor.HAND);
        BSubText.setOnAction(e ->{
            Fridge2ForkApp.root.setCenter(new FridgeView());
        });

        VBox centercontent = new VBox(20, welcomeLabel, subText, BSubText);
        centercontent.setAlignment(Pos.CENTER);
        this.setCenter(centercontent);

    }

    //Tänkt att anropa för inloggning
    public void setUserName(String name){
        welcomeLabel.setText("Välkommen, " + name + "!");
    }
}
