package Database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manges a single shared database connection.
 * It implements a simple connection that ensure that only one active database connection is used throughout the application.
 * Database credentials are loaded from env file.
 * @author Lena and Racil
 */
public class DbConnection {
    private static final Dotenv dotenv = Dotenv.load();

    public static final String URL = dotenv.get("DB_F2F_URL");
    public static final String USER = dotenv.get("DB_F2F_USER");
    public static final String PASSWORD = dotenv.get("DB_F2F_PASSWORD");

    /**
     * Returns an active database connection.
     * If no connection exists or the current one is closed
     * a new connection is created using environment.
     * @return an active connection to the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        System.out.println("öppnar db upkoppling");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
