package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import connect.DatabaseConnection;
import entity.TheLoai;

public class TheLoai_DAO {

    /**
     * Lấy toàn bộ danh sách thể loại từ Database
     * Dùng để đổ dữ liệu vào JComboBox trong ThemPhim_UI và CapNhatPhim_UI
     */
    public List<TheLoai> getAllTheLoai() {
        List<TheLoai> ds = new ArrayList<>();
        String sql = "SELECT * FROM TheLoai";
        
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                TheLoai tl = new TheLoai(
                    rs.getString("maTheLoai"), 
                    rs.getString("tenTheLoai")
                );
                ds.add(tl);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Thêm một thể loại mới vào Database
     * Dùng cho nút "+" thêm nhanh trên giao diện
     */
    public boolean themTheLoai(TheLoai tl) {
        String sql = "INSERT INTO TheLoai (maTheLoai, tenTheLoai) VALUES (?, ?)";
        
        // 1. Lấy mã mới trước khi mở Connection cho lệnh INSERT
        String maMoi = phatSinhMaTL(); 
        
        // 2. Mở kết nối và thực hiện Insert
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, maMoi); // Dòng 47 sẽ không còn lỗi closed connection
            stmt.setString(2, tl.getTenTheLoai());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Tự động phát sinh mã thể loại tiếp theo (TL0001, TL0002...)
     */
    public String phatSinhMaTL() {
        String sql = "SELECT MAX(maTheLoai) FROM TheLoai";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String lastMa = rs.getString(1); // Ví dụ: "TL0005"
                if (lastMa != null) {
                    int nextNum = Integer.parseInt(lastMa.substring(2)) + 1;
                    return String.format("TL%04d", nextNum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "TL0001"; // Trả về mã mặc định nếu bảng trống
    }

    /**
     * Tìm thể loại theo mã (Hỗ trợ khi cần load dữ liệu chi tiết)
     */
    public TheLoai timTheLoaiTheoMa(String ma) {
        String sql = "SELECT * FROM TheLoai WHERE maTheLoai = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, ma);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new TheLoai(rs.getString(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}