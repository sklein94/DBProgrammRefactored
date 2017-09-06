package refactored.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import refactored.classes.Mitarbeiter;
import refactored.db.Datenbank;
import refactored.exceptions.IncompatibleAttributesException;

import java.sql.*;

/**
 * Klasse zum Behandeln von allen Aktionen der Inhalte der Tabelle.
 *
 * @author sklein
 * @version 1.0
 */
public final class TabelleController{
    /**
     * Feld zum Eingeben des Textfilters für die ID.
     */
    @FXML
    private TextField idFilterField;

    /**
     * Feld zum Eingeben des Textfilters für den Vornamen.
     */
    @FXML
    private TextField vornameFilterField;

    /**
     * Feld zum Eingeben des Textfilters fürd en Namen.
     */
    @FXML
    private TextField nameFilterField;

    /**
     * Feld zum Eingeben des Textfilters für das Gehalt.
     */
    @FXML
    private TextField gehaltFilterField;

    /**
     * Feld zum Eingeben des Textfilters für die Abteilung.
     */
    @FXML
    private TextField abteilungFilterField;

    /**
     * Feld zum Eingeben des Textfilters für den Standort.
     */
    @FXML
    private TextField standortFilterField;

    /**
     * Feld zum Eingeben des Textfilters für das Land.
     */
    @FXML
    private TextField landFilterField;

    /**
     * Tabellenspalte für Id.
     */
    @FXML
    private TableColumn<Mitarbeiter, String> idColumn;

    /**
     * Tabellenspalte für Vorname.
     */
    @FXML
    private TableColumn<Mitarbeiter, String> vornameColumn;

    /**
     * Tabellenspalte für Name.
     */
    @FXML
    private TableColumn<Mitarbeiter, String> nameColumn;

    /**
     * Tabellenspalte für Gehalt.
     */
    @FXML
    private TableColumn<Mitarbeiter, String> gehaltColumn;

    /**
     * Tabellenspalte für Abteilung.
     */
    @FXML
    private TableColumn<Mitarbeiter, String> abteilungColumn;

    /**
     * Tabellenspalte für den Standort.
     */
    @FXML
    private TableColumn<Mitarbeiter, String> standortColumn;

    /**
     * Tabellenspalte für das Land.
     */
    @FXML
    private TableColumn<Mitarbeiter, String> landColumn;

    /**
     * Die Liste mit allen Tabellendaten der Mitarbeiter.
     */
    private final ObservableList<Mitarbeiter> tableData = FXCollections.observableArrayList();

    /**
     * Die Tabelle mit den Mitarbeitern
     */
    @FXML
    private TableView<Mitarbeiter> table;

