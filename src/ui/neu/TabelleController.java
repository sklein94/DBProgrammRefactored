package ui.neu;


import ch.makery.sortfilter.Person;
import db.Datenbank;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.logging.Filter;

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

        abteilung.setCellValueFactory(cellData -> cellData.getValue().getAbteilungProperty());
        abteilung.setCellFactory(TextFieldTableCell.forTableColumn());
        abteilung.setOnEditCommit(this);

        standort.setCellValueFactory(cellData -> cellData.getValue().getStandortProperty());

        land.setCellValueFactory(cellData -> cellData.getValue().getLandProperty());

        //Füge Listener zu den Feldern hinzu
        addListener(idFilter, "id");
        addListener(vornameFilter, "vorname");
        addListener(nameFilter, "name");
        addListener(gehaltFilter, "gehalt");
        addListener(abteilungFilter, "abteilung");
        addListener(standortFilter, "standort");
        addListener(landFilter, "land");


        table.setItems(tableData);
    }

    private void addListener(TextField textField, String field) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                ObservableList<ui.neu.Mitarbeiter> filteredList = FXCollections.observableArrayList();
                for (ui.neu.Mitarbeiter m : tableData) {
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

    private void load() {
        Datenbank db = new Datenbank();
        String query = "SELECT * FROM Mitarbeiteruebersicht";
        String[][] Mitarbeiterdaten;
        try {
            Mitarbeiterdaten = db.asArray(query);
            for (String[] temp : Mitarbeiterdaten) {
                if (tableData == null) {
                    tableData = FXCollections.observableArrayList();
                }
                else
                    tableData.add(new ui.neu.Mitarbeiter(temp[0], temp[1], temp[2], temp[3], temp[4], temp[5], temp[6]));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(TableColumn.CellEditEvent<ui.neu.Mitarbeiter, String> t) {
        ui.neu.Mitarbeiter m = t.getTableView().getItems().get(t.getTablePosition().getRow());
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
}
