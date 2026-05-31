package View;

import App.Fridge2ForkApp;
import Controller.UserController;
import View.HomeView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import View.*;

import javax.swing.*;

/**
 * View for registering a new user account.
 * Displays input fields for username, password, name and email.
 * @author Lena and Racil
 */
public class RegisterView extends VBox {

    /**
     * Constructs a RegisterView and builds the UI.
     * @param userController handles the logic of user registration
     */
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

        Label errorLable = new Label();
        errorLable.setStyle("-fx-text-fill: red;");

        Button registerBtn = new Button("Create account");
        Button backBtn = new Button("Back");

        registerBtn.setOnAction(event -> {
            String error = userController.registerUser(username.getText(), password.getText(), name.getText(), email.getText());

            if (error != null) {
                errorLable.setText(error);
            } else {
                Fridge2ForkApp.root.setCenter(new LoginView(userController));
            }
        });
        backBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new HomeView(userController));

        });

        this.getChildren().addAll(title, username, password, name, email, errorLable, registerBtn, backBtn);
        javafx.application.Platform.runLater(() -> this.requestFocus());
    }
}