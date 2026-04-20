package dao;

import entity.Theater;
import connect.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TheaterDAO {
    
    /**
     * Lấy phòng theo ID
     */
    public Theater getTheaterById(int theaterId) {
        String sql = "SELECT * FROM phong WHERE id_phong = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, theaterId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTheater(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả phòng
     */
    public List<Theater> getAllTheaters() {
        List<Theater> theaters = new ArrayList<>();
        String sql = "SELECT * FROM phong ORDER BY ten_phong";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                theaters.add(mapResultSetToTheater(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theaters;
    }
    
    /**
     * Thêm phòng mới
     */
    public boolean addTheater(Theater theater) {
        String sql = "INSERT INTO phong (ten_phong, tong_so_ghe, trang_thai) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, theater.getTheaterName());
            pstmt.setInt(2, theater.getTotalSeats());
            pstmt.setString(3, theater.getStatus());
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật phòng
     */
    public boolean updateTheater(Theater theater) {
        String sql = "UPDATE phong SET ten_phong = ?, tong_so_ghe = ?, trang_thai = ? WHERE id_phong = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, theater.getTheaterName());
            pstmt.setInt(2, theater.getTotalSeats());
            pstmt.setString(3, theater.getStatus());
            pstmt.setInt(4, theater.getTheaterId());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa phòng
     */
    public boolean deleteTheater(int theaterId) {
        String sql = "DELETE FROM phong WHERE id_phong = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, theaterId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map ResultSet thành Theater object
     */
    private Theater mapResultSetToTheater(ResultSet rs) throws SQLException {
        Theater theater = new Theater();
        theater.setTheaterId(rs.getInt("id_phong"));
        theater.setTheaterName(rs.getString("ten_phong"));
        theater.setTotalSeats(rs.getInt("tong_so_ghe"));
        theater.setStatus(rs.getString("trang_thai"));
        return theater;
    }
}
