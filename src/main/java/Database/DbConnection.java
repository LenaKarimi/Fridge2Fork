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


}
