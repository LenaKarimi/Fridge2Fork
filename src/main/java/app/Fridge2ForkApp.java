package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.SideBarView;
import view.HomeView;

public class Fridge2ForkApp extends Application {

    //Static så hittar SideBarView den
    public static BorderPane root;

    @Override
    public void start(Stage stage){
        //1. Här skapar vi huvudramen för programmet
        root = new BorderPane();

        //Sidebaren med färger
        root.setLeft(new SideBarView());

        //Välkomstsidan i mitten
        root.setCenter(new HomeView());

        //2.Innehållet och lägg i ramen, storlek 800x600
        Scene scene = new Scene(root, 800, 600);

        //3. Titel till fönstret högst upp
        stage.setTitle("Fridge2Fork");

        //4. Koppla ihop fönstret (stage) med dess innehåll
        stage.setScene(scene);

        //5. Gör det synligt på skärmen
        stage.show();
    }

    public static void main (String[]args){
        launch(args);
    }
}
