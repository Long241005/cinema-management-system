package dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import connect.DatabaseConnection;
import entity.Phim;
import entity.TheLoai;

public class Phim_DAO {

    /**
     * Helper: Ánh xạ kết quả từ ResultSet sang đối tượng Phim
     */
    private Phim mapPhim(ResultSet rs) throws SQLException {
        // Khởi tạo đối tượng TheLoai đầy đủ thông tin từ kết quả JOIN
        TheLoai tl = new TheLoai(rs.getString("maTheLoai"), rs.getString("tenTheLoai"));

        return new Phim(
            rs.getString("maPhim"),
            rs.getString("tenPhim"),
            rs.getString("daoDien"),
            tl, // Gán đối tượng thực thể TheLoai
            rs.getInt("thoiLuong"),
            rs.getDate("ngayKhoiChieu").toLocalDate(),
            rs.getString("moTa"),
            rs.getString("duongDanAnh")
        );
    }

    // ================== 1. ĐỌC DỮ LIỆU TỔNG HỢP ==================

    public List<Phim> docDanhSachPhim() {
        List<Phim> ds = new ArrayList<>();
        // Sử dụng JOIN để lấy tên thể loại chính xác từ bảng liên kết[cite: 15]
        String sql = "SELECT p.*, tl.tenTheLoai FROM Phim p JOIN TheLoai tl ON p.maTheLoai = tl.maTheLoai";
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

    // ================== 2. TRA CỨU CHI TIẾT ==================

    public Phim timPhimTheoMa(String maPhim) {
        String sql = "SELECT p.*, tl.tenTheLoai FROM Phim p JOIN TheLoai tl ON p.maTheLoai = tl.maTheLoai WHERE p.maPhim = ?";
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

    // ================== 3. THÊM, XÓA, SỬA ==================

    public boolean themPhim(Phim p) {
        String sql = "INSERT INTO Phim (maPhim, tenPhim, daoDien, maTheLoai, thoiLuong, ngayKhoiChieu, moTa, duongDanAnh) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getMaPhim());
            stmt.setString(2, p.getTenPhim());
            stmt.setString(3, p.getDaoDien());
            stmt.setString(4, p.getTheLoai().getMaTheLoai()); // Lấy mã từ đối tượng thực thể[cite: 15]
            stmt.setInt(5, p.getThoiLuong());
            stmt.setDate(6, java.sql.Date.valueOf(p.getNgayKhoiChieu())); 
            stmt.setString(7, p.getMoTa());
            stmt.setString(8, p.getDuongDanAnh());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatPhim(Phim p) {
        // Đảm bảo cập nhật mã thể loại từ thực thể[cite: 15]
        String sql = "UPDATE Phim SET tenPhim=?, daoDien=?, maTheLoai=?, thoiLuong=?, ngayKhoiChieu=?, moTa=?, duongDanAnh=? WHERE maPhim=?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getTenPhim());
            stmt.setString(2, p.getDaoDien());
            stmt.setString(3, p.getTheLoai().getMaTheLoai()); 
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

    // ================== 4. TIỆN ÍCH ==================

    public String phatSinhMaPhimTuDong() {
        // Sắp xếp theo độ dài trước, sau đó mới đến giá trị chuỗi để tìm số lớn nhất thực sự
        String sql = "SELECT TOP 1 maPhim FROM Phim ORDER BY LEN(maPhim) DESC, maPhim DESC";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String lastMa = rs.getString(1); // Ví dụ lấy được "P0015"
                
                // Cắt bỏ chữ 'P', chuyển phần còn lại thành số và cộng thêm 1[cite: 14]
                int nextNum = Integer.parseInt(lastMa.substring(1)) + 1;
                
                // Định dạng lại thành P kèm 4 chữ số (ví dụ: P0016)[cite: 14]
                return String.format("P%04d", nextNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "P0001"; // Nếu bảng trống thì bắt đầu từ P0001[cite: 14]
    }

    // ================== 5. TÌM KIẾM NÂNG CAO (LIVE SEARCH) ==================

    public List<Phim> timKiemPhim(String ma, String ten, String theLoai) {
        List<Phim> ds = new ArrayList<>();
        // StringBuilder giúp nối chuỗi SQL linh hoạt theo tham số đầu vào[cite: 15]
        StringBuilder sql = new StringBuilder("SELECT p.*, tl.tenTheLoai FROM Phim p JOIN TheLoai tl ON p.maTheLoai = tl.maTheLoai WHERE 1=1");

        if (ma != null && !ma.isEmpty()) sql.append(" AND p.maPhim LIKE ?");
        if (ten != null && !ten.isEmpty()) sql.append(" AND p.tenPhim LIKE ?");
        // Lọc theo tên thể loại để khớp với lựa chọn trên UI[cite: 15]
        if (theLoai != null && !theLoai.equals("Tất cả")) sql.append(" AND tl.tenTheLoai LIKE ?");

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