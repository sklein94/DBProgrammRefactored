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

import java.math.BigDecimal;
import java.sql.*;

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
                String gehaltAddString = gehaltAdd.getText().replace("€", "").replace(",", ".").replace(" ", "");
                String abteilungAddString = abteilungAdd.getText();
                Datenbank db = new Datenbank();


                if (validateParameters(vornameAddString, nameAddString, gehaltAddString, abteilungAddString)) {
                    try {
                        db.addMitarbeiter(vornameAddString, nameAddString, gehaltAddString, abteilungAddString);

                        load();
                        vornameAdd.clear();
                        abteilungAdd.clear();
                        gehaltAdd.clear();
                        nameAdd.clear();
                        fehler.setText("Mitarbeiter erfolgreich hinzugefuegt.");
                    }
                    catch (SQLIntegrityConstraintViolationException e) {
                        fehler.setText("Es wurden nicht alle Felder richtig oder vollstaendig ausgefuellt!");
                    }
                    catch (SQLException sq) {
                        fehler.setText("Konnte neuen Mitarbeiter nicht hinzufuegen.");
                    }

                    load();
                }
            }
        });


        table.setItems(tableData);
    }

    private boolean validateParameters(final String vorname, final String name, final String gehalt, final String abteilung) {
        String failureText = "";

        if (validateVorname(vorname) != null) {
            failureText += validateVorname(vorname) + "\n";
        }
        if (validateNachname(name) != null) {
            failureText += validateNachname(name) + "\n";
        }
        if (validateGehalt(gehalt) != null) {
            failureText += validateGehalt(gehalt) + "\n";
        }
        if (validateAbteilung(abteilung) != null) {
            failureText += validateAbteilung(abteilung) + "\n";
        }

        if (failureText.length() > 0) {
            fehler.setText(failureText);
            return false;
        }
        else return true;
    }

    private String validateVorname(final String vorname) {
        if (vorname.length() < 2) {
            return "Der Vorname ist zu kurz!";
        }
        else if (vorname.length() > 42) {
            return "Der Vorname ist zu lang!";
        }
        else return null;
    }

    private String validateNachname(final String name) {
        if (name.length() == 0) {
            return "Der Name ist ein Pflichtfeld!";
        }
        else if (name.length() < 2) {
            return "Der Name ist zu kurz!";
        }
        else if (name.length() > 42) {
            return "Der Name ist zu lang!";
        }
        else return null;
    }

    private String validateGehalt(final String gehalt) {
        if (gehalt.length() == 0) {
            return "Das Gehalt ist ein Pflichtfeld!";
        }
        try {
            BigDecimal bd = new BigDecimal(gehalt);
        }
        catch (NumberFormatException e) {
            return "Das Gehalt enthaelt ungueltige Zeichen!";
        }
        return null;
    }

    private String validateAbteilung(final String abteilung) {
        if (abteilung.length() == 0) {
            return "Die Abteilung ist ein Pflichtfeld!";
        }
        try {
            PreparedStatement ps = Datenbank.giveConnection().prepareStatement("SELECT COUNT(*) FROM Abteilung WHERE NAME=?");
            ps.setString(1, abteilung);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (Integer.parseInt(rs.getString(1)) != 1) {
                return "Die Abteilung existiert nicht!";
            }
        }
        catch (SQLException e) {
            return "Die Abteilung existiert nicht!";
        }
        return null;
    }

    private void addListenerToReloadOn(final TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            load();
        });
    }

    private boolean isHereAfterFiltering(final Mitarbeiter mitarbeiter) {
        return
                (mitarbeiter.getID().toLowerCase().contains(idFilter.getText().toLowerCase()) || idFilter.getText().toLowerCase() == null)
                        && (mitarbeiter.getVorname().toLowerCase().contains(vornameFilter.getText().toLowerCase()) || vornameFilter.getText().toLowerCase() == null)
                        && (mitarbeiter.getName().toLowerCase().contains(nameFilter.getText().toLowerCase()) || nameFilter.getText().toLowerCase() == null)
                        && (mitarbeiter.getGehalt().toLowerCase().contains(gehaltFilter.getText().toLowerCase()) || gehaltFilter.getText().toLowerCase() == null)
                        && (mitarbeiter.getAbteilung().toLowerCase().contains(abteilungFilter.getText().toLowerCase()) || abteilungFilter.getText().toLowerCase() == null)
                        && (mitarbeiter.getStandort().toLowerCase().contains(standortFilter.getText().toLowerCase()) || standortFilter.getText().toLowerCase() == null)
                        && (mitarbeiter.getLand().toLowerCase().contains(landFilter.getText().toLowerCase()) || landFilter.getText().toLowerCase() == null);
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
                    int idColumn = 0;
                    int vornameColumn = 1;
                    int nameColumn = 2;
                    int gehaltColumn = 3;
                    int abteilungColumn = 4;
                    int standortColumn = 5;
                    int landColumn = 6;
                    Mitarbeiter addable = new ui.neu.Mitarbeiter(temp[idColumn],
                            temp[vornameColumn],
                            temp[nameColumn],
                            temp[gehaltColumn],
                            temp[abteilungColumn],
                            temp[standortColumn],
                            temp[landColumn]
                    );
                    if (isHereAfterFiltering(addable)) {
                        tableData.add(addable);
                    }
                }
            }
        }
        catch (SQLException sqle) {
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
        final int COLUMN_VORNAME = 1;
        final int COLUMN_NAME = 2;
        final int COLUMN_GEHALT = 3;
        final int COLUMN_ABTEILUNG = 4;
        try {
            switch (column) {
                case COLUMN_VORNAME:
                    m.setVorname(newVal);
                    break;
                case COLUMN_NAME:
                    m.setName(newVal);
                    break;
                case COLUMN_GEHALT:
                    newVal = newVal.replace(" ", "");
                    newVal = newVal.replace("€", "");
                    newVal = newVal.replace(',', '.');
                    m.setGehalt(newVal);
                    break;
                case COLUMN_ABTEILUNG:
                    m.setAbteilung(newVal);
                    break;
                default:
                    break;
            }
            fehler.setText("Der Wert " + newVal + " ist fuer die Spalte " + columnName + " erfolgreich eingetragen worden.");
        }
        catch (Exception e) {
            fehler.setText("Der Wert " + newVal + " ist fuer die Spalte " + columnName + " nicht zulaessig\n" + e.getMessage());
        }
        load();
        table.refresh();
    }
}
