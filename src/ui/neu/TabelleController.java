package ui.neu;


import db.Datenbank;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public final class TabelleController implements EventHandler<TableColumn.CellEditEvent<ui.neu.Mitarbeiter, String>> {
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
    private TableView<Mitarbeiter> table;

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

        gehalt.setCellValueFactory(cellData -> cellData.getValue().getGehaltProperty());
        gehalt.setCellFactory(TextFieldTableCell.forTableColumn());
        gehalt.setOnEditCommit(this);
        gehalt.setCellValueFactory(cellData ->
                Bindings.format("%.2f €", Double.valueOf(cellData.getValue().getGehalt())));


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
            public void handle(final MouseEvent event) {
                String vornameAddString = vornameAdd.getText();
                String nameAddString = nameAdd.getText();
                String gehaltAddString = gehaltAdd.getText();
                String abteilungAddString = abteilungAdd.getText();
                Datenbank db = new Datenbank();

                String next = "(SELECT Max(ID)+1 FROM Mitarbeiter)";
                String abteilungID = "(SELECT ID FROM Abteilung WHERE Name='" + abteilungAddString + "')";
                String sql = "INSERT INTO Mitarbeiter (ID, Vorname, Name, Gehalt, Abteilung_ID) VALUES (" + next + ", '" + vornameAddString + "', '" + nameAddString + "', " + gehaltAddString + ", " + abteilungID + ")";
                try {
                    db.integerQuery(sql);
                    load();
                    vornameAdd.clear();
                    abteilungAdd.clear();
                    gehaltAdd.clear();
                    nameAdd.clear();
                    fehler.setText("Mitarbeiter erfolgreich hinzugefuegt.");
                }
                catch (SQLException sq) {
                    fehler.setText("Konnte neuen Mitarbeiter nicht hinzufuegen.");

                }

                load();
            }
        });


        table.setItems(tableData);
    }

    private void addListenerToReloadOn(final TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            load();
        });
    }

    private boolean isHereAfterFiltering(final Mitarbeiter mitarbeiter) {
        boolean ok = true;

        ok = ok && (mitarbeiter.getID().toLowerCase().contains(idFilter.getText().toLowerCase()) || idFilter.getText().toLowerCase() == null);
        ok = ok && (mitarbeiter.getVorname().toLowerCase().contains(vornameFilter.getText().toLowerCase()) || vornameFilter.getText().toLowerCase() == null);
        ok = ok && (mitarbeiter.getName().toLowerCase().contains(nameFilter.getText().toLowerCase()) || nameFilter.getText().toLowerCase() == null);
        ok = ok && (mitarbeiter.getGehalt().toLowerCase().contains(gehaltFilter.getText().toLowerCase()) || gehaltFilter.getText().toLowerCase() == null);
        ok = ok && (mitarbeiter.getAbteilung().toLowerCase().contains(abteilungFilter.getText().toLowerCase()) || abteilungFilter.getText().toLowerCase() == null);
        ok = ok && (mitarbeiter.getStandort().toLowerCase().contains(standortFilter.getText().toLowerCase()) || standortFilter.getText().toLowerCase() == null);
        ok = ok && (mitarbeiter.getLand().toLowerCase().contains(landFilter.getText().toLowerCase()) || landFilter.getText().toLowerCase() == null);

        return ok;
    }

    private void load() {
        tableData = null;
        Datenbank db = new Datenbank();
        String query = "SELECT * FROM Mitarbeiteruebersicht";
        String[][] mitarbeiterdaten;
        try {
            mitarbeiterdaten = db.asArray(query);
            for (String[] temp : mitarbeiterdaten) {
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
            //sqle.printStackTrace();
        }
        catch (Exception e) {

        }
        table.setItems(tableData);
    }

    @Override
    public void handle(final TableColumn.CellEditEvent<ui.neu.Mitarbeiter, String> t) {
        ui.neu.Mitarbeiter m = t.getTableView().getItems().get(t.getTablePosition().getRow());
        String columnName = t.getTableColumn().getText();
        String newVal = t.getNewValue();
        int column = t.getTablePosition().getColumn();
        try {
            switch (column) {
                case Mitarbeiter.COLUMN_VORNAME:
                    m.setVorname(newVal);
                    break;
                case Mitarbeiter.COLUMN_NAME:
                    m.setName(newVal);
                    break;
                case Mitarbeiter.COLUMN_GEHALT:
                    newVal = newVal.replace(" ", "");
                    newVal = newVal.replace("€", "");
                    newVal = newVal.replace(',', '.');
                    m.setGehalt(newVal);
                    break;
                case Mitarbeiter.COLUMN_ABTEILUNG:
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
