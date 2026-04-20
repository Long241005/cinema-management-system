package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Cấu hình kết nối PostgreSQL
    private static final String SERVER = "localhost";
    private static final String PORT = "5432";
    private static final String DATABASE = "cinema_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres"; // Thay đổi password của bạn
    private static final String JDBC_URL = String.format(
            "jdbc:postgresql://%s:%s/%s",
            SERVER, PORT, DATABASE
    );

    private DatabaseConnection() {
        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            System.out.println("✓ Kết nối PostgreSQL thành công");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Không tìm thấy PostgreSQL JDBC Driver");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Lỗi kết nối PostgreSQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lấy instance duy nhất (Singleton Pattern)
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Lấy đối tượng Connection
     */
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

    /**
     * Đóng kết nối
     */
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
