package View;

import App.Fridge2ForkApp;
import Controller.UserController;
import DTO.ProfileDTO;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

/**
 * View for viewing and editing the users profile.
 * Allows updating username, email, full name and password.
 * @author Maya, Racil and Lena
 */
public class ProfileView extends VBox {
    private UserController userController;

    /**
     * Constructs the ProfilView and builds the UI.
     * @param userController provides the currently logged in userand handles profile updates.
     * @param profileDTO the current users profile data to prefill the fields
     */
    public ProfileView(UserController userController, ProfileDTO profileDTO){
        this.userController = userController;
        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("MY PROFILE");
        Label statusLabel = new Label();
        statusLabel.setWrapText(true);

        String defaultProfileImageUrl = "https://static.vecteezy.com/system/resources/previews/037/336/395/non_2x/user-profile-flat-illustration-avatar-person-icon-gender-neutral-silhouette-profile-picture-free-vector.jpg";
        Image profileImage = new Image(defaultProfileImageUrl, true);
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(120);
        profileImageView.setFitHeight(120);
        profileImageView.setPreserveRatio(true);

        Circle clip = new Circle(60, 60, 60);
        profileImageView.setClip(clip);

        StackPane profilePicture = new StackPane(profileImageView);
        profilePicture.setPrefSize(120, 120);

        Label userNameLabel = new Label("Username");
        TextField userName = new TextField();
        userName.setMaxWidth(200);

        Label emailLabel = new Label("Email");
        TextField email = new TextField();
        email.setMaxWidth(200);

        PasswordField password = new PasswordField();
        password.setPromptText("New Password (leave empty to keep current)");
        password.setMaxWidth(200);

        Label nameLabel = new Label("Full Name");
        TextField name = new TextField();
        name.setMaxWidth(200);

        if (profileDTO != null) {
            userName.setText(profileDTO.getUsername());
            email.setText(profileDTO.getEmail());
            name.setText(profileDTO.getName());
        }
        Button saveButton = new Button("Save Changes");
        saveButton.setPrefWidth(220);
        saveButton.setStyle("-fx-background-color: tan; -fx-text-fill: white;");
        saveButton.setCursor(Cursor.HAND);

        Button backButton = new Button("Back to Home");
        backButton.setPrefWidth(220);

        Button logoutButton = new Button("Log out");
        logoutButton.setPrefWidth(220);
        logoutButton.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-border-color: red;" +
                " -fx-border-radius: 5;");
        logoutButton.setCursor(Cursor.HAND);

        Label errorLable = new Label();
        errorLable.setStyle("-fx-text-fill: red;");

        saveButton.setOnAction(event -> {
            if (profileDTO != null) {
                String newPassword = password.getText().isEmpty()
                        ? profileDTO.getPassword()
                        : password.getText();

                String error = userController.updateProfile(
                        profileDTO.getId(),
                        userName.getText(),
                        newPassword,
                        name.getText(),
                        email.getText()
                );

                if (error != null) {
                    errorLable.setText(error);
                    errorLable.setStyle("-fx-text-fill: red;");
                } else {
                    errorLable.setText("");
                    statusLabel.setTextFill(Color.GREEN);
                    statusLabel.setText("Profile updated! Redirecting...");
                    System.out.println("Profile updated successfully");

                    PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                    pause.setOnFinished(e -> {
                        profileDTO.setName(name.getText());
                        profileDTO.setUsername(userName.getText());
                        profileDTO.setEmail(email.getText());

                        HomeView hv = new HomeView(userController);
                        hv.setupLoggedInState(profileDTO);
                        Fridge2ForkApp.root.setCenter(hv);
                    });
                    pause.play();
                }

            }
        });

        backButton.setOnAction(e -> {
            HomeView hv = new HomeView(userController);
            if (profileDTO != null){
                hv.setupLoggedInState(profileDTO);
                Fridge2ForkApp.root.setCenter(hv);
            }
        });

        logoutButton.setOnAction(e -> {
            userController.logout();
            Fridge2ForkApp.root.setLeft(new SideBarView(userController));
            Fridge2ForkApp.root.setCenter(new HomeView(userController));
        });

        VBox contentContainer = new VBox(12);
        contentContainer.setAlignment(Pos.TOP_CENTER);
        contentContainer.setPadding(new Insets(30));
        contentContainer.setStyle("-fx-background-color: white;");

        contentContainer.getChildren().addAll(title, statusLabel, profilePicture,
                new Label("Username"),
                userName,
                new Label("Email"),
                email,
                new Label("Full Name"),
                name,
                new Label("Change Password"),
                password,
                errorLable,
                new Separator()
        );
        HBox buttons = new HBox(12);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(saveButton, backButton);
        contentContainer.getChildren().add(buttons);

        if (userController != null && userController.getCurrentUser() != null) {
            contentContainer.getChildren().add(logoutButton);
        }
        ScrollPane scrollPane = new ScrollPane(contentContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        this.getChildren().clear();
        this.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }
}
