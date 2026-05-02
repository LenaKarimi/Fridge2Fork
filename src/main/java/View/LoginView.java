package org.example;


import App.Fridge2ForkApp;
import Controller.UserController;
import View.HomeView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class LoginView extends VBox {

    public LoginView() {

        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);

        Label title = new Label("LOGIN");

        TextField username = new TextField();
        username.setPromptText("Username");
        username.setMaxWidth(200);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setMaxWidth(200);

        Button loginBtn = new Button("Login");
        Button backBtn = new Button("Back");

        loginBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new HomeView());
        });

        backBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new HomeView());
        });

        this.getChildren().addAll(title, username, password, loginBtn, backBtn);
    }
}
