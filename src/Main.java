import db.Datenbank;

import java.sql.*;

public class Main {



    public static void main(String[] args) throws Exception {
        Datenbank db = new Datenbank();
        db.printSQL("SELECT * FROM Mitarbeiter");
    }
}