    /**
     * Initialisiert das Fenster.
     */
    @FXML
    private void initialize() {
        load();
        table.setItems(tableData);

        idColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyId());
        idColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        vornameColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyVorname());
        vornameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        vornameColumn.setOnEditCommit(t -> {
            Mitarbeiter m = t.getTableView().getItems().get(t.getTablePosition().getRow());
            try {
                m.setVorname(t.getNewValue());
            }
            catch (IncompatibleAttributesException e){
                popup(e.getMessage());
            }
            load();
           });

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyName());
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(t -> {
            Mitarbeiter m = t.getTableView().getItems().get(t.getTablePosition().getRow());
            try {
                m.setName(t.getNewValue());
            }
            catch (IncompatibleAttributesException e){
                popup(e.getMessage());
            }
            load();
        });

        gehaltColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyGehalt());
        gehaltColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        gehaltColumn.setOnEditCommit(t -> {
            Mitarbeiter m = t.getTableView().getItems().get(t.getTablePosition().getRow());
            try {
                m.setGehalt(t.getNewValue());
            }
            catch (IncompatibleAttributesException e){
                popup(e.getMessage());
            }
            load();
        });

        abteilungColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyAbteilung());
        abteilungColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        abteilungColumn.setOnEditCommit(t -> {
            Mitarbeiter m = t.getTableView().getItems().get(t.getTablePosition().getRow());

            try {
                m.setAbteilung(t.getNewValue());
            }
            catch (IncompatibleAttributesException e){
                popup(e.getMessage());
            }
            load();
        });

        standortColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyStandort());

        landColumn.setCellValueFactory(cellData -> cellData.getValue().getPropertyLand());
    }

    /**
     * Lädt die Werte der Mitarbeitertabelle aus der Datenbank, filtert sie entsprechend
     * der Werte in den Filterfeldern und speichert diese in der ObservableList tableData.
     * Anschließend werden diese Werte in die Tabelle geschrieben.
     */
    private void load() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        tableData.clear();
        try {
            con = Datenbank.connect();
            ps = con.prepareStatement("SELECT * FROM Mitarbeiteruebersicht");
            rs = ps.executeQuery();
            while (rs.next()) {
                Mitarbeiter m = new Mitarbeiter(
                        rs.getString("id"),
                        rs.getString("vorname"),
                        rs.getString("name"),
                        rs.getString("gehalt"),
                        rs.getString("abteilung"),
                        rs.getString("standort"),
                        rs.getString("land")
                );


                if (isInFilter(m)) {
                    tableData.add(m);
                }
            }
            table.setItems(tableData);
            table.refresh();
        }
        catch (SQLException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
        catch (IncompatibleAttributesException e){
            System.out.println("Ein Fehler beim Laden der Mitarbeiter aus der Datenbank ist aufgetreten!");
        }
        finally {
            if (rs != null){
                try{
                    rs.close();
                }
                catch (SQLException e){
                    System.out.println("Fehler beim Schließen des Datenbank-Results!");
                }
            }
            if (ps != null){
                try{
                    ps.close();
                }
                catch (SQLException e){
                    System.out.println("Fehler beim Schließen des Datenbank-Statements!");
                }
            }
            if (con != null){
                try{
                    con.close();
                }
                catch (SQLException e){
                    System.out.println("Fehler beim Schließen der Datenbank-Connection!");
                }
            }
        }

    }

    /**
     * Filtert, ob der als Parameter übergebene Mitarbeiter den Filtern in den Textfeldern entspricht.
     * @param mitarbeiter Der zu überprüfende Mitarbeiter
     * @return Gibt wahr aus, wennn der Mitarbeiter dem Filter entspricht, ansonsten falsch.
     */
    private boolean isInFilter(final Mitarbeiter mitarbeiter) {
        return
                (mitarbeiter.getId().toLowerCase().contains(idFilterField.getText().toLowerCase()) || idFilterField.getText().length() == 0)
                        && (mitarbeiter.getId().toLowerCase().contains(vornameFilterField.getText().toLowerCase()) || vornameFilterField.getText().length() == 0)
                        && (mitarbeiter.getId().toLowerCase().contains(nameFilterField.getText().toLowerCase()) || nameFilterField.getText().length() == 0)
                        && (mitarbeiter.getId().toLowerCase().contains(gehaltFilterField.getText().toLowerCase()) || gehaltFilterField.getText().length() == 0)
                        && (mitarbeiter.getId().toLowerCase().contains(abteilungFilterField.getText().toLowerCase()) || abteilungFilterField.getText().length() == 0)
                        && (mitarbeiter.getId().toLowerCase().contains(standortFilterField.getText().toLowerCase()) || standortFilterField.getText().length() == 0)
                        && (mitarbeiter.getId().toLowerCase().contains(landFilterField.getText().toLowerCase()) || landFilterField.getText().length() == 0);
    }


    /**
     * Erstellt ein neues Popup-Fenster mit einer Nachricht
     *
     * @param message Die Nachricht, die im Popup erscheint.
     */
    private void popup(final String message){
        Stage stage = new Stage();
        stage.setTitle("Error");

        Group g = new Group();

        Label l = new Label(message);

        g.getChildren().addAll(l);

        Scene scene = new Scene(g, 200, 200);
        stage.setScene(scene);
        stage.show();
    }
}
