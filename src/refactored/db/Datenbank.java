package refactored.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Enthält alle wichtigen Datenbankfunktionen.
 *
 * @author sklein
 * @version 1.0
 */
public final class Datenbank {
    /** Der Username aus der Datenbank. Wird automatisch aus config.properties geladen. (Name: username)*/
    private static final String USERNAME = loadFromConfig("username");
    /** Das Passwort aus der Datenbank. Wird automatisch aus config.properties geladen. (Name: password)*/
    private static final String PASSWORD = loadFromConfig("password");
    /** Die URL aus der Datenbank. Wird automatisch aus config.properties geladen. (Name: url)*/
    private static final String URL = loadFromConfig("url");

    /**
     * Privater Konstruktor zum Überlagern des Standardkonstruktors.
     * Soll verhindern, dass Objekte von diesem Typ erzeugt werden können.
     */
    private Datenbank(){

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
     * @return Gibt eine neue Connection zu der Datenbank aus.
     */
    public static Connection connect(){
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
}
