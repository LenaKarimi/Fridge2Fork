package View;

import App.Fridge2ForkApp;
import Controller.UserController;
import DTO.ProfileDTO;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;


public class HomeView extends BorderPane {

    private Label welcomeLabel;
    private HBox authButtons;

    public HomeView(UserController userController){
        this.setPadding(new Insets(20));

        //Inlogg och registrering
        HBox authButtons = new HBox(10);
        authButtons.setAlignment(Pos.TOP_RIGHT);

        Button loginBtn = new Button("Log in");
        Button registerBtn = new Button("Create account");

        //actions
        loginBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new LoginView(userController));
        });

        registerBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new RegisterView(userController));
        });

        //transparent färg för aesthetics lol
        loginBtn.setStyle("-fx-background-color: transparent; -fx-border-color: tan; -fx-border-radius: 5;");
        registerBtn.setStyle("-fx-background-color: tan; -fx-text-fill: white; -fx-background-radius: 5;");

        //Musen
        loginBtn.setCursor(javafx.scene.Cursor.HAND);
        registerBtn.setCursor(javafx.scene.Cursor.HAND);

        authButtons.getChildren().addAll(loginBtn, registerBtn);
        this.setTop(authButtons); //så raden läggs längst upp

        //Välkomstmeddelande och huvudknapp
        welcomeLabel = new Label("Welcome to Fridge2Fork!");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold");

        Label subText = new Label("Discover recipes based on what you already have at home.");
        subText.setStyle("-fx-font-size: 16px;");

        //Knappen
        Button BSubText = new Button("What's in your fridge?");
        BSubText.setStyle("-fx-font-size: 14px; -fx-padding: 10 20 10 20;");
        BSubText.setCursor(javafx.scene.Cursor.HAND);
        BSubText.setOnAction(e ->{
            Fridge2ForkApp.root.setCenter(new FridgeView());
        });

        VBox centercontent = new VBox(20, welcomeLabel, subText, BSubText);
        centercontent.setAlignment(Pos.CENTER);
        this.setCenter(centercontent);

    }

    //Tänkt att anropas för inloggning
   public void setupLoggedInState(ProfileDTO user){
        //uppdatera välkomstmeddelande med usernamet
       welcomeLabel.setText("Welcome " + user.getName() + " !");

       //ta bort inloggningsknappar helt
       authButtons.getChildren().clear();

       //text istället för knapparna så man vet man är inloggad
       Label loggedInAs = new Label("Logged in as " +  user.getName());
       loggedInAs.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
       authButtons.getChildren().add(loggedInAs);
   }

}
