package View;

import App.Fridge2ForkApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class RegisterView extends VBox {

    public RegisterView(){
        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);

        Label title = new Label("Sign up");

        TextField userName = new TextField();
        userName.setPromptText("Username");
        userName.setMaxWidth(200);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setMaxWidth(200);

        Button registerButton = new Button("Create account");
        Button backButton = new Button("Back");


        registerButton.setOnAction(e -> {
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
                registerButton,
                backButton
        );
    }
}
