package refactored.classes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import refactored.db.Datenbank;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Die Klasse Mitarbeiter dient dazu, eine gesamte Tabellenreihe in den Attributen zu speichern.
 *
 * @author sklein
 * @version 1.0
 */
public final class Mitarbeiter {
    /**
     * String-Property von ID.
     */
    private StringProperty id;
    /**
     * String-Property von Vorname.
     */
    private StringProperty vorname;
    /**
     * String-Property von Name.
     */
    private StringProperty name;
    /**
     * String-Property von Gehalt.
     */
    private StringProperty gehalt;
    /**
     * String-Property von Abteilung.
     */
    private StringProperty abteilung;
    /**
     * String-Property von Standort.
     */
    private StringProperty standort;
    /**
     * String-Property von Land.
     */
    private StringProperty land;

    /**
     * Konstruktor der Klasse. Weist allen Attributen die übergebenen Werte zu.
     *
     * @param idParam        Stringwert von id
     * @param vornameParam   Stringwert von vorname
     * @param nameParam      Stringwert von name
     * @param gehaltParam    Stringwert von gehalt
     * @param abteilungParam Stringwert von abteilung
     * @param standortParam  Stringwert von standort
     * @param landParam      Stringwert von land
     */
    public Mitarbeiter(final String idParam,
                       final String vornameParam,
                       final String nameParam,
                       final String gehaltParam,
                       final String abteilungParam,
                       final String standortParam,
                       final String landParam) {
        this.id = new SimpleStringProperty(idParam);
        this.vorname = new SimpleStringProperty(vornameParam);
        this.name = new SimpleStringProperty(nameParam);
        this.gehalt = new SimpleStringProperty(gehaltParam);
        this.abteilung = new SimpleStringProperty(abteilungParam);
        this.standort = new SimpleStringProperty(standortParam);
        this.land = new SimpleStringProperty(landParam);
    }

    /**
     * Setzt ein Attribut auf einen Wert.
     *
     * @param value Der Wert, auf den das Attribut gesetzt werden soll.
     */
    public void setId(final String value) {
        vorname = new SimpleStringProperty(value);
    }

    /**
     * @return Gibt die ID als String aus.
     */
    public String getId() {
        return id.get();
    }

    /**
     * @return Gibt die ID als StringProperty aus.
     */
    public StringProperty getPropertyId() {
        return id;
    }


    /**
     * Setzt ein Attribut auf einen Wert.
     *
     * @param value Der Wert, auf den das Attribut gesetzt werden soll.
     * @throws SQLException bei Fehlerfall
     */
    public void setVorname(final String value) throws SQLException {
        vorname = new SimpleStringProperty(value);
        Connection con = Datenbank.connect();
        PreparedStatement ps = con.prepareStatement("UPDATE MITARBEITER SET VORNAME=? WHERE ID=?");
        ps.setString(1, value);
        ps.setString(2, this.getId());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    /**
     * @return Gibt den Vornamen als String aus.
     */
    public String getVorname() {
        return vorname.get();
    }

    /**
     * @return Gibt den Vornamen als StringProperty aus.
     */
    public StringProperty getPropertyVorname() {
        return vorname;
    }

    /**
     * Setzt ein Attribut auf einen Wert.
     *
     * @throws SQLException bei Fehlerfall.
     * @param value Der Wert, auf den das Attribut gesetzt werden soll.
     */
    public void setName(final String value) throws SQLException{
        name = new SimpleStringProperty(value);
        Connection con = Datenbank.connect();
        PreparedStatement ps = con.prepareStatement("UPDATE MITARBEITER SET NAME=? WHERE ID=?");
        ps.setString(1, value);
        ps.setString(2, this.getId());
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    /**
     * @return Gibt den Namen als String aus.
     */
    public String getName() {
        return name.get();
    }

    /**
     * @return Gibt den Namen als StringProperty aus.
     */
    public StringProperty getPropertyName() {
        return name;
    }

    /**
     * Setzt ein Attribut auf einen Wert.
     *
     * @param value Der Wert, auf den das Attribut gesetzt werden soll.
     */
    public void setGehalt(final String value) {
        gehalt = new SimpleStringProperty(value);
    }

    /**
     * @return Gibt das Gehalt als String aus.
     */
    public String getGehalt() {
        return gehalt.get();
    }

    /**
     * @return Gibt das Gehalt als StringProperty aus.
     */
    public StringProperty getPropertyGehalt() {
        return gehalt;
    }

    /**
     * Setzt ein Attribut auf einen Wert.
     *
     * @param value Der Wert, auf den das Attribut gesetzt werden soll.
     */
    public void setAbteilung(final String value) throws SQLException{
        abteilung = new SimpleStringProperty(value);
        Connection con = Datenbank.connect();
        //TODO
        /*PreparedStatement ps = con.prepareStatement("UPDATE MITARBEITER SET NAME=? WHERE ID=?");
        ps.setString(1, value);
        ps.setString(2, this.getId());
        ps.executeUpdate();
        ps.close();
        con.close();*/
    }

    /**
     * @return Gibt die Abteilung als String aus.
     */
    public String getAbteilung() {
        return abteilung.get();
    }

    /**
     * @return Gibt die Abteilung als StringProperty aus.
     */
    public StringProperty getPropertyAbteilung() {
        return abteilung;
    }

    /**
     * Setzt ein Attribut auf einen Wert.
     *
     * @param value Der Wert, auf den das Attribut gesetzt werden soll.
     */
    public void setStandort(final String value) {
        standort = new SimpleStringProperty(value);
    }

    /**
     * @return Gibt den Standort als String aus.
     */
    public String getStandort() {
        return standort.get();
    }

    /**
     * @return Gibt den Standort als StringProperty aus.
     */
    public StringProperty getPropertyStandort() {
        return standort;
    }

    /**
     * Setzt ein Attribut auf einen Wert.
     *
     * @param value Der Wert, auf den das Attribut gesetzt werden soll.
     */
    public void setLand(final String value) {
        land = new SimpleStringProperty(value);
    }

    /**
     * @return Gibt das Land als String aus.
     */
    public String getLand() {
        return land.get();
    }

    /**
     * @return Gibt das Land als StringProperty aus.
     */
    public StringProperty getPropertyLand() {
        return land;
    }
}
