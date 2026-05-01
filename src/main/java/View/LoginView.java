package View;

import App.Fridge2ForkApp;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class LoginView extends VBox {

    public LoginView(){
        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);

        Label title = new Label("Log in");

        TextField userName = new TextField();
        userName.setPromptText("Username");
        userName.setMaxWidth(200);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setMaxWidth(200);

        Button loginButton = new Button("Log in");
        Button backButton = new Button("Back");

        loginButton.setOnAction(e -> {
            HomeView home = new HomeView();
            home.setUserName(userName.getText());
            Fridge2ForkApp.root.setCenter(home);
        });

        backButton.setOnAction(e ->{
            Fridge2ForkApp.root.setCenter(new HomeView());
        });

        this.getChildren().addAll(
             title,
             userName,
             password,
             loginButton,
             backButton
        );
    }
}
