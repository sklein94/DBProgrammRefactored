package ui.neu;

import db.Datenbank;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.sql.SQLException;

public class Mitarbeiter {
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

    //Getter und Setter für ID
    public String getID() {
        return this.id.get();
    }

    public StringProperty getIDProperty() {
        return this.id;
    }

    public void setID(String id) {
        this.id.set(id);
    }


    //Getter und Setter für Vorname
    public String getVorname() {
        return this.vorname.get();
    }

    public StringProperty getVornameProperty() {
        return this.vorname;
    }

    public void setVorname(String vorname) throws SQLException {
        Datenbank db = new Datenbank();
        db.integerQuery("UPDATE Mitarbeiter SET Vorname='" + vorname + "' WHERE ID=" + this.getID());
        this.vorname.set(vorname);
    }


    public String getName() {
        return this.name.get();
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public void setName(String name) throws SQLException {
        Datenbank db = new Datenbank();
        db.integerQuery("UPDATE Mitarbeiter SET Name='" + name + "' WHERE ID=" + this.getID());
        this.name.set(name);
    }

    public String getGehalt() {
        return this.gehalt.get();
    }

    public StringProperty getGehaltProperty() {
        return gehalt;
    }

    public void setGehalt(String gehalt) throws SQLException {
        BigDecimal bd = new BigDecimal(gehalt);
        Datenbank db = new Datenbank();
        db.integerQuery("UPDATE Mitarbeiter SET Gehalt=" + gehalt + " WHERE ID=" + this.getID());
        this.gehalt.set(gehalt);
    }

    public String getAbteilung() {
        return this.abteilung.get();
    }

    public StringProperty getAbteilungProperty() {
        return abteilung;
    }

    public void setAbteilung(String abteilung) throws SQLException {
        Datenbank db = new Datenbank();
        String sql = "UPDATE Mitarbeiter SET Abteilung_ID=(select ID from Abteilungsuebersicht WHERE Name='" + abteilung + "') WHERE ID=" + this.getID();
        db.integerQuery(sql);
        this.abteilung.set(abteilung);
        String sqlx = "SELECT Standort, Land FROM Mitarbeiteruebersicht WHERE ID=" + this.getID();
        String[][] arr = db.asArray(sqlx);
        this.setStandort(arr[1][0]);
        this.setLand(arr[1][1]);
    }

    public String getStandort() {
        return this.standort.get();
    }

    public StringProperty getStandortProperty() {
        return standort;
    }

    public void setStandort(String standort) {
        Datenbank db = new Datenbank();
        this.standort.set(standort);
    }

    public String getLand() {
        return this.land.get();
    }

    public StringProperty getLandProperty() {
        return land;
    }

    public void setLand(String land) {
        Datenbank db = new Datenbank();
        this.land.set(land);
    }
}