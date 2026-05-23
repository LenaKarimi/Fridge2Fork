package App;

import Controller.UserController;
import javafx.application.Application;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import View.SideBarView;
import View.HomeView;

/**
 * Main JavaFX applixation class for Fridge2Fork.
 * It creates main UI layout, initializing the controller and setting up the primary scene and stage
 * @author Maya
 * @author Lena
 */
public class Fridge2ForkApp extends Application {
    public static BorderPane root;
    public static SideBarView sideBar;

    /**
     * Entery point for the JavaFX applixation.
     * Initilizes the UI and sets up the main window.
     * @param stage the primary stage provided by JavaFX
     */
    @Override
    public void start(Stage stage){
        root = new BorderPane(); //1.Här skapar vi huvudramen för programmet

        UserController userController = new UserController(); //skapat controllern en gån här

        sideBar = (new SideBarView(userController)); //Sidebaren med färger
        root.setLeft(sideBar); //Välkomstsidan i mitten
        root.setCenter(new HomeView(userController)); // skickar in controllern till homeview så resterande vyer kan jobba med den

        Scene scene = new Scene(root, 800, 600); //2.Innehållet och lägg i ramen, storlek 800x600

        stage.setTitle("Fridge2Fork"); //3. Titel till fönstret högst upp

        stage.setScene(scene); //4. Koppla ihop fönstret (stage) med dess innehåll

        stage.show(); //5. Gör det synligt på skärmen
    }

    /**
     * Launches the JavaFX application.
     * @param args command-line arguments
     */
    public static void main (String[]args){
        launch(args);
    }
}
