import db.Datenbank;

import java.sql.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Datenbank db = new Datenbank();
        String query = "SELECT * FROM Mitarbeiter";
        //db.printSQL(query);

        String[][] result = db.asArray(query);
        for (String[] a : result){
            for (String b : a){
                System.out.print(b+ " ");
            }
            System.out.println();
        }
    }
}
