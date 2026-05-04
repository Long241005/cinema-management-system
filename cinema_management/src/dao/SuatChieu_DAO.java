package dao;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import connect.DatabaseConnection;
import entity.SuatChieu;
import entity.Phim;
import entity.PhongChieu;
import entity.Ve;

public class SuatChieu_DAO {



	public List<Object[]> getAllSuatChieuWithPrice() {
	    List<Object[]> ds = new ArrayList<>();
	    // Lấy DISTINCT giaVe từ bảng Ve ứng với mỗi SuatChieu
	    String sql = "SELECT sc.maSC, p.tenPhim, pc.tenPhong, sc.gioChieu, v.giaVe " +
	                 "FROM SuatChieu sc " +
	                 "JOIN Phim p ON sc.maPhim = p.maPhim " +
	                 "JOIN PhongChieu pc ON sc.maPhong = pc.maPhong " +
	                 "JOIN (SELECT maSC, MAX(giaVe) as giaVe FROM Ve GROUP BY maSC) v ON sc.maSC = v.maSC";
	    try (Connection conn = DatabaseConnection.getInstance().getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        while (rs.next()) {
	            Object[] row = {
	                rs.getString("maSC"),
	                rs.getString("tenPhim"),
	                rs.getString("tenPhong"),
	                rs.getTimestamp("gioChieu").toLocalDateTime(),
	                rs.getBigDecimal("giaVe") // Lấy giá vé từ lớp Ve trong DB
	            };
	            ds.add(row);
	        }
	    } catch (SQLException e) { e.printStackTrace(); }
	    return ds;
	}

