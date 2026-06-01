package Database;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manges a single shared database connection.
 * It implements a simple connection that ensure that only one active database connection is used throughout the application.
 * Database credentials are loaded from environment variables.
 * @author Lena and Racil
 */
public class DbConnection {
    private static Connection connection;

    /**
     * Returns an active database connection.
     * If no connection exists or the current one is closed
     * a new connection is created using environment.
     * @return an active connection to the database
     * @throws SQLException if a database access error occurs
     */
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
