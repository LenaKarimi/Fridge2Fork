package View;

import Controller.UserController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.*;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;


public class ProfilView extends VBox {
    private UserController userController;

    public ProfilView (UserController userController){

        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("MY PROFILE");

        StackPane profilePicture = new StackPane();
        profilePicture.setPrefSize(120,120);
        profilePicture.setStyle("-fx-background-color: lightgray;");
        Label pictureLabel = new Label("Photo");
        profilePicture.getChildren().add(pictureLabel);

        Label userNameLabel = new Label("Username");
        TextField userName = new TextField();
        userName.setMaxWidth(200);

        Label emailLabel = new Label("Email");
        TextField email = new TextField();
        email.setMaxWidth(200);

        Label passwordLabel = new Label("Password");
        PasswordField password = new PasswordField();
        password.setMaxWidth(200);

        Label nameLabel = new Label("Name");
        TextField name = new TextField();
        name.setMaxWidth(200);

        Button saveButton = new Button("Save");
        saveButton.setPrefWidth(120);

        saveButton.setOnAction(e -> {
            System.out.println("Profile saved");
        });

        this.getChildren().addAll(
                profilePicture,
                userNameLabel, userName,
                emailLabel, email,
                passwordLabel, password,
                nameLabel, name,
                saveButton
        );
    }
}
