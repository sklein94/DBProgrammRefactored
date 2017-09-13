package refactored.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import java.util.List;

/**
 * Enthält alle wichtigen Datenbankfunktionen.
 *
 * @author sklein
 * @version 1.0
 */
public final class Datenbank {
    /**
     * Der Username aus der Datenbank. Wird automatisch aus config.properties geladen. (Name: username)
     */
    private static final String USERNAME = loadFromConfig("username");
    /**
     * Das Passwort aus der Datenbank. Wird automatisch aus config.properties geladen. (Name: password)
     */
    private static final String PASSWORD = loadFromConfig("password");
    /**
     * Die URL aus der Datenbank. Wird automatisch aus config.properties geladen. (Name: url)
     */
    private static final String URL = loadFromConfig("url");

    /**
     * Privater Konstruktor zum Überlagern des Standardkonstruktors.
     * Soll verhindern, dass Objekte von diesem Typ erzeugt werden können.
     */
    private Datenbank() {

    }

    /**
     * Lädt einen Wert aus einer Datei namens config.properties.
     *
     * @param type Der Name des Konfigurationswertes aus der Konfigurationsdatei.
     * @return Der aus der Konfigurationsdatei geladene Wert, falls vorhanden. Ansonsten null.
     */
    private static String loadFromConfig(final String type) {
        Properties prop = new Properties();

        try {
            String filename = "config.properties";
            InputStream input = ui.neu.db.Datenbank.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                throw new IOException("Unable to find " + filename);
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            return prop.getProperty(type);

        }
        catch (IOException ex) {
            System.exit(-1);
        }
        return null;
    }

    /**
     * Verbindet sich mit der Datenbank und gibt die Connection aus.
     *
     * @return Gibt eine neue Connection zu der Datenbank aus.
     */
    public static Connection connect() {
        Connection con = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Driver myDriver = new oracle.jdbc.driver.OracleDriver();
            DriverManager.registerDriver(myDriver);
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            con.setAutoCommit(false);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Treiber wurde nicht gefunden!");
        }
        catch (IllegalAccessException e) {
            System.out.println("Treiber wurde nicht gefunden!");
        }
        catch (InstantiationException e) {
            System.out.println("Treiber wurde nicht gefunden!");
        }
        catch (SQLException e) {
            System.out.println("Fehler beim Verbinden mit der Datenbank!");
        }
        return con;
    }

    /**
     * Führt die SQL-Query als Prepared Statement aus. Gibt dabei die Anzahl der veränderten Reihen zurück.
     */
    public static int updateSQL(final String sqlQuery, final String... arguments) {
        Connection con = null;
        PreparedStatement ps = null;
        int returnValue = 0;
        try {
            con = connect();
            ps = con.prepareStatement(sqlQuery);
            for (int i = 0; i < arguments.length; i++) {
                ps.setString(i + 1, arguments[i]);
            }
            returnValue = ps.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("Fehler beim Erstellen der Datenbank-Connection!");
        }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                }
                catch (SQLException e) {
                    System.out.println("Fehler beim Schließen des Datenbank-Statements.");
                }
            }
            if (con != null) {
                try {
                    con.close();
                }
                catch (SQLException e) {
                    System.out.println("Fehler beim Schließen der Datenbank-Connection.");
                }
            }
        }
        return returnValue;
    }

    /**
     * Führt die SQL-Query als Prepared Statement aus. Gibt dabei ein ResultSet zurück.
     */
    public static List<String[]> resultSQL(final String sqlQuery, final String... arguments) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        List<String[]> returnValue = new ArrayList<>();
        try {
            con = connect();
            ps = con.prepareStatement(sqlQuery);
            for (int i = 0; i < arguments.length; i++) {
                ps.setString(i + 1, arguments[i]);
            }
            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            int columns = rsmd.getColumnCount();
            while (rs.next()) {
                String[] temp = new String[columns];
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = rs.getString(i + 1);
                }
                returnValue.add(temp);
            }
        }
        catch (SQLException e) {
            System.out.println("Fehler beim Erstellen der Datenbank-Connection!");
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException e) {
                    System.out.println("Fehler beim Schließen des Datenbank-Result-Sets.");
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                }
                catch (SQLException e) {
                    System.out.println("Fehler beim Schließen des Datenbank-Statements.");
                }
            }
            if (con != null) {
                try {
                    con.close();
                }
                catch (SQLException e) {
                    System.out.println("Fehler beim Schließen der Datenbank-Connection.");
                }
            }
        }
        return returnValue;
    }
}
