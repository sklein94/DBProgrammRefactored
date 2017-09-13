package refactored.classes;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import refactored.db.Datenbank;
import refactored.exceptions.IncompatibleAttributesException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
                       final String landParam) throws IncompatibleAttributesException {
        this.setId(idParam);
        this.setVorname(vornameParam);
        this.setName(nameParam);
        this.setGehalt(gehaltParam);
        this.setAbteilung(abteilungParam);
        this.setStandort(standortParam);
        this.setLand(landParam);
    }

    /**
     * Setzt ein Attribut auf einen Wert.
     *
     * @param value Der Wert, auf den das Attribut gesetzt werden soll.
     */
    public void setId(final String value) throws IncompatibleAttributesException {
        if (value.length() == 0) throw new IncompatibleAttributesException("Die ID ist ein Pflichtfeld!");
        try {
            Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
            throw new IncompatibleAttributesException("Die ID enthaelt ungueltige Zeichen!");
        }
        id = new SimpleStringProperty(value);
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
    public void setVorname(final String value) throws IncompatibleAttributesException {
        if (value.length() < 2 && value.length() > 0)
            throw new IncompatibleAttributesException("Der Vorname ist zu kurz!");
        if (value.length() == 0) throw new IncompatibleAttributesException("Der Vorname ist ein Pflichtfeld!");
        vorname = new SimpleStringProperty(value);
        String sql = "UPDATE MITARBEITER SET VORNAME=? WHERE ID=?";
        Datenbank.updateSQL(sql, value, getId());
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
     * @param value Der Wert, auf den das Attribut gesetzt werden soll.
     * @throws SQLException bei Fehlerfall.
     */
    public void setName(final String value) throws IncompatibleAttributesException {
        if (value.length() < 2 && value.length() > 0)
            throw new IncompatibleAttributesException("Der Vorname ist zu kurz!");
        if (value.length() == 0) throw new IncompatibleAttributesException("Der Vorname ist ein Pflichtfeld!");
        name = new SimpleStringProperty(value);
        String sql = "UPDATE MITARBEITER SET NAME=? WHERE ID=?";
        Datenbank.updateSQL(sql, value, getId());
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
    public void setGehalt(final String value) throws IncompatibleAttributesException {
        try {
            Double.parseDouble(value);
        }
        catch (NumberFormatException e) {
            throw new IncompatibleAttributesException("Das Gehalt enthaelt ungueltige Zeichen!");
        }
        if (value.length() == 0) throw new IncompatibleAttributesException("Das Gehalt ist ein Pflichtfeld!");
        gehalt = new SimpleStringProperty(value);
        String sql = "UPDATE MITARBEITER SET GEHALT=? WHERE ID=?";
        Datenbank.updateSQL(sql, value, getId());
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
    public void setAbteilung(final String value) throws IncompatibleAttributesException {
        if (value.length() == 0) throw new IncompatibleAttributesException("Die Abteilung ist ein Pflichtfeld!");
        List<String[]> resultsAsStringArray = Datenbank.resultSQL("SELECT COUNT(*) AS Anzahl FROM Abteilung WHERE Name=?", value);
        if (Integer.parseInt(resultsAsStringArray.get(0)[0]) != 1) throw new IncompatibleAttributesException("Abteilung " + value + " nicht gefunden!");
        abteilung = new SimpleStringProperty(value);
        String sql = "UPDATE MITARBEITER SET ABTEILUNG_ID=(SELECT ID FROM ABTEILUNG WHERE NAME=?) WHERE ID=?";
        Datenbank.updateSQL(sql, value, getId());
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

    /**
     * Fuegt alle Werte des Mitarbeiters der Datenbank hinzu.
     */
    public void addToDatabase() {
        String sql = "INSERT INTO Mitarbeiter (ID, Vorname, Name, Gehalt, Abteilung) VALUES (M_NR.NEXTVAL, ?, ?, ?, (SELECT ID FROM ABTEILUNG WHERE NAME=?))";
        Datenbank.updateSQL(sql, getVorname(), getName(), getGehalt(), getAbteilung());
    }
}
