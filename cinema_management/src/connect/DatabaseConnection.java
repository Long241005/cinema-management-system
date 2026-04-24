package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Cấu hình cho SQL Server
    private static final String SERVER = "localhost";
    private static final String PORT = "1433"; // Cổng mặc định của SQL Server
    private static final String DATABASE = "RapChieuPhim";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword"; 

    // URL chuẩn cho SQL Server
    private static final String JDBC_URL = String.format(
            "jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;",
            SERVER, PORT, DATABASE
    );

    private DatabaseConnection() {
        try {
            // Load SQL Server JDBC Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            System.out.println("✓ Kết nối SQL Server thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Không tìm thấy SQL Server JDBC Driver. Hãy add file .jar vào thư mục lib!");
        } catch (SQLException e) {
            System.err.println("✗ Lỗi kết nối SQL Server: " + e.getMessage());
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Đã đóng kết nối");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}