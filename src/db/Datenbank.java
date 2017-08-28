package db;

import javax.xml.transform.Result;
import java.sql.*;

public class Datenbank {
    private static final String HOSTNAME = "egmontix";
    private static final String PORT = "1521";
    private static final String SID = "dev3";
    private static final String USERNAME = "sklein";
    private static final String PASSWORD = "sklein";
    private static final String URL = "jdbc:oracle:thin:@" + HOSTNAME + ":" + PORT + ":" + SID;
    private Connection con;
    private Statement stmt;

    public Datenbank(){
        connect();
    }

    private void connect(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Driver myDriver = new oracle.jdbc.driver.OracleDriver();
            DriverManager.registerDriver(myDriver);
            this.con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        catch(Exception e){
            System.out.println("Exception: " + e.getMessage());
        }


        try {
            stmt = con.createStatement();
        }
        catch (SQLException e) {
            System.out.println("Exception: " + e.getMessage());
        }

    }

    public void printSQL(String query) throws Exception{
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int i = 1; i <= columns; i++){
            System.out.print(rsmd.getColumnName(i)+ " ");
        }
        System.out.println();

        while (rs.next()){
            for (int i = 1; i <= columns; i++){
                String value = rs.getString(i);
                System.out.print(value+ " ");
            }
            System.out.println();
        }
    }

    public String[][] asArray(String query) throws Exception{
        int rows = rows(query);
        ResultSet rs = stmt.executeQuery(query);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();

        String[][] result = new String[rows+1][columns];
        for (int i = 1; i <= columns; i++){
            result[0][i-1] = rsmd.getColumnName(i);
        }

        int count = 1;
        while (rs.next()){
            for (int i = 0; i < columns; i++){
                System.out.println(rs.getString(i+1));
                result[count][i] = rs.getString(i+1);
            }
            count++;
        }
        return result;
    }

    private int rows(String query) throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS NumberOfRows FROM ("+query+")");
        rs.next();
        return Integer.valueOf(rs.getString("NumberOfRows"));

    }
}
