package org.example;


import App.Fridge2ForkApp;
import Controller.UserController;
import View.HomeView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class LoginView extends VBox {

    public LoginView(UserController userController) {

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
            String user = username.getText();
            String pass = password.getText();

            //boolean success = userController.login(user,pass);
            if (true){ // ska var success
                Fridge2ForkApp.root.setCenter(new HomeView(userController));
            }
            else {
                //vi behöver bestämma hur vi ska visa felmeddelande
            }

        });

        backBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new HomeView(userController));
        });

        this.getChildren().addAll(title, username, password, loginBtn, backBtn);
    }
}
