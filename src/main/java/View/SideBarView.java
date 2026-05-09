package View;

import Controller.UserController;
import java.util.List;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import App.Fridge2ForkApp;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;


public class SideBarView extends VBox {
    private UserController userController;
    private StackPane leftSquare;

    public SideBarView(UserController userController){
        this.userController = userController;
        //1. Inställningar för själva sidebaren (VBox)
        this.setSpacing(10); //Mellanrum mellan raderna
        this.setPadding(new Insets(10)); //Marginal från kanten
        this.setStyle("-fx-background-color: darkseagreen;");

        //2. Översta raden med två kvadrater (HBox)
        HBox topRow = new HBox(10);
        leftSquare = createBox("#f2ede4", 100, 70, "Profile");
        //StackPane rightSquare = createBox("lightpink", 70, 70, "Likes");
        topRow.getChildren().addAll(leftSquare);

        //Fyra rektanglar som fyller ut bredden automatiskt
        StackPane rect1 = createBox("white", 0, 80, "Home");
        StackPane rect2 = createBox("white", 0, 80, "Liked recipes");
        StackPane rect3 = createBox("white", 0, 80, "Purchases");
        //StackPane rect4 = createBox("white", 0, 80, "Inställningar");

        //Gör rektanglarna lika breda som sidebaren
        rect1.setMaxWidth(Double.MAX_VALUE);
        rect2.setMaxWidth(Double.MAX_VALUE);
        rect3.setMaxWidth(Double.MAX_VALUE);
        //rect4.setMaxWidth(Double.MAX_VALUE);

        this.getChildren().addAll(topRow, rect1, rect2, rect3);
    }

    private StackPane createBox(String color, double width, double height, String text){
        StackPane box = new StackPane();
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5;");
        box.setPrefSize(width, height);

        //Knapptrycken för vänsterpanelen
        box.setCursor(javafx.scene.Cursor.HAND);

        //Det som händer vid klick
        box.setOnMouseClicked(e -> {
            if(text.equals("Home")){
                Fridge2ForkApp.root.setCenter(new HomeView(userController));
            } else if (text.equals("Profile")) {
                if (userController.getCurrentUser() != null) {
                    Fridge2ForkApp.root.setCenter(new ProfileView(userController, userController.getCurrentUser()));
                } else {
                    Fridge2ForkApp.root.setCenter(new LoginView(userController));
                }
            } else if (text.equals("Liked recipes")) {
                //hämta listan från databasen sen (Racils DAO)
                //skickar tom lista tills databasen är klar
                Fridge2ForkApp.root.setCenter(new LikedRecipesView(new java.util.ArrayList<>(), userController));
            }
            System.out.println("Du klickade på: " + text);
        });

        Label label = new Label(text);
        box.getChildren().add(label);

        return box;
    }

    //Anropas efter inloggning för att byta ut Profile texten till bilden
    public void updateProfilePicture() {
        String defaultProfileImageUrl = "https://static.vecteezy.com/system/resources/previews/037/336/395/non_2x/user-profile-flat-illustration-avatar-person-icon-gender-neutral-silhouette-profile-picture-free-vector.jpg";
        Image profileImage = new Image(defaultProfileImageUrl, true);
        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(60);
        profileImageView.setFitHeight(60);
        profileImageView.setPreserveRatio(true);

        Circle clip = new Circle(30, 30, 30);
        profileImageView.setClip(clip);

        leftSquare.getChildren().clear();
        leftSquare.getChildren().add(profileImageView);
    }

    //Anropas vid utloggning för att återställa Profile texten från bilden
    public void resetProfilePicture() {
        leftSquare.getChildren().clear();
        Label profileLabel = new Label("Profile");
        leftSquare.getChildren().add(profileLabel);
    }
}
