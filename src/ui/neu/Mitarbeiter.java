package ui.neu;

import db.Datenbank;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Mitarbeiter {
    private final StringProperty id;
    private final StringProperty vorname;
    private final StringProperty name;
    private final StringProperty gehalt;
    private final StringProperty abteilung;
    private final StringProperty standort;
    private final StringProperty land;

    public Mitarbeiter(final String id, final String vorname, final String name, final String gehalt, final String abteilung, final String standort, final String land) {
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

    public void setID(final String id) {
        this.id.set(id);
    }


    //Getter und Setter für Vorname
    public String getVorname() {
        return this.vorname.get();
    }

    public StringProperty getVornameProperty() {
        return this.vorname;
    }

    public void setVorname(final String vorname) throws SQLException {
        Connection con = Datenbank.giveConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE Mitarbeiter SET Vorname=? WHERE ID=?");
        ps.setString(1, vorname);
        ps.setString(2, this.getID());
        ps.executeUpdate();
        con.commit();
        con.close();
        this.vorname.set(vorname);
    }


    public String getName() {
        return this.name.get();
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public void setName(final String name) throws SQLException {
        Connection con = Datenbank.giveConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE Mitarbeiter SET Name=? WHERE ID=?");
        ps.setString(1, name);
        ps.setString(2, this.getID());
        ps.executeUpdate();
        con.commit();
        con.close();
        this.name.set(name);
    }

    public String getGehalt() {
        return this.gehalt.get();
    }

    public StringProperty getGehaltProperty() {
        return gehalt;
    }

    public void setGehalt(final String gehalt) throws SQLException {
        Connection con = Datenbank.giveConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE Mitarbeiter SET Gehalt=? WHERE ID=?");
        ps.setString(1, gehalt);
        ps.setString(2, this.getID());
        ps.executeUpdate();
        con.commit();
        con.close();
        this.gehalt.set(gehalt);
    }

    public String getAbteilung() {
        return this.abteilung.get();
    }

    public StringProperty getAbteilungProperty() {
        return abteilung;
    }



    public void setAbteilung(final String abteilung) throws SQLException {
        Connection con = Datenbank.giveConnection();
        PreparedStatement ps = con.prepareStatement("UPDATE Mitarbeiter SET Abteilung_ID=(select ID from Abteilungsuebersicht WHERE Name=?) WHERE ID=?");
        ps.setString(1, abteilung);
        ps.setString(2, this.getID());
        ps.executeUpdate();

        this.abteilung.set(abteilung);

        con.commit();
        con.close();
    }

    public String getStandort() {
        return this.standort.get();
    }

    public StringProperty getStandortProperty() {
        return standort;
    }

    public void setStandort(final String standort) {
        this.standort.set(standort);
    }

    public String getLand() {
        return this.land.get();
    }

    public StringProperty getLandProperty() {
        return land;
    }

    public void setLand(final String land) {
        this.land.set(land);
    }
}