package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

import connect.DatabaseConnection;
import entity.KhachHang;

public class KhachHang_DAO {

    /**
     * Helper: Ánh xạ kết quả từ SQL ResultSet sang đối tượng KhachHang
     */
    private KhachHang mapKhachHang(ResultSet rs) throws SQLException {
        return new KhachHang(
            rs.getString("maKH"),
            rs.getString("tenKhachHang"),
            rs.getString("SDT"),
            rs.getString("Email"),
            rs.getInt("diemTichLuy")
        );
    }

    // ================== NHÓM ĐỌC DỮ LIỆU ==================

    public List<KhachHang> docDanhSachKhachHang() {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ds.add(mapKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ================== NHÓM TÌM KIẾM (CHO UI TRA CỨU) ==================

    public List<KhachHang> timKiemTheoTen(String tenKH) {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE tenKhachHang LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + tenKH + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ds.add(mapKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public List<KhachHang> timKiemTheoMa(String maKH) {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE maKH LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + maKH + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ds.add(mapKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public List<KhachHang> timKiemTheoSDT(String sdt) {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE SDT LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + sdt + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ds.add(mapKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ================== NHÓM THAY ĐỔI DỮ LIỆU (CUD) ==================

    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKH, tenKhachHang, SDT, Email, diemTichLuy) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kh.getMaKH());
            stmt.setString(2, kh.getTenKhachHang());
            stmt.setString(3, kh.getSDT());
            stmt.setString(4, kh.getEmail());
            stmt.setInt(5, kh.getDiemTichLuy());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm khách hàng: " + e.getMessage());
            return false;
        }
    }

    public boolean capNhatKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKhachHang = ?, SDT = ?, Email = ?, diemTichLuy = ? WHERE maKH = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kh.getTenKhachHang());
            stmt.setString(2, kh.getSDT());
            stmt.setString(3, kh.getEmail());
            stmt.setInt(4, kh.getDiemTichLuy());
            stmt.setString(5, kh.getMaKH());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaKhachHang(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE maKH = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa khách hàng: " + e.getMessage() + " (Có thể do ràng buộc hóa đơn)");
            return false;
        }
    }

    // ================== NHÓM LOGIC NÂNG CAO ==================

    /**
     * Lấy mã khách hàng lớn nhất hiện có để phục vụ tự tăng mã (KH000001...)
     */
    public String getMaKHLonNhat() {
        String sql = "SELECT TOP 1 maKH FROM KhachHang ORDER BY maKH DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Thống kê top khách hàng chi tiêu nhiều nhất (Dựa trên bảng HoaDon)
     */
    public Map<String, BigDecimal> getTopKhachHang(int topN) {
        Map<String, BigDecimal> data = new java.util.LinkedHashMap<>();
        String sql = "SELECT TOP (?) kh.tenKhachHang, SUM(hd.tongTien) as DoanhThu " +
                     "FROM KhachHang kh JOIN HoaDon hd ON kh.maKH = hd.maKH " +
                     "GROUP BY kh.maKH, kh.tenKhachHang ORDER BY DoanhThu DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, topN);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                data.put(rs.getString("tenKhachHang"), rs.getBigDecimal("DoanhThu"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
 // ================== NHÓM SẮP XẾP ==================

    /**
     * Sắp xếp theo tên
     * @param tangDan true: A-Z, false: Z-A
     */
    public List<KhachHang> sapXepTheoTen(boolean tangDan) {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang ORDER BY tenKhachHang " + (tangDan ? "ASC" : "DESC");
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ds.add(mapKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Sắp xếp theo điểm tích lũy
     * @param tangDan true: Thấp -> Cao, false: Cao -> Thấp
     */
    public List<KhachHang> sapXepTheoDiem(boolean tangDan) {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang ORDER BY diemTichLuy " + (tangDan ? "ASC" : "DESC");
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ds.add(mapKhachHang(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}