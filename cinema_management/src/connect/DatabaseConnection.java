package connect;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        connect();
    }

    public void connect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            connection = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=RapChieuPhim;encrypt=true;trustServerCertificate=true",
                    "sa",
                    "sapassword"
            );

            System.out.println("Kết nối thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }

}