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

            String url = System.getenv("DB_F2F_URL");
            String user = System.getenv("DB_F2F_USER");
            String password = System.getenv("DB_F2F_PASSWORD");

            connection = DriverManager.getConnection(
                    url,
                    user,
                    password

            );
        }
        return connection;
    }


}
