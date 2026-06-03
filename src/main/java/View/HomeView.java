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

/**
 * The main home screen of the application.
 * Displays a welcome message, background image, and a navigation to the fridge ingredient view.
 * Shows login/register buttons when logged out, and user information when logged in.
 * @author Maya
 */
public class HomeView extends BorderPane {
    private Label welcomeLabel;
    private HBox authButtons;
    private UserController userController;
    private ImageView fridgeImageView;

    /**
     * Constructs the HomeView and builds the UI.
     * @param userController provides the current user
     */
    public HomeView(UserController userController){
        this.userController = userController;
        this.setPadding(new Insets(20));

        authButtons = new HBox(10);
        authButtons.setAlignment(Pos.TOP_RIGHT);

        Button loginBtn = new Button("Log in");
        Button registerBtn = new Button("Create account");

        loginBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new LoginView(userController, "Home"));
        });

        registerBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new RegisterView(userController));
        });

        loginBtn.setStyle("-fx-background-color: white; -fx-border-color: darkseagreen; -fx-border-radius: 2;-fx-border-radius: 6; -fx-text-fill: darkseagreen; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 6 18;");
        registerBtn.setStyle("-fx-background-color: darkseagreen; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 6 18;");

        loginBtn.setCursor(javafx.scene.Cursor.HAND);
        registerBtn.setCursor(javafx.scene.Cursor.HAND);

        authButtons.getChildren().addAll(loginBtn, registerBtn);
        this.setTop(authButtons);

        welcomeLabel = new Label("Welcome to Fridge2Fork!");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold");

        Label subText = new Label("Discover recipes based on what you already have at home.");
        subText.setStyle("-fx-font-size: 16px;");

        Button BSubText = new Button("What's in your fridge?");
        BSubText.setStyle("-fx-font-size: 14px; -fx-padding: 10 20 10 20;");
        BSubText.setCursor(javafx.scene.Cursor.HAND);
        BSubText.setOnAction(e -> Fridge2ForkApp.root.setCenter(new FridgeView(userController)));

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

        Tooltip homeTooltip = new Tooltip("Click to start searching for recipes based on your ingredients!");
        homeTooltip.setShowDelay(Duration.millis(100));
        Tooltip.install(homeInfoIcon, homeTooltip);

        homeInfoIcon.setOnMouseClicked(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Getting Started");
            alert.setHeaderText("Welcome to Fridge2Fork!");
            alert.setContentText("Click the button to list the ingredients you have at home. " +
                    "We will then help you find six delicious recipes that matches what you have in at least" +
                    " 50% that you can cook right now!");
            alert.showAndWait();
        });

        HBox buttonBox = new HBox(15, BSubText, homeInfoIcon);
        buttonBox.setAlignment(Pos.CENTER);

        StackPane centerStack = new StackPane();
        centerStack.setAlignment(Pos.CENTER);

        Image fridgeImage = new Image(
                getClass().getResourceAsStream("/images/fridge.png")
        );
        ImageView bg = new ImageView(fridgeImage);

        bg.setPreserveRatio(true);
        bg.setSmooth(true);
        bg.setFitHeight(615);
        bg.setOpacity(0.3);

        VBox overlay = new VBox(20);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPadding(new Insets(30, 50, 30, 50));
        overlay.setMaxWidth(600);
        overlay.getChildren().addAll(welcomeLabel, subText, buttonBox);
        welcomeLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #2b2b2b;");
        subText.setStyle("-fx-font-size: 16px;  -fx-font-weight: bold; -fx-text-fill: #1a1a1a;"
        +  " -fx-background-color: rgba(143,188,143,0.80); -fx-background-radius: 8; -fx-padding: 4 12;" );

        centerStack.getChildren().addAll(bg, overlay);
        this.setCenter(centerStack);

        if (userController != null && userController.getCurrentUser() != null) {
            setupLoggedInState(userController.getCurrentUser());
        }

    }

    /**
     * Loads the fridge background image from a URL.
     * Returns null if the imange fails to load.
     * @return an ImageView with the fridge image, or null on failure
     */
    private ImageView createAndLoadFridgeImage() {
        try {
            String fridgeImageUrl = "https://images.stockcake.com/public/e/5/5/e557df37-e25c-42d9-ad93-9a9d692580d2_large/stocked-fridge-interior-stockcake.jpg";

            Image fridgeImage = new Image(fridgeImageUrl, false);
            if (fridgeImage.isError()) {
                System.out.println("HomeView: FAILED TO LOAD FRIDGE IMAGE -> " + fridgeImage.getException());
                return null;
            }

            ImageView iv = new ImageView(fridgeImage);
            iv.setFitWidth(360);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            iv.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");

            return iv;
        }
        catch (Exception ex) {
            System.out.println("HomeView: Exception while loading fridge image -> " + ex);
            return null;
        }
    }

    /**
     * Updates the UI to reflect on a logged in state.
     * Replaces login/register button with the users name and a logout button.
     * @param user the currenttly logged in user
     */
   public void setupLoggedInState(ProfileDTO user){
       welcomeLabel.setText("Welcome " + user.getName() + "!");

       authButtons.getChildren().clear();

       Label loggedInAs = new Label("Logged in as " +  user.getName());
       loggedInAs.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
       authButtons.getChildren().add(loggedInAs);

       Button logoutBtn = new Button("Log out");
       logoutBtn.setStyle("-fx-background-color: transparent; -fx-border-color: red; -fx-border-radius: 5;" +
               "-fx-text-fill: red;");
       logoutBtn.setCursor(Cursor.HAND);

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

           loginBtn.setOnAction(ev -> Fridge2ForkApp.root.setCenter(new LoginView(userController, "Home")));
           registerBtn.setOnAction(ev -> Fridge2ForkApp.root.setCenter(new RegisterView(userController)));

           authButtons.getChildren().addAll(loginBtn, registerBtn);

           System.out.println("sideBar är: " + Fridge2ForkApp.sideBar);
           userController.logout();
           Fridge2ForkApp.sideBar.resetProfilePicture();

       });
   }
}
