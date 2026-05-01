package org.example;

import App.Fridge2ForkApp;
import View.HomeView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class RegisterView extends VBox {

    public RegisterView() {

        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);

        Label title = new Label("SIGN UP");

        TextField username = new TextField();
        username.setPromptText("Username");
        username.setMaxWidth(200);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setMaxWidth(200);

        Button registerBtn = new Button("Create account");
        Button backBtn = new Button("Back");

        registerBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new HomeView());
        });

        backBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new HomeView());
        });

        this.getChildren().addAll(title, username, password, registerBtn, backBtn);
    }
}