package AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String url = "jdbc:mysql://localhost:3306/newsdb";
    private static final String username = "root";
    private static final String password = "";

    public Connection getConnect() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
