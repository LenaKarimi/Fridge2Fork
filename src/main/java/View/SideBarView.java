package View;

import Controller.LikedRecipeController;
import Controller.UserController;
import java.util.List;

import Model.Recipe;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
        this.setSpacing(12); //Mellanrum mellan raderna
        this.setPadding(new Insets(10)); //Marginal från kanten
        this.setStyle("-fx-background-color: darkseagreen;");

        //2. Översta raden med två kvadrater (HBox)
        HBox topRow = new HBox(10);

        //left square finns alltid, lägg till profile som standard
        leftSquare = createBox("#f2ede4", 100, 70, "Profile");
        topRow.getChildren().addAll(leftSquare);

        //om användaren är inloggad, byt ut leftsquare innehåll till profilbild
        if (userController != null && userController.getCurrentUser() != null) {
            updateProfilePicture();
        } else {
            resetProfilePicture();
        }

        //Visa användarnamn + logout-knapp om inloggad
        if (userController != null && userController.getCurrentUser() != null){
            VBox userInfo = new VBox(5);
            userInfo.setAlignment(Pos.CENTER_LEFT);

            Label userLabel = new Label(userController.getCurrentUser().getUsername());
            userLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");

            //log out knapp
            Button logoutBtn = new Button("Log out");
            logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;" +
                    "-fx-font-size: 10px; -fx-background-radius: 5;");
            logoutBtn.setCursor(Cursor.HAND);

            //dialog innan utlogg
            logoutBtn.setOnAction(e -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm logout");
                confirm.setHeaderText(null);
                confirm.setContentText("Are you sure you want to log out?");

                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No", ButtonType.NO.getButtonData());
                confirm.getButtonTypes().setAll(yes, no);

                confirm.showAndWait().ifPresent(choice -> {
                    if (choice == yes) {
                        userController.logout();

                        //återskapa sidebar + gå till home, leftsquare ska bli profile igen
                        Fridge2ForkApp.root.setLeft(new SideBarView(userController));
                        Fridge2ForkApp.root.setCenter(new HomeView(userController));
                    }
                });
            });

            userInfo.getChildren().addAll(userLabel, logoutBtn);
            topRow.getChildren().add(userInfo);
            HBox.setHgrow(userInfo, Priority.ALWAYS);
        }


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

    private StackPane createBox(String color, double width, double height, String text) {
        StackPane box = new StackPane();
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5;");

        //sätt endast pref size om bredden är större än 0, annars maxWidth
        box.setPrefSize(width, height);

        //Knapptrycken för vänsterpanelen
        box.setCursor(javafx.scene.Cursor.HAND);

        //Det som händer vid klick
        box.setOnMouseClicked(e -> {
            if (Fridge2ForkApp.root.getCenter() instanceof FridgeView) { //kollar om användaren är i receptgenereringen
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Lämna sidan?");
                alert.setHeaderText(null);
                alert.setContentText("Om du byter panel sparas ej dina val.");

                ButtonType stannaKvar = new ButtonType("Stanna kvar");
                ButtonType byt = new ButtonType("Byt panel");
                alert.getButtonTypes().setAll(stannaKvar, byt);

                alert.showAndWait().ifPresent(svar -> {
                    if (svar == byt) {
                        navigera(text); // byt panel
                    }
                    // annars händer ingenting
                });
            } else {
                navigera(text); //byt direkt om man inte är i fridgeView
            }
            System.out.println("Du klickade på: " + text);
        });
        Label lable = new Label(text);
        box.getChildren().addAll(lable);
        return box;

    }

    private void navigera(String text) {
         //Mayas originalmetod
         if(text.equals("Home")) {
             Fridge2ForkApp.root.setCenter(new HomeView(userController));
         } else if (text.equals("Profile")) {
             if (userController.getCurrentUser() != null) {
                 Fridge2ForkApp.root.setCenter(new ProfileView(userController, userController.getCurrentUser()));
             } else {
                 Fridge2ForkApp.root.setCenter(new LoginView(userController));
             }
         } else if (text.equals("Liked recipes")) {
             System.out.println("getCurrentUser: " + userController.getCurrentUser());
             if (userController.getCurrentUser() == null) { // om man ej är inloggad
                 Fridge2ForkApp.root.setCenter(new LoginView(userController));
             } else {
                 LikedRecipeController likedRecipeController = new LikedRecipeController(); // annars hämta recept från db via controller
                 int profileId = userController.getCurrentUser().getId();
                 List<Recipe> likedRecipes = likedRecipeController.getLikedRecipes(profileId);
                 Fridge2ForkApp.root.setCenter(new LikedRecipesView(likedRecipes, userController));
             }
         } else if (text.equals("Purchases")) { // bara en tom vy visas då detta ej är klar
             Fridge2ForkApp.root.setCenter(new VBox());
         }
    }

    //anropas efter inloggning för att byta ut till bild
    public void updateProfilePicture() {
        String defaultProfileImageUrl = "https://static.vecteezy.com/system/resources/previews/037/336/395/non_2x/user-profile-flat-illustration-avatar-person-icon-gender-neutral-silhouette-profile-picture-free-vector.jpg";
        Image profileImage = new Image(defaultProfileImageUrl, false);

        //felkontroll
        if (profileImage.isError()) {
            System.out.println("SideBarView: IMAGE LOAD ERROR -> " + profileImage.getException());
            //om fel, återställ till text
            resetProfilePicture();
            return;
        } else {
            System.out.println("SideBarView: IMAGE LOADED, SIZE = " + profileImage.getWidth() + "x" +
                    profileImage.getHeight());
        }

        ImageView profileImageView = new ImageView(profileImage);
        profileImageView.setFitWidth(60);
        profileImageView.setFitHeight(60);
        profileImageView.setPreserveRatio(true);
        profileImageView.setSmooth(true);

        Circle clip = new Circle(30, 30, 30);
        profileImageView.setClip(clip);

        leftSquare.setMinWidth(80);
        leftSquare.setMinHeight(70);
        leftSquare.setPrefWidth(100);

        //rensar och lägger till bild
        leftSquare.getChildren().clear();
        leftSquare.getChildren().add(profileImageView);
    }

    //Anropas vid utloggning för att återställa profile texten från bilden
    public void resetProfilePicture() {
        leftSquare.getChildren().clear();
        Label profileLabel = new Label("Profile");
        leftSquare.getChildren().add(profileLabel);
    }
}
