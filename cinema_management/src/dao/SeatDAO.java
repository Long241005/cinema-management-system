package dao;

import entity.Seat;
import connect.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {
    
    /**
     * Lấy ghế theo ID
     */
    public Seat getSeatById(int seatId) {
        String sql = "SELECT * FROM ghe WHERE id_ghe = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seatId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToSeat(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả ghế của một phòng
     */
    public List<Seat> getSeatsByTheaterId(int theaterId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM ghe WHERE id_phong = ? ORDER BY hang_ghe, so_ghe";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, theaterId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                seats.add(mapResultSetToSeat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }
    
    /**
     * Lấy ghế theo trạng thái
     */
    public List<Seat> getSeatsByStatus(int theaterId, String status) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM ghe WHERE id_phong = ? AND trang_thai = ? ORDER BY hang_ghe, so_ghe";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, theaterId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                seats.add(mapResultSetToSeat(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seats;
    }
    
    /**
     * Thêm ghế mới
     */
    public boolean addSeat(Seat seat) {
        String sql = "INSERT INTO ghe (id_phong, hang_ghe, so_ghe, loai_ghe, gia, trang_thai) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seat.getTheaterId());
            pstmt.setString(2, seat.getSeatRow());
            pstmt.setInt(3, seat.getSeatNumber());
            pstmt.setString(4, seat.getSeatType());
            pstmt.setDouble(5, seat.getPrice());
            pstmt.setString(6, seat.getStatus());
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật trạng thái ghế
     */
    public boolean updateSeatStatus(int seatId, String status) {
        String sql = "UPDATE ghe SET trang_thai = ? WHERE id_ghe = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, seatId);
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật ghế
     */
    public boolean updateSeat(Seat seat) {
        String sql = "UPDATE ghe SET hang_ghe = ?, so_ghe = ?, loai_ghe = ?, gia = ?, trang_thai = ? WHERE id_ghe = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, seat.getSeatRow());
            pstmt.setInt(2, seat.getSeatNumber());
            pstmt.setString(3, seat.getSeatType());
            pstmt.setDouble(4, seat.getPrice());
            pstmt.setString(5, seat.getStatus());
            pstmt.setInt(6, seat.getSeatId());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa ghế
     */
    public boolean deleteSeat(int seatId) {
        String sql = "DELETE FROM ghe WHERE id_ghe = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, seatId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map ResultSet thành Seat object
     */
    private Seat mapResultSetToSeat(ResultSet rs) throws SQLException {
        Seat seat = new Seat();
        seat.setSeatId(rs.getInt("id_ghe"));
        seat.setTheaterId(rs.getInt("id_phong"));
        seat.setSeatRow(rs.getString("hang_ghe"));
        seat.setSeatNumber(rs.getInt("so_ghe"));
        seat.setSeatType(rs.getString("loai_ghe"));
        seat.setPrice(rs.getDouble("gia"));
        seat.setStatus(rs.getString("trang_thai"));
        return seat;
    }
}
