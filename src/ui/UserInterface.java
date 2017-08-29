package ui;

import db.Datenbank;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class UserInterface extends Application {
    private TableView table = new TableView();
    private ObservableList<Mitarbeiter> tableData;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Group());
        primaryStage.setTitle("View");
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);

        loadMitarbeiter();

        final Label label = new Label("Mitarbeiteruebersicht");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("ID");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<Mitarbeiter, String>("id"));



        table.setItems(tableData);
        table.getColumns().addAll(firstNameCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void loadMitarbeiter() {
        Datenbank db = new Datenbank();
        String query = "SELECT * FROM Mitarbeiteruebersicht";
        String[][] Mitarbeiterdaten;
        try {
            Mitarbeiterdaten = db.asArray(query);
            for (String[] temp : Mitarbeiterdaten) {
                tableData.add(new Mitarbeiter(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6]));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static class Mitarbeiter {
        private final StringProperty id;
        private final StringProperty vorname;
        private final StringProperty name;
        private final StringProperty gehalt;
        private final StringProperty abteilung;
        private final StringProperty standort;
        private final StringProperty land;

        public Mitarbeiter(String id, String vorname, String name, String gehalt, String abteilung, String standort, String land) {
            this.id = new SimpleStringProperty(id);
            this.vorname = new SimpleStringProperty(vorname);
            this.name = new SimpleStringProperty(name);
            this.gehalt = new SimpleStringProperty(id);
            this.abteilung = new SimpleStringProperty(abteilung);
            this.standort = new SimpleStringProperty(standort);
            this.land = new SimpleStringProperty(land);
        }

        public String getID() {
            return this.id.get();
        }

        public String getVorname() {
            return this.vorname.get();
        }

        public String getName() {
            return this.name.get();
        }

        public String getGehalt() {
            return this.gehalt.get();
        }

        public String getAbteilung() {
            return this.abteilung.get();
        }

        public String getStandort() {
            return this.standort.get();
        }

        public String getLand() {
            return this.land.get();
        }
    }
}
