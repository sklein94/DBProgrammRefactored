import java.sql.*;

public class Main {
    private static final String HOSTNAME = "egmontix";
    private static final String PORT = "1521";
    private static final String SID = "dev3";
    private static final String USERNAME = "sklein";
    private static final String PASSWORD = "sklein";
    private static final String URL = "jdbc:oracle:thin:@" + HOSTNAME + ":" + PORT + ":" + SID;



    public static void main(String[] args) throws Exception {

        //Schritt 1
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }


        //Schritt 2
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }
        catch (IllegalAccessException ex) {
            System.out.println("Error: access problem while loading!");
            System.exit(2);
        }
        catch (InstantiationException ex) {
            System.out.println("Error: unable to instantiate driver!");
            System.exit(3);
        }


        //Schritt 3
        try {
            Driver myDriver = new oracle.jdbc.driver.OracleDriver();
            DriverManager.registerDriver(myDriver);
        }
        catch (Exception ex) {
            System.out.println("Error: unable to load driver class!");
            System.exit(1);
        }


        //Schritt 4
        Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);


        //Schritt 5
        Statement stmt = null;
        try {
            stmt = con.createStatement();
        }
        catch (SQLException e) {
            System.out.println("Fehler!!!!!");
        }

        //Schritt 6
        ResultSet rs = stmt.executeQuery("SELECT * FROM Mitarbeiter");

        while (rs.next()){
            String test = rs.getString("Name");
            System.out.println(test);
        }



        con.close();

    }
}
