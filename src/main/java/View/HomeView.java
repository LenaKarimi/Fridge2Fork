package View;

import App.Fridge2ForkApp;
import Controller.UserController;
import DTO.ProfileDTO;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
    private ImageView fridgeImageView;

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
                    "We will then help you find six delicious recipes that matches what you have in at least" +
                    " 50% that you can cook right now!");
            alert.showAndWait();
        });

        //knapp + ikon i samma rad
        HBox buttonBox = new HBox(15, BSubText, homeInfoIcon);
        buttonBox.setAlignment(Pos.CENTER);

        //välkomsttext + bild + knapp (overlay för allt)
        StackPane centerStack = new StackPane();
        centerStack.setAlignment(Pos.CENTER);

        //ladda bilden (synkront för felsökning)
        String fridgeImageUrl = "https://images.stockcake.com/public/e/5/5/e557df37-e25c-42d9-ad93-9a9d692580d2_large/stocked-fridge-interior-stockcake.jpg";
        Image fridgeImage = new Image(fridgeImageUrl, false);
        ImageView bg = new ImageView(fridgeImage);

        //bilden ska fylla hela ytan
        bg.setPreserveRatio(true); //false betyder täck hela området
        bg.setSmooth(true);

        //bind imageview till homeview så den alltid fyller bakgrunden
        bg.fitWidthProperty().bind(this.widthProperty());
        bg.fitHeightProperty().bind(this.heightProperty());

        //skugga/effekt
        bg.setStyle("-fx-opacity: 0.95;");

        //overlay, text + knapp i en VBox (transparent bakgrund så man ser bilden bakom)
        VBox overlay = new VBox(20);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(40)); //ger lite luft från toppen
        overlay.setMaxWidth(800); //begränsa bredd på innehåll så det inte blir för utspritt

        //welcomeLabel, subText och knapp
        overlay.getChildren().addAll(welcomeLabel, subText, buttonBox);

        //styling på overlay så texten syns mot bilden
        welcomeLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #2b2b2b;");
        subText.setStyle("-fx-font-size: 16px; -fx-text-fill: #444;");

        //först bakgrunden, sedan overlay, den hamnar ovanpå och fångar musklick
        centerStack.getChildren().addAll(bg, overlay);

        //placera StackPane i center
        this.setCenter(centerStack);

        //om användare redan är inloggad när denna view skapas så visas välkomstmeddelande direkt
        if (userController != null && userController.getCurrentUser() != null) {
            //konstruktor kallar setup för att visa namnet och ta bort knapparna
            setupLoggedInState(userController.getCurrentUser());
        }

    }

    /**
     * Skapar och laddar ImageView för startsidans kylskåpsbild.
     * Laddar synkront för att lätt upptäcka fel
     */
    private ImageView createAndLoadFridgeImage() {
        try {
            String fridgeImageUrl = "https://images.stockcake.com/public/e/5/5/e557df37-e25c-42d9-ad93-9a9d692580d2_large/stocked-fridge-interior-stockcake.jpg";

            //ladda bild
            Image fridgeImage = new Image(fridgeImageUrl, false);

            if (fridgeImage.isError()) {
                System.out.println("HomeView: FAILED TO LOAD FRIDGE IMAGE -> " + fridgeImage.getException());
                return null;
            }

            //skapa ImageView
            ImageView iv = new ImageView(fridgeImage);
            iv.setFitWidth(360);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            iv.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");

            return iv;
        } catch (Exception ex) {
            System.out.println("HomeView: Exception while loading fridge image -> " + ex);
            return null;
        }
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
