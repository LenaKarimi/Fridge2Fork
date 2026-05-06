package Database;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbConnection {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        System.out.println("öppnar db upkoppling");
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    "", // url
                    "", // user
                    "" // lösen

            );
        }
        return connection;
    }

    /**
    private static final String URL = "jdbc:postgresql://postgres.mau.se:55432/fridge2fork";
    private static final String User = "aq2327";
    private static final String Password = "fdblm85i";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, User, Password);
    }
     **/
}
