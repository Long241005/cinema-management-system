package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import connect.DatabaseConnection;
import entity.Phim;

public class Phim_DAO {

    /**
     * Helper: Ánh xạ kết quả từ ResultSet sang đối tượng Phim
     */
    private Phim mapPhim(ResultSet rs) throws SQLException {
        return new Phim(
            rs.getString("maPhim"),
            rs.getString("tenPhim"),
            rs.getString("daoDien"),
            rs.getString("theLoai"),
            rs.getInt("thoiLuong"),
            rs.getDate("ngayKhoiChieu").toLocalDate(), // Chuyển sql.Date sang LocalDate
            rs.getString("moTa"),
            rs.getString("duongDanAnh") // Tên file ảnh khớp với logic UI
        );
    }

    // ================== 1. ĐỌC DỮ LIỆU (Dùng cho TraCuuPhim_UI & CapNhatPhim_UI) ==================

    public List<Phim> docDanhSachPhim() {
        List<Phim> ds = new ArrayList<>();
        String sql = "SELECT * FROM Phim";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ds.add(mapPhim(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ================== 2. TÌM KIẾM CHI TIẾT (Khớp với CapNhatPhim_UI) ==================

    /**
     * Hàm này cực kỳ quan trọng để đổ dữ liệu lên Form cập nhật khi click bảng
     */
    public Phim timPhimTheoMa(String maPhim) {
        String sql = "SELECT * FROM Phim WHERE maPhim = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhim);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapPhim(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================== 3. THÊM, XÓA, SỬA (Khớp với ThemPhim_UI & CapNhatPhim_UI) ==================

    public boolean themPhim(Phim p) {
        String sql = "INSERT INTO Phim (maPhim, tenPhim, daoDien, theLoai, thoiLuong, ngayKhoiChieu, moTa, duongDanAnh) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getMaPhim());
            stmt.setString(2, p.getTenPhim());
            stmt.setString(3, p.getDaoDien());
            stmt.setString(4, p.getTheLoai());
            stmt.setInt(5, p.getThoiLuong());
            stmt.setDate(6, Date.valueOf(p.getNgayKhoiChieu())); // Chuyển LocalDate sang sql.Date
            stmt.setString(7, p.getMoTa());
            stmt.setString(8, p.getDuongDanAnh()); // Lưu tên file đã chọn
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatPhim(Phim p) {
        String sql = "UPDATE Phim SET tenPhim=?, daoDien=?, theLoai=?, thoiLuong=?, ngayKhoiChieu=?, moTa=?, duongDanAnh=? WHERE maPhim=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getTenPhim());
            stmt.setString(2, p.getDaoDien());
            stmt.setString(3, p.getTheLoai());
            stmt.setInt(4, p.getThoiLuong());
            stmt.setDate(5, Date.valueOf(p.getNgayKhoiChieu()));
            stmt.setString(6, p.getMoTa());
            stmt.setString(7, p.getDuongDanAnh());
            stmt.setString(8, p.getMaPhim());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaPhim(String maPhim) {
        String sql = "DELETE FROM Phim WHERE maPhim = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhim);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // ================== 4. TÌM KIẾM LIVE SEARCH (Khớp với TraCuuPhim_UI) ==================

    public List<Phim> timKiemPhim(String ma, String ten, String theLoai) {
        List<Phim> ds = new ArrayList<>();
        // Xây dựng câu lệnh SQL linh hoạt dựa trên input của người dùng
        StringBuilder sql = new StringBuilder("SELECT * FROM Phim WHERE 1=1");
        
        if (ma != null && !ma.isEmpty()) sql.append(" AND maPhim LIKE ?");
        if (ten != null && !ten.isEmpty()) sql.append(" AND tenPhim LIKE ?");
        if (theLoai != null && !theLoai.equals("Tất cả")) sql.append(" AND theLoai LIKE ?");

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (ma != null && !ma.isEmpty()) stmt.setString(paramIndex++, "%" + ma + "%");
            if (ten != null && !ten.isEmpty()) stmt.setString(paramIndex++, "%" + ten + "%");
            if (theLoai != null && !theLoai.equals("Tất cả")) stmt.setString(paramIndex++, "%" + theLoai + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapPhim(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}