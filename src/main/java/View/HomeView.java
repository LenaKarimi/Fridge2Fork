package View;

import App.Fridge2ForkApp;
import Controller.UserController;
import DTO.ProfileDTO;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.util.Duration;


public class HomeView extends BorderPane {

    private Label welcomeLabel;
    private HBox authButtons;
    private UserController userController;

    public HomeView(UserController userController){
        this.userController = userController;
        this.setPadding(new Insets(20));

        //Inlogg och registrering
        authButtons = new HBox(10);
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

        Button BSubText = new Button("What's in your fridge?");
        BSubText.setStyle("-fx-font-size: 14px; -fx-padding: 10 20 10 20;");
        BSubText.setCursor(javafx.scene.Cursor.HAND);
        BSubText.setOnAction(e -> Fridge2ForkApp.root.setCenter(new FridgeView(userController)));

        //info-ikonen
        Label homeInfoIcon = new Label("i");
        homeInfoIcon.setStyle(
                "-fx-background-color: darkseagreen; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-family: 'Serif'; " +
                        "-fx-font-weight: bold; " +
                        "-fx-font-size: 14px; " +
                        "-fx-shape: 'M 10 0 A 10 10 0 1 0 10 20 A 10 10 0 1 0 10 0'; " +
                        "-fx-min-width: 20px; -fx-min-height: 20px; " +
                        "-fx-alignment: center; -fx-cursor: help;"
        );

        //tooltip vid hover
        Tooltip homeTooltip = new Tooltip("Click to start searching for recipes based on your ingredients!");
        homeTooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(homeInfoIcon, homeTooltip);

        //klickbar för mer info
        homeInfoIcon.setOnMouseClicked(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Getting Started");
            alert.setHeaderText("Welcome to Fridge2Fork!");
            alert.setContentText("Click the button to list the ingredients you have at home. " +
                    "We will then help you find delicious recipes you can cook right now!");
            alert.showAndWait();
        });

        //knapp + ikon i samma rad
        HBox buttonBox = new HBox(15, BSubText, homeInfoIcon);
        buttonBox.setAlignment(Pos.CENTER);

        VBox centercontent = new VBox(20, welcomeLabel, subText, buttonBox);
        centercontent.setAlignment(Pos.CENTER);
        this.setCenter(centercontent);

    }

    //Anropas vid inloggning/utloggning
   public void setupLoggedInState(ProfileDTO user){
        //Vid inloggning: uppdatera välkomstmeddelande med usernamet
       welcomeLabel.setText("Welcome " + user.getName() + "!");

       //ta bort inloggningsknappar helt
       authButtons.getChildren().clear();

       //text istället för knapparna så man vet man är inloggad
       Label loggedInAs = new Label("Logged in as " +  user.getName());
       loggedInAs.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
       authButtons.getChildren().add(loggedInAs);

       //utloggning
       Button logoutBtn = new Button("Log out");
       logoutBtn.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-radius: 5;" +
               "-fx-text-fill: red;");
       logoutBtn.setCursor(Cursor.HAND);

       //vid utloggning, återställ till ursprungsläge
       logoutBtn.setOnAction(e -> {
           welcomeLabel.setText("Welcome to Fridge2Fork!");
           authButtons.getChildren().clear();

           Button loginBtn = new Button("Log in");
           Button registerBtn = new Button("Create account");

           loginBtn.setStyle("-fx-background-color: transparent; -fx-border-color: tan;" +
                   "-fx-border-radius: 5;");
           registerBtn.setStyle("-fx-background-color: tan; -fx-text-fill: white;" +
                   "-fx-background-radius: 5;");
           loginBtn.setCursor(Cursor.HAND);
           registerBtn.setCursor(Cursor.HAND);

           loginBtn.setOnAction(ev -> Fridge2ForkApp.root.setCenter(new LoginView(userController)));
           registerBtn.setOnAction(ev -> Fridge2ForkApp.root.setCenter(new RegisterView(userController)));

           authButtons.getChildren().addAll(loginBtn, registerBtn);

           //återställ profilbilden i sidebaren + jag gjorde debugging
           System.out.println("sideBar är: " + Fridge2ForkApp.sideBar);
           userController.logout();
           Fridge2ForkApp.sideBar.resetProfilePicture();
       });
   }
}
