package mg.fita.kinkinasaifu.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static Connection connection;

    private ConnectionDB(){
        String url = System.getenv("DATABASE_URL");
        String user = System.getenv("DATABASE_USER");
        String password = System.getenv("DATABASE_PASSWORD");

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection successfull !");
        } catch (SQLException e) {
            System.err.println("Error connecting to the PostgreSQL database: " + e.getMessage());
        }
    }
    public static Connection getConnection(){
        if (connection == null) {
            new ConnectionDB();
        }
        return connection;
    }
}