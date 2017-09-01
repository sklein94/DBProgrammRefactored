package ui.neu;


import db.Datenbank;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class TabelleController implements EventHandler<TableColumn.CellEditEvent<ui.neu.Mitarbeiter, String>> {
    @FXML
    private TextField idFilter;

    @FXML
    private TextField vornameFilter;

    @FXML
    private TextField nameFilter;

    @FXML
    private TextField gehaltFilter;

    @FXML
    private TextField abteilungFilter;

    @FXML
    private TextField standortFilter;

    @FXML
    private TextField landFilter;

    @FXML
    private TableView<ui.neu.Mitarbeiter> table;

    @FXML
    private TableColumn<ui.neu.Mitarbeiter, String> id;

    @FXML
    private TableColumn<ui.neu.Mitarbeiter, String> vorname;

    @FXML
    private TableColumn<ui.neu.Mitarbeiter, String> name;

    @FXML
    private TableColumn<ui.neu.Mitarbeiter, String> gehalt;

    @FXML
    private TableColumn<ui.neu.Mitarbeiter, String> abteilung;

    @FXML
    private TableColumn<ui.neu.Mitarbeiter, String> standort;

    @FXML
    private TableColumn<ui.neu.Mitarbeiter, String> land;

    @FXML
    private Button mitarbeiterAdd;

    @FXML
    private TextField vornameAdd;

    @FXML
    private TextField nameAdd;

    @FXML
    private TextField gehaltAdd;

    @FXML
    private TextField abteilungAdd;

    @FXML
    private Label fehler;


    private ObservableList<ui.neu.Mitarbeiter> tableData;

    @FXML
    private void initialize() {
        //Lade aus der Datenbank die Werte in die Tabelle
        load();

        //Initialisiere die Tabellenspalten
        id.setCellValueFactory(cellData -> cellData.getValue().getIDProperty());

        vorname.setCellValueFactory(cellData -> cellData.getValue().getVornameProperty());
        vorname.setCellFactory(TextFieldTableCell.forTableColumn());
        vorname.setOnEditCommit(this);

        name.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(this);

        gehalt.setCellValueFactory(
                film -> {
                    String gehalt = film.getValue().getGehalt();
                    SimpleStringProperty property = new SimpleStringProperty();
                    property.setValue(gehalt + " €");
                    return property;
                });
        gehalt.setCellFactory(TextFieldTableCell.forTableColumn());
        gehalt.setOnEditCommit(this);


        abteilung.setCellValueFactory(cellData -> cellData.getValue().getAbteilungProperty());
        abteilung.setCellFactory(TextFieldTableCell.forTableColumn());
        abteilung.setOnEditCommit(this);

        standort.setCellValueFactory(cellData -> cellData.getValue().getStandortProperty());

        land.setCellValueFactory(cellData -> cellData.getValue().getLandProperty());

        //Füge Listener zu den Feldern hinzu
        addListenerToReloadOn(idFilter);
        addListenerToReloadOn(vornameFilter);
        addListenerToReloadOn(nameFilter);
        addListenerToReloadOn(gehaltFilter);
        addListenerToReloadOn(abteilungFilter);
        addListenerToReloadOn(standortFilter);
        addListenerToReloadOn(landFilter);

        //Button
        mitarbeiterAdd.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String vorname = vornameAdd.getText();
                String name = nameAdd.getText();
                String gehalt = gehaltAdd.getText();
                String abteilung = abteilungAdd.getText();
                Datenbank db = new Datenbank();

                String next = "(SELECT Max(ID)+1 FROM Mitarbeiter)";
                String abteilungID = "(SELECT ID FROM Abteilung WHERE Name='" + abteilung + "')";
                String sql = "INSERT INTO Mitarbeiter (ID, Vorname, Name, Gehalt, Abteilung_ID) VALUES (" + next + ", '" + vorname + "', '" + name + "', " + gehalt + ", " + abteilungID + ")";
                try {
                    db.integerQuery(sql);
                    load();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                load();
                table.setItems(tableData);
                table.refresh();

            }
        });


        table.setItems(tableData);
    }

    private void addListenerToReloadOn(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            load();
                });
    }

    private boolean isHereAfterFiltering(Mitarbeiter mitarbeiter) {
        boolean ok = true;

        ok = ok && (mitarbeiter.getID().contains(idFilter.getText()) || idFilter.getText() == null);
        ok = ok && (mitarbeiter.getVorname().contains(vornameFilter.getText()) || vornameFilter.getText() == null);
        ok = ok && (mitarbeiter.getName().contains(nameFilter.getText()) || nameFilter.getText() == null);
        ok = ok && (mitarbeiter.getGehalt().contains(gehaltFilter.getText()) || gehaltFilter.getText() == null);
        ok = ok && (mitarbeiter.getAbteilung().contains(abteilungFilter.getText()) || abteilungFilter.getText() == null);
        ok = ok && (mitarbeiter.getStandort().contains(standortFilter.getText()) || standortFilter.getText() == null);
        ok = ok && (mitarbeiter.getLand().contains(landFilter.getText()) || landFilter.getText() == null);

        return ok;
    }

    private void load() {
        tableData = null;
        Datenbank db = new Datenbank();
        String query = "SELECT * FROM Mitarbeiteruebersicht";
        String[][] Mitarbeiterdaten;
        try {
            Mitarbeiterdaten = db.asArray(query);
            for (String[] temp : Mitarbeiterdaten) {
                if (tableData == null) {
                    tableData = FXCollections.observableArrayList();
                }
                else {
                    Mitarbeiter addable = new ui.neu.Mitarbeiter(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6]);
                    if (isHereAfterFiltering(addable)) {
                        tableData.add(addable);
                    }
                }
            }
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        catch (Exception e) {

        }
        table.setItems(tableData);
    }

    @Override
    public void handle(TableColumn.CellEditEvent<ui.neu.Mitarbeiter, String> t) {
        ui.neu.Mitarbeiter m = t.getTableView().getItems().get(t.getTablePosition().getRow());
        String columnName = t.getTableColumn().getText();
        String newVal = t.getNewValue();
        int column = t.getTablePosition().getColumn();
        try {
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
        }
        catch (Exception e) {
            fehler.setText("Der Wert " + newVal + " ist fuer die Spalte " + columnName + " nicht zulaessig");
        }
        table.refresh();
    }
}
