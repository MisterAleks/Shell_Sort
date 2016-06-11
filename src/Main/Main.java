package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


// For work with JavaFX we need to extend a class Application
public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {

        //Here is loaded Main.fxml - file of marking which we edit using framework
        //JavaFX SceneBuilder 2.0
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));

        //Passed in parameter object primaryStage is our main window
        primaryStage.setTitle("Shell sort");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {

        //Our launch point. This method raises the FX stack and download our application in it
        launch(args);

    }
}
