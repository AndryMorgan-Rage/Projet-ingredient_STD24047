import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private  String JDBC_URL = "jdbc:postgresql://localhost:5432/mini_dish_db";
    private String DB_USER = "mini_dish_db_manager";
    private String DB_PASSWORD = "1234";

    public Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }
}