	public boolean themSuatChieu(SuatChieu sc, BigDecimal giaVe) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false); 

            String sqlSC = "INSERT INTO SuatChieu (maSC, maPhong, maPhim, ngayChieu, gioChieu) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmtSC = conn.prepareStatement(sqlSC)) {
                stmtSC.setString(1, sc.getMaSC());
                stmtSC.setString(2, sc.getPhongChieu().getMaPhong());
                stmtSC.setString(3, sc.getPhim().getMaPhim());
                stmtSC.setDate(4, Date.valueOf(sc.getNgayChieu()));
                stmtSC.setTimestamp(5, Timestamp.valueOf(sc.getGioChieu()));
                stmtSC.executeUpdate();
            }

            String sqlVe = "INSERT INTO Ve (maVe, maSC, maGhe, giaVe, trangThai) " +
                           "SELECT 'V' + ? + g.maGhe, ?, g.maGhe, ?, N'Trống' " +
                           "FROM Ghe g WHERE g.maPhong = ?";
            
            try (PreparedStatement stmtVe = conn.prepareStatement(sqlVe)) {
                stmtVe.setString(1, sc.getMaSC());   
                stmtVe.setString(2, sc.getMaSC());   
                stmtVe.setBigDecimal(3, giaVe);      
                stmtVe.setString(4, sc.getPhongChieu().getMaPhong()); 
                stmtVe.executeUpdate();
            }

            conn.commit(); 
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        }
    }

	public boolean xoaSuatChieu(String maSC) {
	    Connection conn = null;
	    try {
	        conn = DatabaseConnection.getInstance().getConnection();
	        conn.setAutoCommit(false); // Bắt đầu giao dịch

	        // 1. Xóa tất cả vé của suất chiếu này trước
	        String sqlVe = "DELETE FROM Ve WHERE maSC = ?";
	        PreparedStatement stmtVe = conn.prepareStatement(sqlVe);
	        stmtVe.setString(1, maSC);
	        stmtVe.executeUpdate();

	        // 2. Sau đó mới xóa suất chiếu
	        String sqlSC = "DELETE FROM SuatChieu WHERE maSC = ?";
	        PreparedStatement stmtSC = conn.prepareStatement(sqlSC);
	        stmtSC.setString(1, maSC);
	        int result = stmtSC.executeUpdate();

	        conn.commit(); // Hoàn tất xóa cả hai
	        return result > 0;
	    } catch (SQLException e) {
	        if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
	        e.printStackTrace();
	        return false;
	    }
	}
    public boolean capNhatSuatChieu(SuatChieu sc) {
        String sql = "UPDATE SuatChieu SET maPhong = ?, maPhim = ?, ngayChieu = ?, gioChieu = ? WHERE maSC = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sc.getPhongChieu().getMaPhong());
            stmt.setString(2, sc.getPhim().getMaPhim());
            stmt.setDate(3, java.sql.Date.valueOf(sc.getNgayChieu()));
            stmt.setTimestamp(4, java.sql.Timestamp.valueOf(sc.getGioChieu()));
            stmt.setString(5, sc.getMaSC());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int demSoLuongSuatChieu() {
        String sql = "SELECT COUNT(*) FROM SuatChieu";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public List<SuatChieu> timKiemTheoMa(String maSC) {
        List<SuatChieu> ds = new ArrayList<>();
        String sql = "SELECT sc.*, p.tenPhim, pc.tenPhong FROM SuatChieu sc " +
                     "JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON sc.maPhong = pc.maPhong WHERE sc.maSC LIKE ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + maSC + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ds.add(mapResultToEntity(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    // Helper để tái sử dụng code map ResultSet
    private SuatChieu mapResultToEntity(ResultSet rs) throws SQLException {
        Phim phim = new Phim();
        phim.setMaPhim(rs.getString("maPhim"));
        phim.setTenPhim(rs.getString("tenPhim"));

        PhongChieu pc = new PhongChieu();
        pc.setMaPhong(rs.getString("maPhong"));
        pc.setTenPhong(rs.getString("tenPhong"));

        SuatChieu sc = new SuatChieu(
            rs.getString("maSC"),
            pc, phim,
            rs.getDate("ngayChieu").toLocalDate(),
            rs.getTimestamp("gioChieu").toLocalDateTime()
        );
        
        // Lưu ý: Nếu lớp SuatChieu của bạn đã thêm thuộc tính giaVe, hãy set ở đây:
        // sc.setGiaVe(rs.getBigDecimal("giaVe"));
        
        return sc;
    }
    public List<Object[]> timKiemSuatChieuWithPrice(String keyword) {
        List<Object[]> ds = new ArrayList<>();
        // Kết hợp JOIN phim, phòng và bảng vé (lấy giá vé cao nhất đại diện)
        String sql = "SELECT sc.maSC, p.tenPhim, pc.tenPhong, sc.gioChieu, v.giaVe " +
                     "FROM SuatChieu sc " +
                     "JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON sc.maPhong = pc.maPhong " +
                     "LEFT JOIN (SELECT maSC, MAX(giaVe) as giaVe FROM Ve GROUP BY maSC) v ON sc.maSC = v.maSC " +
                     "WHERE sc.maSC LIKE ? OR p.tenPhim LIKE ?";
                     
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = {
                    rs.getString("maSC"),
                    rs.getString("tenPhim"),
                    rs.getString("tenPhong"),
                    rs.getTimestamp("gioChieu").toLocalDateTime(),
                    rs.getBigDecimal("giaVe") // Lấy giá vé từ bảng Ve tương ứng
                };
                ds.add(row);
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return ds;
    }
    public List<SuatChieu> timKiemSuatChieu(String keyword) {
        List<SuatChieu> ds = new ArrayList<>();
        // Tìm kiếm theo mã suất chiếu HOẶC tên phim
        String sql = "SELECT sc.*, p.tenPhim, pc.tenPhong FROM SuatChieu sc " +
                     "JOIN Phim p ON sc.maPhim = p.maPhim " +
                     "JOIN PhongChieu pc ON sc.maPhong = pc.maPhong " +
                     "WHERE sc.maSC LIKE ? OR p.tenPhim LIKE ?";
        try (Connection conn = connect.DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ds.add(mapResultToEntity(rs)); // Sử dụng lại helper mapResultToEntity đã có
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }
    public String getMaSuatChieuLonNhat() {
        String ma = "SC0000";
        String sql = "SELECT MAX(maSC) FROM SuatChieu";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next() && rs.getString(1) != null) {
                ma = rs.getString(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ma;
    }
    public boolean capNhatGiaVe(String maSC, BigDecimal giaVeMoi) {
        // Cập nhật cột giaVe trong bảng Ve ứng với mã suất chiếu
        String sql = "UPDATE Ve SET giaVe = ? WHERE maSC = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, giaVeMoi);
            stmt.setString(2, maSC);
            return stmt.executeUpdate() >= 0; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}