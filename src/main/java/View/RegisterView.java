package org.example;

import App.Fridge2ForkApp;
import Controller.UserController;
import View.HomeView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import javax.swing.*;

public class RegisterView extends VBox {

    public RegisterView(UserController userController) {

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

        TextField name = new TextField();
        name.setPromptText("Name");
        name.setMaxWidth(200);

        TextField email = new TextField();
        email.setPromptText("Email");
        email.setMaxWidth(200);

        Button registerBtn = new Button("Create account");
        Button backBtn = new Button("Back");

        registerBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new HomeView(userController));
        });

        backBtn.setOnAction(e -> {
            String user = username.getText();
            String pass = password.getText();
            String firstName = name.getText();
            String emailAdress = email.getText();

            userController.registerUser(user,pass,firstName,emailAdress);
            Fridge2ForkApp.root.setCenter(new HomeView(userController));
        });

        this.getChildren().addAll(title, username, password, name, email, registerBtn, backBtn);
    }
}