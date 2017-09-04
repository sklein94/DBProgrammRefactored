package db;



import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public final class Datenbank {
    private static final String HOSTNAME = "egmontix";
    private static final String PORT = "1521";
    private static final String SID = "dev3";
    private static final String USERNAME = Datenbank.loadFromConfig("username");
    private static final String PASSWORD = Datenbank.loadFromConfig("password");
    private static final String URL = "jdbc:oracle:thin:@" + HOSTNAME + ":" + PORT + ":" + SID;

    private Connection con;

    public Datenbank() {
        connect();
    }

    private void connect() {
        try {
            Datenbank.loadFromConfig("dbpassword");
        }
        catch (Exception e){

        }
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Driver myDriver = new oracle.jdbc.driver.OracleDriver();
            DriverManager.registerDriver(myDriver);
            this.con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            con.setAutoCommit(false);
        }
        catch (ClassNotFoundException e) {
            System.out.println("Treiber wurde nicht gefunden!");
        }catch (IllegalAccessException e) {
            System.out.println("Treiber wurde nicht gefunden!");
        }catch (InstantiationException e) {
            System.out.println("Treiber wurde nicht gefunden!");
        }catch (SQLException e) {
            System.out.println("Fehler beim Verbinden mit der Datenbank!");
        }
    }

    public String[][] asArray(final String query) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query);
        int rows = rows(query);
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();

        String[][] result = new String[rows + 1][columns];
        for (int i = 1; i <= columns; i++) {
            result[0][i - 1] = rsmd.getColumnName(i);
        }

        int count = 1;
        while (rs.next()) {
            for (int i = 0; i < columns; i++) {
                result[count][i] = rs.getString(i + 1);
            }
            count++;
        }
        con.commit();
        return result;
    }

    private int rows(final String query) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) AS NumberOfRows FROM (" + query + ")");
        ResultSet rs = ps.executeQuery();
        rs.next();
        con.commit();
        return Integer.parseInt(rs.getString("NumberOfRows"));
    }

    public int integerQuery(final String sql) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        int returnValue = ps.executeUpdate();
        con.commit();
        return returnValue;
    }

    private static String loadFromConfig(String type){
        Properties prop = new Properties();



        try {
            String filename = "config.properties";
            InputStream input = Datenbank.class.getClassLoader().getResourceAsStream(filename);
            if(input==null){
                throw new IOException("Unable to find " + filename);
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            return prop.getProperty(type);

        } catch (IOException ex) {
            System.exit(-1);
        }
        return null;
    }

}
