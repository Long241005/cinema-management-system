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
        // Khởi tạo đối tượng TheLoai từ dữ liệu JOIN[cite: 2]
        TheLoai tl = new TheLoai(rs.getString("maTheLoai"), rs.getString("tenTheLoai"));

        return new Phim(
            rs.getString("maPhim"),
            rs.getString("tenPhim"),
            rs.getString("daoDien"),
            tl, // Truyền đối tượng vào[cite: 2]
            rs.getInt("thoiLuong"),
            rs.getDate("ngayKhoiChieu").toLocalDate(),
            rs.getString("moTa"),
            rs.getString("duongDanAnh")
        );
    }

    // ================== 1. ĐỌC DỮ LIỆU (Dùng cho TraCuuPhim_UI & CapNhatPhim_UI) ==================

	public List<Phim> docDanhSachPhim() {
	    List<Phim> ds = new ArrayList<>();
	    // Cần JOIN với bảng TheLoai để lấy được tenTheLoai
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

    // ================== 2. TÌM KIẾM CHI TIẾT (Khớp với CapNhatPhim_UI) ==================

    /**
     * Hàm này cực kỳ quan trọng để đổ dữ liệu lên Form cập nhật khi click bảng
     */
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

    // ================== 3. THÊM, XÓA, SỬA (Khớp với ThemPhim_UI & CapNhatPhim_UI) ==================

    public boolean themPhim(Phim p) {
        // Câu lệnh SQL phải khớp với các cột trong Database của bạn
        String sql = "INSERT INTO Phim (maPhim, tenPhim, daoDien, maTheLoai, thoiLuong, ngayKhoiChieu, moTa, duongDanAnh) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connect.DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, p.getMaPhim());
            stmt.setString(2, p.getTenPhim());
            stmt.setString(3, p.getDaoDien());
            
            // SỬA LỖI TẠI ĐÂY: Lấy mã từ đối tượng thực thể TheLoai
            stmt.setString(4, p.getTheLoai().getMaTheLoai()); 
            
            stmt.setInt(5, p.getThoiLuong());
            
            // Chuyển LocalDate sang sql.Date để lưu vào SQL Server
            stmt.setDate(6, java.sql.Date.valueOf(p.getNgayKhoiChieu())); 
            
            stmt.setString(7, p.getMoTa());
            stmt.setString(8, p.getDuongDanAnh()); // hoặc getDuongDanAnh() tùy bạn đặt tên
            
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
    public String phatSinhMaPhimTuDong() {
        // Lấy mã phim lớn nhất dựa trên thứ tự giảm dần[cite: 1, 2]
        String sql = "SELECT TOP 1 maPhim FROM Phim ORDER BY maPhim DESC";
        try (Connection conn = connect.DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String lastMa = rs.getString(1); // Ví dụ: "P0005"[cite: 1, 2]
                // Cắt chuỗi từ vị trí index 1 để lấy phần số "0005" rồi cộng thêm 1[cite: 1]
                int nextNum = Integer.parseInt(lastMa.substring(1)) + 1;
                // Trả về định dạng P kèm 4 chữ số (VD: P0006)
                return String.format("P%04d", nextNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "P0001"; // Nếu bảng trống, trả về mã mặc định đầu tiên
    }

    // ================== 4. TÌM KIẾM LIVE SEARCH (Khớp với TraCuuPhim_UI) ==================

    public List<Phim> timKiemPhim(String ma, String ten, String theLoai) {
        List<Phim> ds = new ArrayList<>();
        // Sử dụng JOIN để lấy được tenTheLoai
        StringBuilder sql = new StringBuilder("SELECT p.*, tl.tenTheLoai FROM Phim p JOIN TheLoai tl ON p.maTheLoai = tl.maTheLoai WHERE 1=1");

        if (ma != null && !ma.isEmpty()) sql.append(" AND p.maPhim LIKE ?");
        if (ten != null && !ten.isEmpty()) sql.append(" AND p.tenPhim LIKE ?");
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