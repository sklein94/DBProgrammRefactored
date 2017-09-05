package refactored;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Hauptklasse zum Starten der Applikation.
 *
 * @author sklein
 * @version 1.0
 */
public final class Main extends Application {
    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Mitarbeitertabelle");

        try {
            //Lade FXML
            FXMLLoader loader = new FXMLLoader(refactored.Main.class.getResource("ui/Tabelle.fxml"));
            AnchorPane page = loader.load();
            Scene scene = new Scene(page);

            //Setze Scene
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (IllegalStateException e) {
            System.out.println("Fehler beim Laden der FXML-Datei: " + e.getMessage());
        }

    }
}
