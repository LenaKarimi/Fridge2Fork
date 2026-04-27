package Database;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbConnection {

    private static final String URL = "";
    private static final String User = "...";
    private static final String Password = "...";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, User, Password);
    }
}
