package main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    public static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        scene = new Scene(FXMLLoader.load(App.class.getResource("view.fxml")), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}