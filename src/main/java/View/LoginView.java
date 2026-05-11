package org.example;


import App.Fridge2ForkApp;
import Controller.UserController;
import DTO.ProfileDTO;
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

        Label errorLable = new Label();
        errorLable.setStyle("-fx-text-fill: red;"); // fellable skrivit av racil

        Button loginBtn = new Button("Login");
        Button backBtn = new Button("Back");

        loginBtn.setOnAction(event -> {
            ProfileDTO isUser = userController.login(username.getText(), password.getText());

            if (isUser == null) {
                errorLable.setText(userController.getLoginError());
            } else {
                HomeView homeView = new HomeView(userController);
                homeView.setUserName(isUser.getName());
                Fridge2ForkApp.root.setCenter(homeView);
            }
        });

        /** //orginalmetod
        loginBtn.setOnAction(e -> {
            String user = username.getText();
            String pass = password.getText();

            ProfileDTO isUser = userController.login(user,pass);
            if (isUser != null){// ska var success
                HomeView homeView = new HomeView(userController);
                homeView.setUserName(isUser.getName());
                Fridge2ForkApp.root.setCenter(homeView);
            }
            else {
                //vi behöver bestämma hur vi ska visa felmeddelande
            }

        });
         */

        backBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new HomeView(userController));
        });

        this.getChildren().addAll(title, username, password, errorLable, loginBtn, backBtn);
    }
}
