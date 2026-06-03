package App;

import Controller.UserController;
import javafx.application.Application;
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
        root = new BorderPane();

        UserController userController = new UserController();

        sideBar = (new SideBarView(userController));
        root.setLeft(sideBar); //Välkomstsidan i mitten
        root.setCenter(new HomeView(userController));

        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("Fridge2Fork");

        stage.setScene(scene);

        stage.show();
    }

    /**
     * Launches the JavaFX application.
     * @param args command-line arguments
     */
    public static void main (String[]args){
        launch(args);
    }
}
