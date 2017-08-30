package ui;

import db.Datenbank;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class UserInterface extends Application implements EventHandler<TableColumn.CellEditEvent<UserInterface.Mitarbeiter, String>>{
    private ObservableList<Mitarbeiter> tableData;

    private Stage primary;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("View");
        primaryStage.setWidth(750);
        primaryStage.setHeight(500);
        primary = primaryStage;

        primaryStage.setScene(loadMitarbeiterScene());


        primaryStage.show();
    }


    private Scene loadMitarbeiterScene() {
        TableView<Mitarbeiter> table  = new TableView<>();
        tableData = null;
        Scene scene = new Scene(new Group());
        Datenbank db = new Datenbank();
        String query = "SELECT * FROM Mitarbeiteruebersicht";
        String[][] Mitarbeiterdaten;
        try {
            Mitarbeiterdaten = db.asArray(query);
            for (String[] temp : Mitarbeiterdaten) {
                if (tableData == null) {
                    tableData = FXCollections.observableArrayList();
                }
                else tableData.add(new Mitarbeiter(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6]));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        final Label label = new Label("Mitarbeiteruebersicht");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn columnID = new TableColumn("ID");
        columnID.setMinWidth(100);
        columnID.setCellValueFactory(
                new PropertyValueFactory<Mitarbeiter, String>("ID"));


        TableColumn columnVorname = new TableColumn("Vorname");
        columnVorname.setMinWidth(100);
        columnVorname.setCellFactory(TextFieldTableCell.forTableColumn());
        columnVorname.setCellValueFactory(
                new PropertyValueFactory<Mitarbeiter, String>("Vorname"));
        columnVorname.setOnEditCommit(this);


        TableColumn columnName = new TableColumn("Name");
        columnName.setMinWidth(100);
        columnName.setCellFactory(TextFieldTableCell.forTableColumn());
        columnName.setCellValueFactory(
                new PropertyValueFactory<Mitarbeiter, String>("Name"));
        columnName.setOnEditCommit(this);


        TableColumn columnGehalt = new TableColumn("Gehalt");
        columnGehalt.setMinWidth(100);
        columnGehalt.setCellFactory(TextFieldTableCell.forTableColumn());
        columnGehalt.setCellValueFactory(
                new PropertyValueFactory<Mitarbeiter, String>("Gehalt"));
        columnGehalt.setOnEditCommit(this);


        TableColumn columnAbteilung = new TableColumn("Abteilung");
        columnAbteilung.setMinWidth(100);
        columnAbteilung.setCellFactory(TextFieldTableCell.forTableColumn());
        columnAbteilung.setCellValueFactory(
                new PropertyValueFactory<Mitarbeiter, String>("Abteilung"));
        columnAbteilung.setOnEditCommit(this);


        TableColumn columnStandort = new TableColumn("Standort");
        columnStandort.setMinWidth(100);
        columnStandort.setCellValueFactory(
                new PropertyValueFactory<Mitarbeiter, String>("Standort"));


        TableColumn columnLand = new TableColumn("Land");
        columnLand.setMinWidth(100);
        columnLand.setCellValueFactory(
                new PropertyValueFactory<Mitarbeiter, String>("Land"));


        table.setItems(tableData);
        table.getColumns().addAll(columnID, columnVorname, columnName, columnGehalt, columnAbteilung, columnStandort, columnLand);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        return scene;
    }

    @Override
    public void handle(TableColumn.CellEditEvent<Mitarbeiter, String> t) {
        Mitarbeiter m = t.getTableView().getItems().get(t.getTablePosition().getRow());
        String newVal = t.getNewValue();
        int column = t.getTablePosition().getColumn();
        switch (column){
            case 1:
                m.setVorname(newVal);
                break;
            case 2:
                m.setName(newVal);
                break;
            case 3:
                m.setGehalt(newVal);
                break;
            case 4:
                m.setAbteilung(newVal);
                break;
        }
        primary.setScene(loadMitarbeiterScene());
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
            this.gehalt = new SimpleStringProperty(gehalt);
            this.abteilung = new SimpleStringProperty(abteilung);
            this.standort = new SimpleStringProperty(standort);
            this.land = new SimpleStringProperty(land);
        }

        public String getID() {
            return this.id.get();
        }

        public void setID(String id) {
            this.id.set(id);
        }

        public String getVorname() {
            return this.vorname.get();
        }

        public void setVorname(String vorname) {
            try {
                Datenbank db = new Datenbank();
                int a = db.integerQuery("UPDATE Mitarbeiter SET Vorname='" + vorname + "' WHERE ID=" + this.getID());
                this.vorname.set(vorname);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getName() {
            return this.name.get();
        }

        public void setName(String name) {
            try {
                Datenbank db = new Datenbank();
                db.integerQuery("UPDATE Mitarbeiter SET Name=" + name + " WHERE ID=" + this.getID());
                this.name.set(name);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getGehalt() {
            return this.gehalt.get();
        }

        public void setGehalt(String gehalt) {
            try {
                BigDecimal bd = new BigDecimal(gehalt);
                if (bd.compareTo(new BigDecimal("100000")) == -1) {
                    Datenbank db = new Datenbank();
                    db.integerQuery("UPDATE Mitarbeiter SET Gehalt=" + gehalt + " WHERE ID=" + this.getID());
                    this.gehalt.set(gehalt);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getAbteilung() {
            return this.abteilung.get();
        }

        public void setAbteilung(String abteilung) {

            try {
                Datenbank db = new Datenbank();
                String sql = "UPDATE Mitarbeiter SET Abteilung_ID=(select ID from Abteilungsuebersicht WHERE Name='" + abteilung + "') WHERE ID=" + this.getID();
                System.out.println(sql);
                db.integerQuery(sql);
                this.abteilung.set(abteilung);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getStandort() {
            return this.standort.get();
        }

        public void setStandort(String standort) {
            try {
                Datenbank db = new Datenbank();
                db.integerQuery("UPDATE Mitarbeiter SET Standort=" + standort + " WHERE ID=" + this.getID());
                this.standort.set(standort);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String getLand() {
            return this.land.get();
        }

        public void setLand(String land) {
            try {
                Datenbank db = new Datenbank();
                db.integerQuery("UPDATE Mitarbeiter SET Land=" + land + " WHERE ID=" + this.getID());
                this.land.set(land);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
