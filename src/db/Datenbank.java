package db;



import java.sql.*;

public final class Datenbank {
    private static final String HOSTNAME = "egmontix";
    private static final String PORT = "1521";
    private static final String SID = "dev3";
    private static final String USERNAME = "sklein";
    private static final String PASSWORD = "sklein";
    private static final String URL = "jdbc:oracle:thin:@" + HOSTNAME + ":" + PORT + ":" + SID;

    private Connection con;
    private Statement stmt;

    public Datenbank() {
        connect();
    }

    private void connect() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            Driver myDriver = new oracle.jdbc.driver.OracleDriver();
            DriverManager.registerDriver(myDriver);
            this.con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            con.setAutoCommit(false);
            stmt = con.createStatement();
        }
        catch (Exception e) {
            /*e.printStackTrace();
            System.exit(1);*/
        }
    }

    public void printSQL(final String query) throws Exception {
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();

        for (int i = 1; i <= columns; i++) {
            System.out.print(rsmd.getColumnName(i) + " ");
        }
        System.out.println();

        while (rs.next()) {
            for (int i = 1; i <= columns; i++) {
                String value = rs.getString(i);
                System.out.print(value + " ");
            }
            System.out.println();
        }
        con.commit();
    }


    public void printSQLComplex(final String query) throws SQLException {
        PreparedStatement ps = con.prepareStatement(query);
        int rows = rows(query) + 1;
        ResultSet rs = ps.executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();


        if (columns <= 7 && columns > 0) {
            String format = "|";
            String formatDesign = "+";
            String titleDesign = "| ";
            for (int i = 0; i < columns; i++) {
                format += " %-32s |";
                formatDesign += "----------------------------------+";

                titleDesign += " ";
                titleDesign += rsmd.getColumnName(i + 1);
                while ((titleDesign.length() % 35) != 0) {
                    titleDesign += " ";
                }
                titleDesign += "|";
            }
            format += "%n";
            formatDesign += "%n";
            titleDesign += "%n";

            System.out.format(formatDesign);
            System.out.format(titleDesign);
            System.out.format(formatDesign);


            String[] var = new String[rows];
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    String value = rs.getString(i);
                    var[i - 1] = value;
                }
                switch (columns) {
                    case 1:
                        System.out.format(format, var[0]);
                        break;
                    case 2:
                        System.out.format(format, var[0], var[1]);
                        break;
                    case 3:
                        System.out.format(format, var[0], var[1], var[2]);
                        break;
                    case 4:
                        System.out.format(format, var[0], var[1], var[2], var[3]);
                        break;
                    case 5:
                        System.out.format(format, var[0], var[1], var[2], var[3], var[4]);
                        break;
                    case 6:
                        System.out.format(format, var[0], var[1], var[2], var[3], var[4], var[5]);
                        break;
                    case 7:
                        System.out.format(format, var[0], var[1], var[2], var[3], var[4], var[5], var[6]);
                        break;
                    case 8:
                        System.out.format(format, var[0], var[1], var[2], var[3], var[4], var[5], var[6], var[7]);
                        break;
                    default:
                        break;
                }
            }
            System.out.format(formatDesign);
        }
        else System.out.println("Zu viele oder zu wenige Spalten! (1-5)");
        con.commit();
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

    public boolean booleanQuery(final String sql) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        boolean returnValue = ps.execute();
        con.commit();
        return returnValue;
    }

}
