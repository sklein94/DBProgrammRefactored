package ui;

import db.Datenbank;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class UserInterface extends Application implements EventHandler<TableColumn.CellEditEvent<UserInterface.Mitarbeiter, String>> {
    private TableView<Mitarbeiter> table = new TableView<>();
    private ObservableList<Mitarbeiter> tableData;
    private Scene mainScene;

    private Stage primary;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("View");
        primaryStage.setWidth(750);
        primaryStage.setHeight(700);
        primary = primaryStage;

        mainScene = loadMitarbeiterScene();


        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private Scene loadScene(String sqlQuery) {
        tableData = null;
        Scene scene = new Scene(new Group());
        Datenbank db = new Datenbank();
        String query = sqlQuery;
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

        //Filterfelder

        //Filter Vorname
        final Label idLabel = new Label("Filter nach ID");
        TextField filterFieldID = new TextField();
        addListener(filterFieldID, "id");

        //Filter Vorname
        final Label vornameLabel = new Label("Filter nach Vorname");
        TextField filterFieldVorname = new TextField();
        addListener(filterFieldVorname, "vorname");

        //Filter Name
        final Label nameLabel = new Label("Filter nach Name");
        TextField filterFieldName = new TextField();
        addListener(filterFieldName, "name");

        //Filter Vorname
        final Label gehaltLabel = new Label("Filter nach Gehalt");
        TextField filterFieldGehalt = new TextField();
        addListener(filterFieldGehalt, "gehalt");

        //Filter Vorname
        final Label abteilungLabel = new Label("Filter nach Abteilung");
        TextField filterFieldAbteilung = new TextField();
        addListener(filterFieldAbteilung, "abteilung");

        //Filter Vorname
        final Label standortLabel = new Label("Filter nach Standort");
        TextField filterFieldStandort = new TextField();
        addListener(filterFieldStandort, "standort");

        //Filter Vorname
        final Label landLabel = new Label("Filter nach Land");
        TextField filterFieldLand = new TextField();
        addListener(filterFieldLand, "land");

        final VBox filterFields = new VBox();
        filterFields.setMaxWidth(200);
        filterFields.getChildren().addAll(idLabel, filterFieldID,
                vornameLabel, filterFieldVorname,
                nameLabel, filterFieldName,
                gehaltLabel, filterFieldGehalt,
                abteilungLabel, filterFieldAbteilung,
                standortLabel, filterFieldStandort,
                landLabel, filterFieldLand);

        //Tabelle
        final Label label = new Label("Mitarbeiteruebersicht");
        label.setFont(new Font("Arial", 20));

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



        table.setMaxHeight(300);
        table.setItems(tableData);
        table.setEditable(true);
        table.getColumns().addAll(columnID, columnVorname, columnName, columnGehalt, columnAbteilung, columnStandort, columnLand);


        //MainBox
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(filterFields, label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        return scene;
    }

    private void addListener(TextField textField, String field) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<Mitarbeiter> filteredList = FXCollections.observableArrayList();
                for (Mitarbeiter m : tableData) {
                    String checkString = "";
                    switch (field.toLowerCase()) {
                        case "vorname":
                            checkString = m.getVorname();
                            break;
                        case "name":
                            checkString = m.getName();
                            break;
                        case "standort":
                            checkString = m.getStandort();
                            break;
                        case "abteilung":
                            checkString = m.getAbteilung();
                            break;
                        case "land":
                            checkString = m.getLand();
                            break;
                        case "id":
                            checkString = m.getID();
                            break;
                        case "gehalt":
                            checkString = m.getID();
                            break;
                    }
                    if (checkString.toLowerCase().contains(newValue.toLowerCase())) {
                        filteredList.add(m);
                    }
                }
                table.setItems(filteredList);
            }
            else {
                table.setItems(tableData);
            }
            table.refresh();
        });
    }


    private Scene loadMitarbeiterScene() {
        return loadScene("SELECT * FROM Mitarbeiteruebersicht");
    }

    @Override
    public void handle(TableColumn.CellEditEvent<Mitarbeiter, String> t) {
        Mitarbeiter m = t.getTableView().getItems().get(t.getTablePosition().getRow());
        String newVal = t.getNewValue();
        int column = t.getTablePosition().getColumn();
        switch (column) {
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
        table.refresh();
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
                db.integerQuery("UPDATE Mitarbeiter SET Vorname='" + vorname + "' WHERE ID=" + this.getID());
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
                db.integerQuery("UPDATE Mitarbeiter SET Name='" + name + "' WHERE ID=" + this.getID());
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
                db.integerQuery(sql);
                this.abteilung.set(abteilung);
                String sqlx = "SELECT Standort, Land FROM Mitarbeiteruebersicht WHERE ID=" + this.getID();
                String[][] arr = db.asArray(sqlx);
                this.setStandort(arr[1][0]);
                this.setLand(arr[1][1]);
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
                this.land.set(land);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
