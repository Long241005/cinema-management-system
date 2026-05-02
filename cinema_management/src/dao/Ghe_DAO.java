package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connect.DatabaseConnection;
import entity.Ghe;
import entity.PhongChieu;

public class Ghe_DAO {

    /**
     * Chuyển đổi dữ liệu từ ResultSet sang thực thể Ghe (Tham chiếu đối tượng)
     */
    private Ghe mapGhe(ResultSet rs) throws SQLException {
        // Khởi tạo thực thể PhongChieu để làm tham chiếu
        PhongChieu pc = new PhongChieu();
        pc.setMaPhong(rs.getString("maPhong")); 

        return new Ghe(
            rs.getString("maGhe"),
            pc, // Truyền tham chiếu đối tượng trực tiếp
            rs.getString("hang"),
            rs.getInt("soGhe"),
            rs.getString("loaiGhe"),
            rs.getString("trangThai")
        );
    }

    // ================== TRUY VẤN DỮ LIỆU ==================

    /**
     * Tìm danh sách ghế theo mã phòng (Dùng để vẽ sơ đồ thực tế)
     */
    public List<Ghe> timGheTheoPhong(String maPhong) {
        List<Ghe> ds = new ArrayList<>();
        String sql = "SELECT * FROM Ghe WHERE maPhong = ? ORDER BY hang, soGhe";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhong);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ds.add(mapGhe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Đếm tổng số ghế để phục vụ phát sinh mã tự động
     */
    public int demSoLuongGhe() {
        String sql = "SELECT COUNT(*) FROM Ghe";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ================== THAO TÁC DỮ LIỆU ==================

    /**
     * Thêm ghế mới (Lấy mã phòng từ tham chiếu g.getPhongChieu())
     */
    public boolean themGhe(Ghe g) {
        String sql = "INSERT INTO Ghe (maGhe, maPhong, hang, soGhe, loaiGhe, trangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, g.getMaGhe());
            // TRUY XUẤT THAM CHIẾU ĐỐI TƯỢNG
            stmt.setString(2, g.getPhongChieu().getMaPhong()); 
            stmt.setString(3, g.getHang());
            stmt.setInt(4, g.getSoGhe());
            stmt.setString(5, g.getLoaiGhe());
            stmt.setString(6, g.getTrangThai());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin ghế thông qua tham chiếu
     */
    public boolean capNhatGhe(Ghe g) {
        String sql = "UPDATE Ghe SET maPhong=?, hang=?, soGhe=?, loaiGhe=?, trangThai=? WHERE maGhe=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, g.getPhongChieu().getMaPhong());
            stmt.setString(2, g.getHang());
            stmt.setInt(3, g.getSoGhe());
            stmt.setString(4, g.getLoaiGhe());
            stmt.setString(5, g.getTrangThai());
            stmt.setString(6, g.getMaGhe());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xóa ghế dựa trên mã ghế
     */
    public boolean xoaGhe(String maGhe) {
        String sql = "DELETE FROM Ghe WHERE maGhe = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maGhe);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    public String layMaLonNhat() {
        String sql = "SELECT MAX(maGhe) FROM Ghe";
        try (Connection conn = connect.DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString(1); // Trả về mã lớn nhất, ví dụ "G0050"
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
}