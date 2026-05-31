package View;


import App.Fridge2ForkApp;
import Controller.UserController;
import DTO.ProfileDTO;
import View.HomeView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

/**
 * View for logging in to an exiting user account.
 * Displays input fields for username and password.
 * @author Lena, Racil and Maya
 */

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

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("Login");
        Button backBtn = new Button("Back");

        loginBtn.setOnAction(event -> {
            ProfileDTO isUser = userController.login(username.getText(), password.getText());

            if (isUser == null) {
                errorLabel.setText(userController.getLoginError());
            } else {
                HomeView homeView = new HomeView(userController);
                homeView.setupLoggedInState(isUser);

                Fridge2ForkApp.root.setCenter(homeView);
                Fridge2ForkApp.root.setLeft(new SideBarView(userController));
            }
        });

        backBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new HomeView(userController));
        });

        this.getChildren().addAll(title, username, password, errorLabel, loginBtn, backBtn);

        javafx.application.Platform.runLater(() -> this.requestFocus());
    }
}
