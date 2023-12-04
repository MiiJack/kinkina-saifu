package mg.fita.kinkinasaifu.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfiguration {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        try{
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost/library_management",
                    System.getenv("DB_USER"),
                    System.getenv("DB_PASSWORD")
            );
            return connection;
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
            throw new RuntimeException("Connection failed");
        }
    }
}
