package View;


import App.Fridge2ForkApp;
import Controller.UserController;
import DTO.ProfileDTO;
import View.HomeView;
import javafx.scene.Cursor;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.paint.Color;

public class LoginView extends VBox {

    public LoginView(UserController userController) {

        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);

        Label title = new Label("LOGIN");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold");

        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(200);

        TextField usernameInput = new TextField();
        usernameInput.setPromptText("Username");
        usernameInput.setMaxWidth(200);

        PasswordField passwordInput = new PasswordField();
        passwordInput.setPromptText("Password");
        passwordInput.setMaxWidth(200);

        Button loginBtn = new Button("Login");
        loginBtn.setMinWidth(100);
        loginBtn.setCursor(Cursor.HAND);

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: gray;");
        backBtn.setCursor(Cursor.HAND);

        //Login
        loginBtn.setOnAction(e -> {
            String user = usernameInput.getText().trim();
            String pass = passwordInput.getText().trim();

            //Avbryt om tomt
            if (user.isEmpty() || pass.isEmpty()) {
                errorLabel.setText("Please enter both username and password.");
                return;
            }

            //Försök logga in via controllern
            ProfileDTO loggedInUser = userController.login(user, pass);

            if (loggedInUser != null) {
                //om inte tom, skapa HomeView och sätt välkomstmeddelandet och byt center.

                //skapa hemvy
                HomeView homeView = new HomeView(userController);

                //sätt i "inloggat-läge"
                homeView.setupLoggedInState(loggedInUser);

                //visa den
                Fridge2ForkApp.root.setCenter(homeView);

            } else {
                //vid misslyckad inlogg, visa felmeddelande
                errorLabel.setText("Invalid username or password.");
                //rensa lösenord för säkerhets skull
                passwordInput.clear();
            }
        });

        backBtn.setOnAction(e -> {
            //skicka tbx användaren till HomeView
            Fridge2ForkApp.root.setCenter(new HomeView(userController));
        });

        //tryck enter för login
        this.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                loginBtn.fire();
            }
        });

        this.getChildren().addAll(title, errorLabel, usernameInput, passwordInput, loginBtn, backBtn);
    }
}
