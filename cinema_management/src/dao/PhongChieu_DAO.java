package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connect.DatabaseConnection;
import entity.PhongChieu;

public class PhongChieu_DAO {

    /**
     * Chuyển đổi dữ liệu từ ResultSet sang thực thể PhongChieu
     */
    private PhongChieu mapPhongChieu(ResultSet rs) throws SQLException {
        return new PhongChieu(
            rs.getString("maPhong"),
            rs.getString("tenPhong"),
            rs.getInt("soGhe"),
            rs.getString("loaiPhong")
        );
    }

    // ================== TRUY VẤN DỮ LIỆU ==================

    /**
     * Lấy danh sách toàn bộ phòng chiếu
     */
    public List<PhongChieu> docDanhSachPhongChieu() {
        List<PhongChieu> ds = new ArrayList<>();
        String sql = "SELECT * FROM PhongChieu";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ds.add(mapPhongChieu(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Tìm kiếm phòng chiếu theo mã
     */
    public PhongChieu timPhongTheoMa(String ma) {
        String sql = "SELECT * FROM PhongChieu WHERE maPhong = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ma);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapPhongChieu(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================== THAO TÁC DỮ LIỆU ==================

    public boolean themPhongChieu(PhongChieu p) {
        String sql = "INSERT INTO PhongChieu (maPhong, tenPhong, soGhe, loaiPhong) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getMaPhong());
            stmt.setString(2, p.getTenPhong());
            stmt.setInt(3, p.getSoGhe());
            stmt.setString(4, p.getLoaiPhong());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean capNhatPhongChieu(PhongChieu p) {
        String sql = "UPDATE PhongChieu SET tenPhong=?, soGhe=?, loaiPhong=? WHERE maPhong=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getTenPhong());
            stmt.setInt(2, p.getSoGhe());
            stmt.setString(3, p.getLoaiPhong());
            stmt.setString(4, p.getMaPhong());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean xoaPhongChieu(String ma) {
        String sql = "DELETE FROM PhongChieu WHERE maPhong = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ma);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Trả về false nếu có ràng buộc khóa ngoại với bảng Ghe
            return false; 
        }
    }

    // ================== TIỆN ÍCH ==================

    /**
     * Lấy mã lớn nhất để làm mã tự động P000x
     */
    public String getMaPhongLonNhat() {
        String sql = "SELECT MAX(maPhong) FROM PhongChieu";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}