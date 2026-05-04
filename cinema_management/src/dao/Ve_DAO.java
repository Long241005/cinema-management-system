package dao;

import java.sql.*;
import java.math.BigDecimal;
import connect.DatabaseConnection;

public class Ve_DAO {
    public boolean themVeTuDong(String maSC, BigDecimal giaVe) {
        // Logic: Thêm vé cho tất cả ghế của phòng thuộc suất chiếu đó
        String sql = "INSERT INTO Ve (maVe, maSC, maGhe, giaVe, trangThai) " +
                     "SELECT 'V' + CAST(NEXT VALUE FOR RowCounter AS VARCHAR), ?, maGhe, ?, N'Trống' " +
                     "FROM Ghe WHERE maPhong = (SELECT maPhong FROM SuatChieu WHERE maSC = ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSC);
            stmt.setBigDecimal(2, giaVe);
            stmt.setString(3, maSC);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}