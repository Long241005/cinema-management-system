package connect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
	private static DatabaseConnection instance;
    private Connection connection;

    // Cấu hình kết nối SQL Server
    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE = "CinemaDB";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword"; // Thay đổi password của bạn
    private static final String JDBC_URL = String.format(
            "jdbc:sqlserver://%s:%s;databaseName=%s;trustServerCertificate=true",
            SERVER, PORT, DATABASE
    );

    private DatabaseConnection() {
        try {
            // Load SQL Server JDBC Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            System.out.println("✓ Kết nối SQL Server thành công");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Không tìm thấy SQL Server JDBC Driver");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Lỗi kết nối SQL Server: " + e.getMessage());
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
