package View;

import Controller.LikedRecipeController;
import Controller.PantryController;
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

/**
 * Sidebar navigation view displayed on the left side of the application.
 * Contains navigation buttons for all main views and shows user info whrn logged in.
 * @author Maya and Racil
 */
public class SideBarView extends VBox {
    private UserController userController;
    private StackPane leftSquare;

    /**
     * Constructs the SidebarView and builds the UI.
     * @param userController provides the current logged in user and handles logout
     */
    public SideBarView(UserController userController){
        this.userController = userController;

        this.setSpacing(12);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-background-color: darkseagreen;");

        HBox topRow = new HBox(10);

        leftSquare = createBox("#f2ede4", 100, 70, "Profile");
        topRow.getChildren().addAll(leftSquare);

        if (userController != null && userController.getCurrentUser() != null) {
            updateProfilePicture();
        } else {
            resetProfilePicture();
        }

        if (userController != null && userController.getCurrentUser() != null){
            VBox userInfo = new VBox(5);
            userInfo.setAlignment(Pos.CENTER_LEFT);

            Label userLabel = new Label(userController.getCurrentUser().getUsername());
            userLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");

            Button logoutBtn = new Button("Log out");
            logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;" +
                    "-fx-font-size: 10px; -fx-background-radius: 5;");
            javafx.scene.effect.DropShadow logoutShadow = new javafx.scene.effect.DropShadow();
            logoutShadow.setColor(javafx.scene.paint.Color.rgb(231, 76, 60, 0.4));
            logoutShadow.setRadius(10);

            logoutBtn.setOnMouseEntered(e -> {
                logoutBtn.setEffect(logoutShadow);
                logoutBtn.setTranslateY(-2); // Ett litet lyft
            });

            logoutBtn.setOnMouseExited(e -> {
                logoutBtn.setEffect(null);
                logoutBtn.setTranslateY(0);
            });

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
                        Fridge2ForkApp.root.setLeft(new SideBarView(userController));
                        Fridge2ForkApp.root.setCenter(new HomeView(userController));
                    }
                });
            });
            userInfo.getChildren().addAll(userLabel, logoutBtn);
            topRow.getChildren().add(userInfo);
            HBox.setHgrow(userInfo, Priority.ALWAYS);
        }

        StackPane rect1 = createBox("white", 0, 80, "Home");
        StackPane rect2 = createBox("white", 0, 80, "Liked recipes");
        StackPane rect3 = createBox("white", 0, 80, "Purchases");

        rect1.setMaxWidth(Double.MAX_VALUE);
        rect2.setMaxWidth(Double.MAX_VALUE);
        rect3.setMaxWidth(Double.MAX_VALUE);

        this.getChildren().addAll(topRow, rect1, rect2, rect3);
    }

    /**
     * Creates a styled clickable box used as a navigation button in the sidebar.
     * @param color background color of the box
     * @param width preferred width (0 means full width)
     * @param height preferred height
     * @param text label text and navigation key
     * @return a configured StackPane acting as a navigation button
     */
    private StackPane createBox(String color, double width, double height, String text) {
        StackPane box = new StackPane();
        box.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 5;");

        if (width > 0) box.setPrefWidth(width);
        box.setPrefSize(width, height);

        box.setCursor(javafx.scene.Cursor.HAND);

        javafx.scene.effect.DropShadow shadow = new javafx.scene.effect.DropShadow();
        shadow.setColor(javafx.scene.paint.Color.rgb(0, 0, 0, 0.3));
        shadow.setRadius(10);
        shadow.setOffsetY(3);

        box.setOnMouseEntered(e -> {
            box.setEffect(shadow);
            box.setTranslateY(-3);
        });

        box.setOnMouseExited(e -> {
            box.setEffect(null);
            box.setTranslateY(0);
        });

        box.setOnMouseClicked(e -> {
            if (Fridge2ForkApp.root.getCenter() instanceof FridgeView) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Leave this page?");
                alert.setHeaderText(null);
                alert.setContentText("If you exit this page, your choices won't be saved.");

                ButtonType stannaKvar = new ButtonType("Stay");
                ButtonType byt = new ButtonType("Switch panel");
                alert.getButtonTypes().setAll(stannaKvar, byt);

                alert.showAndWait().ifPresent(svar -> {
                    if (svar == byt) {
                        navigera(text);
                    }
                });
            }
            else {
                navigera(text);
            }
            System.out.println("Du klickade på: " + text);
        });

        Label lable = new Label(text);
        box.getChildren().addAll(lable);
        return box;
    }

    /**
     * Navigates to the appropriate view based on the clicked button label.
     * @param text the label of the clicked navigation button
     */
    private void navigera(String text) {
         if(text.equals("Home")) {
             Fridge2ForkApp.root.setCenter(new HomeView(userController));
         }
         else if (text.equals("Profile")) {
             if (userController.getCurrentUser() != null) {
                 Fridge2ForkApp.root.setCenter(new ProfileView(userController, userController.getCurrentUser()));
             } else {
                 Fridge2ForkApp.root.setCenter(new LoginView(userController));
             }
         }
         else if (text.equals("Liked recipes")) {
             System.out.println("getCurrentUser: " + userController.getCurrentUser());
             if (userController.getCurrentUser() == null) {
                 Fridge2ForkApp.root.setCenter(new LoginView(userController));
             }
             else {
                 LikedRecipeController likedRecipeController = new LikedRecipeController();
                 int profileId = userController.getCurrentUser().getId();
                 List<Recipe> likedRecipes = likedRecipeController.getLikedRecipes(profileId);
                 Fridge2ForkApp.root.setCenter(new LikedRecipesView(likedRecipes, userController));
             }
         }
         else if (text.equals("Purchases")) {
             if (userController.getCurrentUser() != null) {
                 Fridge2ForkApp.root.setCenter(new PantryView(new PantryController(), userController));
             }
             else {
                 Fridge2ForkApp.root.setCenter(new LoginView(userController));
             }
         }
    }

    /**
     * Updates the profil picture in the top left box by loading an image from URL.
     * Falls back to text if the image fails to load.
     */
    public void updateProfilePicture() {
        String defaultProfileImageUrl = "https://static.vecteezy.com/system/resources/previews/037/336/395/non_2x/user-profile-flat-illustration-avatar-person-icon-gender-neutral-silhouette-profile-picture-free-vector.jpg";
        Image profileImage = new Image(defaultProfileImageUrl, false);

        if (profileImage.isError()) {
            System.out.println("SideBarView: IMAGE LOAD ERROR -> " + profileImage.getException());
            resetProfilePicture();
            return;
        }
        else {
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
        leftSquare.getChildren().clear();
        leftSquare.getChildren().add(profileImageView);
    }

    /**
     * Resets the top left show "Profile" text instead of a profile picture.
     * Called on logout or when no user is logged in.
     */
    public void resetProfilePicture() {
        leftSquare.getChildren().clear();
        Label profileLabel = new Label("Profile");
        leftSquare.getChildren().add(profileLabel);
    }
}
