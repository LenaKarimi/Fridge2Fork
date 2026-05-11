package View;

import App.Fridge2ForkApp;
import Controller.UserController;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class RegisterView extends VBox {

    public RegisterView(UserController userController) {

        this.setSpacing(10);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);

        Label title = new Label("SIGN UP");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label statusLabel = new Label();
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(250);

        TextField username = new TextField();
        username.setPromptText("Username");
        username.setMaxWidth(200);

        PasswordField password = new PasswordField();
        password.setPromptText("Password (min 12 chars, A, a, 1)");
        password.setMaxWidth(200);

        TextField name = new TextField();
        name.setPromptText("Full name");
        name.setMaxWidth(200);

        TextField email = new TextField();
        email.setPromptText("Email");
        email.setMaxWidth(200);

        Button registerBtn = new Button("Create account");
        registerBtn.setMinWidth(150);
        registerBtn.setCursor(Cursor.HAND);

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: gray;");
        backBtn.setCursor(Cursor.HAND);

        registerBtn.setOnAction(e -> {
            String user = username.getText().trim();
            String pass = password.getText().trim();
            String fullName = name.getText().trim();
            String emailText = email.getText().trim();

            //kollar tomma fält
            if (user.isEmpty() || pass.isEmpty() || fullName.isEmpty() || emailText.isEmpty()){
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Please fill in all fields.");
                return;
            }

            //kollar lösenordskrav
            if (!isValidPassword(pass)){
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Password must be 12+ chars and include a capital letter," +
                        " a lowercase letter and a number.");
                return;
            }

            //försök registrera via controller
            boolean success = userController.registerUser(user, pass, fullName, emailText);

            if (success){
                statusLabel.setTextFill(Color.GREEN);
                statusLabel.setText("Account created! You can now log in.");
                username.clear();
                password.clear();
                name.clear();
                email.clear();
            } else {
                statusLabel.setTextFill(Color.RED);
                statusLabel.setText("Username already exists or something went wrong.");
            }
        });

        backBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new HomeView(userController));
        });

        this.getChildren().addAll(title, statusLabel, username, password, name, email, registerBtn, backBtn);
    }

    //Validerar lösenord, 12 tecken, stor, liten, siffra
    private boolean isValidPassword(String password){
        if (password.length() < 12) return false;

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()){
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
        }

        return hasUpper && hasLower && hasDigit;
    }
}