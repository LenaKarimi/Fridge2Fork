package View;


import App.Fridge2ForkApp;
import Controller.LikedRecipeController;
import Controller.PantryController;
import Controller.UserController;
import DTO.ProfileDTO;
import Model.Recipe;
import View.HomeView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.util.List;

/**
 * View for logging in to an exiting user account.
 * Displays input fields for username and password.
 * @author Lena, Racil and Maya
 */

public class LoginView extends VBox {
    /**
     * Constructs the LoginView and builds the UI.
     * @param userController handles login logic and user state
     * @param destination the view to navigate to after successful login
     */
    public LoginView(UserController userController, String destination) {
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
            }
            else {
                Fridge2ForkApp.root.setLeft(new SideBarView(userController));

                if (destination.equals("Liked recipes")){
                    LikedRecipeController likedRecipeController = new LikedRecipeController();
                    int profilId = userController.getCurrentUser().getId();
                    List<Recipe> likedRecipes = likedRecipeController.getLikedRecipes(profilId);
                    Fridge2ForkApp.root.setCenter(new LikedRecipesView(likedRecipes,userController));
                }
                else if (destination.equals("Purchases")){
                    Fridge2ForkApp.root.setCenter(new PantryView(new PantryController(), userController));
                }
                else if (destination.equals("Profile")){
                    Fridge2ForkApp.root.setCenter(new ProfileView(userController, userController.getCurrentUser()));
                }
                else {
                    HomeView homeView = new HomeView(userController);
                    homeView.setupLoggedInState(isUser);
                    Fridge2ForkApp.root.setCenter(homeView);
                }
            }
        });

        backBtn.setOnAction(e -> {
            Fridge2ForkApp.root.setCenter(new HomeView(userController));
        });

        this.getChildren().addAll(title, username, password, errorLabel, loginBtn, backBtn);

        javafx.application.Platform.runLater(() -> this.requestFocus());
    }
}
