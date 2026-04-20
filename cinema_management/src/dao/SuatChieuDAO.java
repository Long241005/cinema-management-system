package dao;

import entity.Showtime;
import connect.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDAO {
    
    /**
     * Lấy suất chiếu theo ID
     */
    public Showtime getShowtimeById(int showtimeId) {
        String sql = "SELECT * FROM suat_chieu WHERE id_suat = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, showtimeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToShowtime(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả suất chiếu
     */
    public List<Showtime> getAllShowtimes() {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM suat_chieu ORDER BY thoi_gian_bat_dau DESC";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                showtimes.add(mapResultSetToShowtime(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }
    
    /**
     * Lấy suất chiếu theo phim
     */
    public List<Showtime> getShowtimesByFilmId(int filmId) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM suat_chieu WHERE id_phim = ? ORDER BY thoi_gian_bat_dau";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, filmId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                showtimes.add(mapResultSetToShowtime(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }
    
    /**
     * Lấy suất chiếu theo phòng
     */
    public List<Showtime> getShowtimesByTheaterId(int theaterId) {
        List<Showtime> showtimes = new ArrayList<>();
        String sql = "SELECT * FROM suat_chieu WHERE id_phong = ? ORDER BY thoi_gian_bat_dau";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, theaterId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                showtimes.add(mapResultSetToShowtime(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return showtimes;
    }
    
    /**
     * Thêm suất chiếu mới
     */
    public boolean addShowtime(Showtime showtime) {
        String sql = "INSERT INTO suat_chieu (id_phim, id_phong, thoi_gian_bat_dau, thoi_gian_ket_thuc, gia_ve, trang_thai) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, showtime.getFilmId());
            pstmt.setInt(2, showtime.getTheaterId());
            pstmt.setTimestamp(3, Timestamp.valueOf(showtime.getStartTime()));
            pstmt.setTimestamp(4, Timestamp.valueOf(showtime.getEndTime()));
            pstmt.setDouble(5, showtime.getTicketPrice());
            pstmt.setString(6, showtime.getStatus());
            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Cập nhật suất chiếu
     */
    public boolean updateShowtime(Showtime showtime) {
        String sql = "UPDATE suat_chieu SET id_phim = ?, id_phong = ?, thoi_gian_bat_dau = ?, "
                   + "thoi_gian_ket_thuc = ?, gia_ve = ?, trang_thai = ? WHERE id_suat = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, showtime.getFilmId());
            pstmt.setInt(2, showtime.getTheaterId());
            pstmt.setTimestamp(3, Timestamp.valueOf(showtime.getStartTime()));
            pstmt.setTimestamp(4, Timestamp.valueOf(showtime.getEndTime()));
            pstmt.setDouble(5, showtime.getTicketPrice());
            pstmt.setString(6, showtime.getStatus());
            pstmt.setInt(7, showtime.getShowtimeId());
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Xóa suất chiếu
     */
    public boolean deleteShowtime(int showtimeId) {
        String sql = "DELETE FROM suat_chieu WHERE id_suat = ?";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, showtimeId);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Map ResultSet thành Showtime object
     */
    private Showtime mapResultSetToShowtime(ResultSet rs) throws SQLException {
        Showtime showtime = new Showtime();
        showtime.setShowtimeId(rs.getInt("id_suat"));
        showtime.setFilmId(rs.getInt("id_phim"));
        showtime.setTheaterId(rs.getInt("id_phong"));
        showtime.setStartTime(rs.getTimestamp("thoi_gian_bat_dau").toLocalDateTime());
        showtime.setEndTime(rs.getTimestamp("thoi_gian_ket_thuc").toLocalDateTime());
        showtime.setTicketPrice(rs.getDouble("gia_ve"));
        showtime.setStatus(rs.getString("trang_thai"));
        return showtime;
    }
}
