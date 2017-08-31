package ui.neu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Test FXML");

        try {
            //Lade FXML
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Tabelle.fxml"));
            AnchorPane page = loader.load();
            Scene scene = new Scene(page);

            //Setze Scene
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        launch(args);
    }
}
